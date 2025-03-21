FROM maven:3.8.5-openjdk-17 AS dependencies

WORKDIR /opt/app
COPY shareKernel/pom.xml shareKernel/pom.xml
COPY domain/pom.xml domain/pom.xml
COPY application/pom.xml application/pom.xml
COPY infrastructure/pom.xml infrastructure/pom.xml
COPY presentation/pom.xml presentation/pom.xml
COPY pom.xml .

RUN mvn -B -e org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline -DexcludeArtifactIds=domain,application,infrastructure,presentation,shareKernel

FROM maven:3.8.5-openjdk-17 AS builder

WORKDIR /opt/app
COPY --from=dependencies /root/.m2 /root/.m2
COPY --from=dependencies /opt/app/ /opt/app
COPY shareKernel/src /opt/app/shareKernel/src
COPY domain/src /opt/app/domain/src
COPY application/src /opt/app/application/src
COPY infrastructure/src /opt/app/infrastructure/src
COPY presentation/src /opt/app/presentation/src

RUN mvn -B -e clean install -DskipTests

FROM openjdk:17-slim

WORKDIR /opt/app
COPY --from=builder /opt/app/shareKernel/target/*.jar /shareKernel.jar
COPY --from=builder /opt/app/domain/target/*.jar /domain.jar
COPY --from=builder /opt/app/application/target/*.jar /application.jar
COPY --from=builder /opt/app/infrastructure/target/*.jar /infrastructure.jar
COPY --from=builder /opt/app/presentation/target/*.jar /presentation.jar

COPY start.sh /start.sh
RUN chmod +x /start.sh

EXPOSE 8080

ENTRYPOINT ["/bin/bash", "/start.sh"]