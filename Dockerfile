FROM maven:3-jdk-11 as tg2smtp-builder
WORKDIR      /usr/src/tg2smtp

COPY pom.xml /usr/src/tg2smtp
RUN mvn -B -e -C -q dependency:go-offline

COPY .       .
RUN mvn -B -e -q package

FROM openjdk:11-jre-slim as tg2smtp-assembler
WORKDIR                                /opt/tg2smtp

ARG JAR_FILE=tg2smtp-0.4.0-SNAPSHOT.jar
COPY --from=tg2smtp-builder \
     /usr/src/tg2smtp/target/$JAR_FILE /opt/tg2smtp/app.jar

CMD ["/usr/local/openjdk-11/bin/java", "-Djava.security.egd=file:/dev/./urandom", "-jar" ,"/opt/tg2smtp/app.jar"]
