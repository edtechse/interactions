#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package
RUN mvn -f /home/app/pom.xml verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=Interaction-api -Dsonar.login=02e22fd21c2fa9204657ba74bbd43f8b03e9ae88
#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/interactions-0.0.1-SNAPSHOT.jar /usr/local/lib/interactions.jar
EXPOSE 8888
ENTRYPOINT ["java","-jar","/usr/local/lib/interactions.jar"]
