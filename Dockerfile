FROM openjdk:8-jdk-alpine

RUN apk add --no-cache bash
RUN apk add --no-cache vim

VOLUME /tmp
ADD target/user.jar app.jar

ENV JAVA_OPTS=""

ENV SPRING_ACTIVE_PROFILE="prod"

ENV DATABASE_HOST="192.168.1.18"
ENV DATABASE_USER="root"
ENV DATABASE_PASSWORD="root"
ENV DATABASE_PORT="3306"
ENV DATABASE_NAME="sharingcraftsmanuser"

EXPOSE 8080

ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar

