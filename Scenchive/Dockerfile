# 1단계: Build Stage
FROM gradle:8.2.1-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/app
WORKDIR /home/gradle/app
RUN gradle build -x test

# 2단계: Run Stage
FROM openjdk:17-jdk-slim
COPY --from=build /home/gradle/app/build/libs/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
