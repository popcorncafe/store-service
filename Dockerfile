FROM eclipse-temurin:21-jdk-alpine as build-jre

WORKDIR /opt

COPY jdk-modules.info .

RUN jlink \
             --add-modules $(cat jdk-modules.info) \
             --no-header-files \
             --no-man-pages \
             --strip-debug \
             --output jdk

FROM alpine:3.19

VOLUME /tmp

ENV JAR_PATH=extracted-jar
ENV APP_USER=spring-app

ENV JAVA_HOME=/jdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"

RUN adduser --no-create-home -u 1000 -D $APP_USER

RUN mkdir /app && chown -R $APP_USER /app

USER 1000

WORKDIR /app

COPY --from=build-jre opt/jdk $JAVA_HOME
COPY $JAR_PATH/dependencies/ ./
COPY $JAR_PATH/spring-boot-loader/ ./
COPY $JAR_PATH/application/ ./

EXPOSE 8093

ENTRYPOINT ["java", "-cp", "/app", "org.springframework.boot.loader.launch.JarLauncher"]