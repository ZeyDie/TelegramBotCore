FROM openjdk:21-slim AS builder

RUN apt-get update && \
    apt-get install -y gradle

COPY gradle /app/gradle
COPY libs /app/libs
COPY projects /app/projects
COPY gradlew gradlew.bat  /app/
COPY build.gradle settings.gradle boot.gradle /app/

WORKDIR /app

RUN chmod +X gradlew

RUN ./gradlew clean
RUN ./gradlew build -x test
RUN ./gradlew build -x test

RUN ls && pwd

FROM openjdk:21-slim

WORKDIR /app/container

COPY --from=builder /app/build/libs/*.jar TelegramBotCore.jar

ENTRYPOINT ["java", "-jar", "/app/container/TelegramBotCore.jar"]