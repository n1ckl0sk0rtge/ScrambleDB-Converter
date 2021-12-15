FROM quay.io/quarkus/ubi-quarkus-native-image:21.3.0-java11 AS build
COPY --chown=quarkus:quarkus gradlew /code/
COPY --chown=quarkus:quarkus /gradle/ /code/gradle/
COPY --chown=quarkus:quarkus build.gradle.kts /code/
COPY --chown=quarkus:quarkus settings.gradle.kts /code/
COPY --chown=quarkus:quarkus gradle.properties /code/
USER quarkus
WORKDIR /code
COPY src/ /code/src/
COPY libs/ /code/libs/
RUN ./gradlew -b /code/build.gradle.kts build -Dquarkus.package.type=native

FROM registry.access.redhat.com/ubi8/ubi-minimal:8.5 as nativebuilder	
RUN mkdir -p /tmp/ssl \
&& cp /usr/lib64/libstdc++.so.6.0.25 /tmp/ssl/libstdc++.so.6 \
&& cp /usr/lib64/libgcc_s-8-20210514.so.1 /tmp/ssl/libgcc_s.so.1 \
&& cp /usr/lib64/libz.so.1 /tmp/ssl/libz.so.1

## Stage 2 : create the docker final image
FROM gcr.io/distroless/base
ENV LD_LIBRARY_PATH /
COPY --from=nativebuilder /tmp/ssl/ /
COPY --from=build /code/build/*-runner /application
EXPOSE 8080
CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]