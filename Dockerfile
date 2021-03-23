FROM adoptopenjdk/openjdk8:jdk8u272-b10 as builder

COPY --chown=root:root . /src
WORKDIR /src
RUN ./gradlew jar --no-daemon

FROM openjdk:8-jre-slim
EXPOSE 8080
COPY --from=builder /src/build/libs/taxi-booking-1.0.jar /app/
WORKDIR /app
CMD java -jar taxi-booking-1.0.jar
