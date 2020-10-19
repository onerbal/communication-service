FROM anapsix/alpine-java:8_server-jre_unlimited

FROM openjdk:8-jdk-alpine

ENV PROFILE=dev

ADD ./target/*.jar app.jar

CMD ["java", "-Dspring.profiles.active=${PROFILE}", "-Xmx1024m", "-Duser.timezone=UTC","-agentlib:jdwp=transport=dt_socket,address=5005,server=y,suspend=n","-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

EXPOSE 8080