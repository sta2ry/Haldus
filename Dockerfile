FROM gradle:9.6.1-jdk25-alpine AS builder
WORKDIR /workspace
COPY . .
RUN gradle :haldus-vert:installDist --no-daemon -x test

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=builder /workspace/haldus-vert/build/install/haldus-vert .
EXPOSE 8080
ENTRYPOINT ["./bin/haldus-vert"]
