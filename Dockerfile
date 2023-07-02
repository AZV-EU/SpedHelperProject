FROM openjdk:17-oracle
COPY target/*.jar spedhelper.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","spedhelper.jar"]