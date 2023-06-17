FROM maven:3.9.2-eclipse-temurin-17
WORKDIR /app
COPY warehouse .
RUN mvn clean install -DskipUiTests -DskipTests

WORKDIR /app/springboot/server

# Download dependencies
RUN mvn spring-boot:start
RUN mvn spring-boot:stop

CMD [ "mvn", "spring-boot:run" ]
EXPOSE 8080
