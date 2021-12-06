FROM quay.io/quarkus/ubi-quarkus-native-image:21.3.0-java11 AS build

ENV LD_LIBRARY_PATH=/usr/local/lib
ENV OPENSSL_ROOT_DIR=/usr/bin/openssl
ENV OPENSSL_LIBRARIES=/usr/bin/openssl/lib
ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk

USER 0

COPY --chown=quarkus:quarkus gradlew /code/
COPY --chown=quarkus:quarkus /gradle/ /code/gradle/
COPY --chown=quarkus:quarkus build.gradle.kts /code/
COPY --chown=quarkus:quarkus settings.gradle.kts /code/
COPY --chown=quarkus:quarkus gradle.properties /code/
COPY libgroupsig/ /libgroupsig/

RUN microdnf install -y git cmake clang make gcc-c++ glibc-static openssl openssl-devel; \
    cd /libgroupsig && \
    rm -rf build && \
    mkdir build && \
    cd build && \
    cmake .. && \
    make
RUN microdnf install -y java-11-openjdk-devel.x86_64 maven && \
    cd /libgroupsig/src/wrappers/java/jgroupsig && \
    mvn clean package && \
    mkdir /code/libs/ && \
    mv /libgroupsig/src/wrappers/java/jgroupsig/java/target/main/resources/libjnigroupsig.so /usr/local/lib/libjnigroupsig.so && \
    mv /libgroupsig/src/wrappers/java/jgroupsig/java/target/main/resources/libjgroupsig-1.1.0.jar /code/libs/libjgroupsig-1.1.0.jar && \
    ldconfig -v;

USER quarkus

WORKDIR /code
COPY src/ /code/src/

RUN ./gradlew -b /code/build.gradle.kts build -Dquarkus.package.type=native

## Stage 2 : create the docker final image
FROM registry.access.redhat.com/ubi8/ubi-minimal
WORKDIR /work/
COPY --from=build /code/build/*-runner /work/application
RUN chmod 775 /work
EXPOSE 8080
CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]