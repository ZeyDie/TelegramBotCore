FROM openjdk:21-slim AS builder

RUN apt-get update && \
    apt-get install -y gradle

COPY ./gradle /home/source/gradle
COPY ./libs /home/source/libs
COPY ./projects /home/source/projects
COPY ./gradlew ./gradlew.bat  /home/source/
COPY ./build.gradle ./settings.gradle ./boot.gradle /home/source/

WORKDIR /home/source

RUN chmod +x ./gradlew
RUN ./gradlew build -x test

FROM openjdk:21-slim

COPY --from=builder /home/source/build/libs/*.jar /app.jar

RUN useradd -d /home/container -m container

USER container
ENV USER=container HOME=/home/container

COPY ./entrypoint.sh /entrypoint.sh

CMD ["/bin/bash", "/entrypoint.sh"]