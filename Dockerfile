#├── Dockerfile
#├── extracted-jar
#│   ├── application
#│   ├── dependencies
#│   └── spring-boot-loader
#└── jdk
#    ├── bin

FROM alpine:3.18

VOLUME /tmp

ARG EXTRACTED=/extracted-jar
ARG APPLICATION_USER=spring-app

COPY jdk ./jdk
ENV JAVA_HOME=/jdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"

RUN adduser --no-create-home -u 1000 -D $APPLICATION_USER

RUN mkdir /app && chown -R $APPLICATION_USER /app

USER 1000

WORKDIR /app

#COPY jdk $JAVA_HOME
COPY $EXTRACTED/dependencies/ ./
COPY $EXTRACTED/spring-boot-loader/ ./
COPY $EXTRACTED/application/ ./

EXPOSE 8093

ENTRYPOINT ["java", "-cp", "/app", "org.springframework.boot.loader.JarLauncher"]
