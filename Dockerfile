FROM openjdk:17-jdk-slim-buster
WORKDIR /app
COPY /build/libs/middle-0.0.1-SNAPSHOT.jar /app/middle.jar
ENTRYPOINT ["sh", "-c", "jav -jar middle.jar --BACKEND_URL=${BACKEND_URL}"]