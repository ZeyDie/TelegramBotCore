FROM openjdk:21-slim AS builder

RUN apt-get update && \
    apt-get install -y gradle

RUN mkdir -p /app

COPY gradle /app/gradle
COPY libs /app/libs
COPY projects /app/projects
COPY build.gradle settings.gradle boot.gradle /app/

WORKDIR /app

CMD gradle build -x test
CMD gradle build -x test

CMD ls && pwd

FROM openjdk:21-slim

WORKDIR /app/container

COPY --from=builder /app/build/libs/*.jar TelegramBotCore.jar

ENTRYPOINT ["java", "-jar", "/app/container/TelegramBotCore.jar"]