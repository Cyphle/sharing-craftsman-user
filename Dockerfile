FROM openjdk:8-jdk-alpine

VOLUME /tmp
ADD target/user-1.0-SNAPSHOT.jar app.jar

ENV JAVA_OPTS=""
ENV MYSQL_USER="root"
ENV MYSQL_PASSWORD="root"

EXPOSE 8080

ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar

