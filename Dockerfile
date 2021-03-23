FROM adoptopenjdk/openjdk8:jdk8u272-b10 as builder

COPY --chown=root:root . /src
WORKDIR /src
RUN ./gradlew build --no-daemon
RUN ls -la /src/build/libs

FROM openjdk:8-jre-slim
EXPOSE 8080
ARG VERSION
ARG NAME
COPY --from=builder /src/build/libs/${NAME}-${VERSION}.jar /app/
WORKDIR /app
ENV VERSION ${VERSION}
ENV NAME ${NAME}
CMD java -jar ${NAME}-${VERSION}.jar
