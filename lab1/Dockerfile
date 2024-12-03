FROM openjdk:17-jdk-slim AS build

RUN apt-get update && apt-get install -y curl unzip zip
RUN curl -s https://get.sdkman.io | bash
RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install gradle"

WORKDIR /app

COPY . .

RUN chmod +x gradlew

RUN ./gradlew build --no-daemon

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/lb1-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]