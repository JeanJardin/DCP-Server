FROM maven AS build

WORKDIR /app/demo

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/demo/target/demo*.jar app.jar
COPY --from=build /app/demo/.env .env

ENTRYPOINT ["java","-jar","app.jar"]
