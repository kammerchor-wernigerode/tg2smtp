FROM maven:3-jdk-11 as maven
RUN mkdir -p  /usr/src/app
WORKDIR       /usr/src/app

COPY pom.xml /usr/src/app
RUN mvn -B -q dependency:go-offline

COPY src     /usr/src/app/src
# Due to missing depencencies, mvn does not run with -o (offline)
RUN mvn -q package

FROM openjdk:11-jre-slim

RUN mkdir -p /app
WORKDIR      /app

ARG JAR_FILE=tg2smtp-0.2.0.jar
COPY --from=maven /usr/src/app/target/$JAR_FILE /app/application.jar

CMD ["/usr/local/openjdk-11/bin/java", "-Djava.security.egd=file:/dev/./urandom", "-jar" ,"/app/application.jar"]
