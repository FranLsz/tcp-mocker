FROM openjdk:11-jre-slim

MAINTAINER Andrey Saksonov <andrey.saksonov@payworks.com>

ARG TCPPORT=10001
ARG TCPMAPPINGSPATH=/var/lib/tcp-mocker/tcp-mappings

ARG WEBPORT=8080

ARG APP_DIR
ARG APP_MAIN

VOLUME /tmp

COPY ${APP_DIR}/BOOT-INF/lib /app/lib
COPY ${APP_DIR}/META-INF /app/META-INF
COPY ${APP_DIR}/BOOT-INF/classes /app

ENV MOCKSERVER_PORT=$TCPPORT \
    MOCKSERVER_TCPMAPPINGSPATH=$TCPMAPPINGSPATH \
    SERVER_PORT=$WEBPORT \
    JAVA_OPTIONS="-Dfile.encoding=UTF-8" \
    JAVA_MAIN=$APP_MAIN

EXPOSE $TCPPORT $WEBPORT

VOLUME ["${TCPMAPPINGSPATH}"]

CMD sh -c "java ${JAVA_OPTIONS} -cp app:app/lib/* ${JAVA_MAIN}"
