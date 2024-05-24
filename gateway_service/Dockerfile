FROM bellsoft/liberica-openjdk-alpine:17-cds

RUN apk add dumb-init && \
    rm -rf /var/cache/apk/*

RUN mkdir /app
RUN addgroup --system javauser && adduser -S -s /bin/false -G javauser javauser

WORKDIR /app

RUN chown -R javauser:javauser /app

USER javauser

COPY ./target/gateway-*.jar ./gateway_service.jar
EXPOSE 8090

ENTRYPOINT ["dumb-init", "java", "-jar", "gateway_service.jar"]
