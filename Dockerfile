FROM gradle:8.7-jdk21-alpine AS build
WORKDIR /home/gradle/src/
COPY --chown=gradle:gradle . .
RUN gradle build -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/personal-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "personal-0.0.1-SNAPSHOT.jar"]
