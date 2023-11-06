FROM eclipse-temurin:21-jdk-alpine as build

WORKDIR /opt/build

ARG RELEASE=21

COPY *.jar ./application.jar

RUN java -Djarmode=layertools -jar application.jar extract --destination extracted

RUN jdeps  \
    --ignore-missing-deps -q -recursive  \
    --multi-release ${RELEASE}  \
    --print-module-deps  \
    -cp 'extracted/dependencies/BOOT-INF/lib/*' application.jar > deps.info

RUN jlink \
         --add-modules $(cat deps.info) \
         --strip-java-debug-attributes \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output jdk

FROM alpine:3.18

VOLUME /tmp

ARG BUILD_PATH=/opt/build
ARG EXTRACTED=/opt/build/extracted
ARG APPLICATION_USER=spring-app


ENV JAVA_HOME=/jdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"

RUN adduser --no-create-home -u 1000 -D $APPLICATION_USER

RUN mkdir /app && \
    chown -R $APPLICATION_USER /app

USER 1000

WORKDIR /app

COPY --from=build $BUILD_PATH/jdk $JAVA_HOME
COPY --from=build $EXTRACTED/dependencies/ ./
COPY --from=build $EXTRACTED/spring-boot-loader/ ./
COPY --from=build $EXTRACTED/application/ ./

HEALTHCHECK --interval=5s --timeout=5s --retries=10 CMD wget -qO- http://localhost:8093/actuator/health/ | grep UP || exit 1

EXPOSE 8093

ENTRYPOINT ["java", "-cp", "/app", "org.springframework.boot.loader.JarLauncher"]
