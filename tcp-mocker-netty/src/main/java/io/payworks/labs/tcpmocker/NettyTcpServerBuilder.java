package io.payworks.labs.tcpmocker;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

public class NettyTcpServerBuilder extends DispatchingDataHandlerServerBuilder<NettyTcpServer, NettyTcpServerBuilder> {

    private final int acceptThreads;
    private final int connectThreads;
    private final int queueSize;

    public NettyTcpServerBuilder(final int acceptThreads,
                                 final int connectThreads,
                                 final int queueSize) {
        this.acceptThreads = acceptThreads;
        this.connectThreads = connectThreads;
        this.queueSize = queueSize;
    }

    public NettyTcpServerBuilder() {
        this(1, 5, 100);
    }

    @Override
    public NettyTcpServer build() {
        try {
            return createNettyTcpServer();
        } catch (final Exception e) {
            throw new TcpServerException(e);
        }
    }

    private NettyTcpServer createNettyTcpServer() throws Exception {
        final DefaultThreadFactory acceptThreadFactory = new DefaultThreadFactory("accept");
        final DefaultThreadFactory connectThreadFactory = new DefaultThreadFactory("connect");

        final EventLoopGroup acceptGroup = new NioEventLoopGroup(acceptThreads, acceptThreadFactory);
        final EventLoopGroup connectGroup = new NioEventLoopGroup(connectThreads, connectThreadFactory);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            acceptGroup.shutdownGracefully();
            connectGroup.shutdownGracefully();
        }));

        final ServerBootstrap bootstrap = new ServerBootstrap()
                .group(acceptGroup, connectGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, queueSize)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(final SocketChannel ch) {
                        final ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new NettyDataHandlerAdapter(createDataHandlerDispatcher()));
                    }
                });

        return new NettyTcpServer(bootstrap.bind(getPort()));
    }

    @Override
    protected NettyTcpServerBuilder self() {
        return this;
    }
}
