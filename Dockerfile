FROM java:8
EXPOSE 8080
ADD target/member-service-0.0.1-SNAPSHOT.jar member-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "-Djava.security.egd=file:/dev/./urandom", "member-service-0.0.1-SNAPSHOT.jar"]