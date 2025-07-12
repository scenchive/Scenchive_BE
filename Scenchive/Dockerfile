# 1단계: Build Stage
FROM gradle:8.2.1-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/app
WORKDIR /home/gradle/app
RUN gradle build -x test

# 2단계: Run Stage
FROM openjdk:17-jdk-slim

ARG DB_URL
ARG DB_USERNAME
ARG DB_PASSWORD
ARG SMTP_PASSWORD
ARG AWS_S3_ACCESS_KEY
ARG AWS_S3_SECRET_KEY

ENV DB_URL=$DB_URL \
    DB_USERNAME=$DB_USERNAME \
    DB_PASSWORD=$DB_PASSWORD \
    SMTP_PASSWORD=$SMTP_PASSWORD \
    AWS_S3_ACCESS_KEY=$AWS_S3_ACCESS_KEY \
    AWS_S3_SECRET_KEY=$AWS_S3_SECRET_KEY

COPY --from=build /home/gradle/app/build/libs/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
