FROM openjdk:17-slim AS builder

RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    curl \
    && curl -sL https://archive.apache.org/dist/maven/maven-3/3.9.0/binaries/apache-maven-3.9.0-bin.tar.gz | tar xz -C /opt \
    && ln -s /opt/apache-maven-3.9.0/bin/mvn /usr/bin/mvn

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app
COPY --from=builder /app/target/transferservice-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
