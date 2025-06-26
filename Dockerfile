FROM gradle:8.14.2-jdk17 AS build
ENV APP_HOME=/apps
WORKDIR $APP_HOME
COPY build.gradle settings.gradle gradlew $APP_HOME
COPY gradle $APP_HOME/gradle
COPY src $APP_HOME/src

RUN chmod +x gradlew
RUN ./gradlew clean build -x test


FROM amazoncorretto:17.0.15

ENV APP_HOME=/apps
ARG ARTIFACT_NAME=app.jar
ARG JAR_FILE_PATH=build/libs/BitO-1.0.1.jar

WORKDIR $APP_HOME

COPY --from=build $APP_HOME/$JAR_FILE_PATH $ARTIFACT_NAME

EXPOSE 8080

ENTRYPOINT ["java", "-Xms512m", "-Xmx1024m", "-jar", "app.jar", "--spring.profiles.active=prod"]
