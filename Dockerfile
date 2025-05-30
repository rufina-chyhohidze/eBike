# Stage 1: Build your app (optional if you build outside docker)
FROM timbru31/java-node:21-alpine-jdk-22 AS javaapp-builder
LABEL author="Team 18 - Integration 4"
WORKDIR /team18/project
COPY ./ /team18/project
WORKDIR /team18/project
#RUN gradle wrapper --gradle-version 8.13
RUN ./gradlew bootJar -x test

## Stage 2: Run the app
FROM timbru31/java-node:21-alpine-jdk-22
LABEL author="Team 18 - Integration 4"
# Copy jar from build stage
COPY --from=javaapp-builder /team18/project/build/libs/Team18-Integration4-Application.jar /team18/project/build/libs/
WORKDIR /team18/project/build/libs/
ENV WORKBENCH_API_KEY='b9f55fa7-279f-4314-b781-e319b385c463'
ENV MAIL_USERNAME='team18int4@gmail.com'
ENV MAIL_PASSWORD='rfxchlbkjwoazmjc'
EXPOSE 8080
ENTRYPOINT ["java","-jar","Team18-Integration4-Application.jar"]