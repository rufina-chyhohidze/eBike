# Stage 1: Build your app (optional if you build outside docker)
FROM gradle:jdk21 AS javaapp-builder
LABEL author="Team 18 - Integration 4"
WORKDIR /team18/project
COPY ./ /team18/project
WORKDIR /team18/project
RUN gradle wrapper --gradle-version 8.13
RUN ./gradlew bootJar -x test

## Stage 2: Run the app
FROM eclipse-temurin:21-jre-alpine
LABEL author="Team 18 - Integration 4"
# Copy jar from build stage
COPY --from=javaapp-builder /team18/project/build/libs/Team18-Integration4-Application.jar /team18/project/build/libs/
COPY --from=javaapp-builder /team18/project/.env /team18/project/build/libs/
WORKDIR /team18/project/build/libs/
EXPOSE 8080
ENTRYPOINT ["java","-jar","Team18-Integration4-Application.jar"]