FROM alpine:3.18

VOLUME /tmp

ARG APPLICATION_USER=spring-app

#COPY jdk ./jdk
ENV JAVA_HOME=/jdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"

RUN adduser --no-create-home -u 1000 -D $APPLICATION_USER

RUN mkdir /app
RUN chown -R $APPLICATION_USER /app

USER 1000

WORKDIR /app

COPY jdk $JAVA_HOME
COPY extracted-jar/dependencies/ ./
COPY extracted-jar/spring-boot-loader/ ./
COPY extracted-jar/application/ ./

EXPOSE 8093

ENTRYPOINT ["java", "-cp", "/app", "org.springframework.boot.loader.JarLauncher"]