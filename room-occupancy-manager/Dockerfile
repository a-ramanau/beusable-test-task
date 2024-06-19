FROM eclipse-temurin:21.0.3_9-jre-alpine as builder

WORKDIR application

COPY build/libs/*jar ./app.jar

RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:21.0.3_9-jre-alpine

RUN apk --no-cache add curl

RUN addgroup -S bu-test && adduser -S bu-test -G bu-test \
    && mkdir /application && chown bu-test:bu-test /application \
    && mkdir /application/logs && chown bu-test:bu-test /application/logs
USER bu-test:bu-test

WORKDIR application

COPY --from=builder /application/dependencies/ .
COPY --from=builder /application/spring-boot-loader/ .
COPY --from=builder /application/snapshot-dependencies/ .
COPY --from=builder /application/application/ .

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
