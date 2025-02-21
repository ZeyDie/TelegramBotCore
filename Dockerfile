FROM openjdk:21-slim AS builder

ENV HOME=/home/telegram-bot
RUN mkdir -p $HOME

COPY gradle $HOME/gradle
COPY libs $HOME/libs
COPY projects $HOME/projects
COPY build.gradle settings.gradle boot.gradle $HOME/

WORKDIR $HOME

RUN gradle build -x test
RUN gradle build -x test

WORKDIR $HOME/build/libs

CMD java -jar TelegramBotCore.jar