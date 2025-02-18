FROM gradle:jdk21 as builder
WORKDIR /app

COPY gradle ./gradle
COPY libs ./libs
COPY projects ./projects
COPY build.gradle settings.gradle boot.gradle ./

RUN gradle jar build -x test

FROM openjdk:21-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*-*.jar app.jar
EXPOSE 6969
ENTRYPOINT ["java", "-jar", "/app/app.jar"]