FROM openjdk:21-slim AS builder

RUN apt-get update && \
    apt-get install -y gradle

ENV HOME=/home/telegram-bot
RUN mkdir -p $HOME

COPY gradle $HOME/gradle
COPY libs $HOME/libs
COPY projects $HOME/projects
COPY build.gradle settings.gradle boot.gradle $HOME/

WORKDIR $HOME

CMD gradle build -x test
CMD gradle build -x test

WORKDIR $HOME/build/libs

CMD java -jar TelegramBotCore.jar