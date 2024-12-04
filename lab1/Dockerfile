FROM gradle:8.8-jdk17 AS build

#RUN apt-get update && apt-get install -y curl unzip zip
#RUN curl -s https://get.sdkman.io | bash
#RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install gradle"

WORKDIR /app

COPY --chown=gradle:gradle . ./
RUN chmod +x gradlew
RUN gradle build --no-daemon

#RUN apk add --no-cache --virtual .build-deps curl bash && \
#    curl -s https://services.gradle.org/distributions/gradle-7.6-bin.zip -o gradle.zip && \
#    unzip gradle.zip && \
#    mv gradle-7.6 /opt/gradle && \
#    ln -s /opt/gradle/bin/gradle /usr/local/bin/gradle && \
#    rm gradle.zip && \
#    apk del .build-deps
#
#COPY . .
#
#RUN chmod +x gradlew
#
#RUN ./gradlew build --no-daemon

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/lb1-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]