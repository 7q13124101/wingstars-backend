FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests -pl app -am

FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=build /app/app/target/app-*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]