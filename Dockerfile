FROM maven:3.8.7-openjdk-18-slim as build
WORKDIR /recipetoria-backend
COPY pom.xml .
COPY src ./src
COPY .env .
COPY local.env .
RUN mvn -f pom.xml clean package

FROM openjdk:18-alpine
WORKDIR /JuniorITClub
COPY --from=build /recipetoria-backend/target/*.jar rec.jar
COPY . .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "rec.jar"]