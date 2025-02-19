FROM gradle:8.12-jdk21 AS builder

WORKDIR /app

COPY gradle ./gradle
COPY libs ./libs
COPY projects ./projects
COPY build.gradle settings.gradle boot.gradle ./

RUN gradle build -x test