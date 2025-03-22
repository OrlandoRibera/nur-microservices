FROM maven:3.8.5-openjdk-17 AS builder

WORKDIR /opt/app

COPY . . 
RUN ls -l 
RUN mvn install package -DskipTests -Dmaven.test.skip 
RUN ls -lt presentation/target/ 

FROM openjdk:17-slim 

WORKDIR /opt/app 
COPY --from=builder /opt/app/presentation/target/*.jar /opt/app/ 

EXPOSE 8080 

ENTRYPOINT ["sh","-c", "java -jar /opt/app/presentation-0.0.1-SNAPSHOT.jar"]
