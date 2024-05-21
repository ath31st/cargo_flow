#FROM maven:3.8.5-openjdk-17 AS build
#
#WORKDIR /app
#
#COPY pom.xml ./
#COPY ../pom.xml ../
#COPY src ./src
#
#RUN mvn clean package -DskipTests

FROM bellsoft/liberica-openjdk-alpine:17-cds

RUN apk add dumb-init && \
    rm -rf /var/cache/apk/*

RUN mkdir /app
RUN addgroup --system javauser && adduser -S -s /bin/false -G javauser javauser

WORKDIR /app

RUN chown -R javauser:javauser /app

USER javauser

#COPY --from=build /app/target/discovery-0.1.0.jar ./discovery_service.jar
COPY ./target/discovery-*.jar ./discovery_service.jar
EXPOSE 8761

ENTRYPOINT ["dumb-init", "java", "-jar", "discovery_service.jar"]
