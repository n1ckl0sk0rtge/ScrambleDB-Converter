plugins {
    java
    id("io.quarkus")
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))

    implementation("io.quarkus:quarkus-arc:2.5.0.Final")

    implementation("io.quarkus:quarkus-resteasy:2.5.0.Final")
    implementation("io.quarkus:quarkus-resteasy-mutiny:2.5.0.Final")
    implementation("io.quarkus:quarkus-resteasy-jackson:2.5.0.Final")
    implementation("io.quarkus:quarkus-smallrye-openapi:2.5.0.Final")

    implementation("io.quarkus:quarkus-reactive-mysql-client:2.5.0.Final")
    implementation("io.quarkus:quarkus-redis-client:2.4.2.Final")

    implementation(files("libs/libjgroupsig-1.1.0.jar"))
    implementation(files("usr/local/lib/libjnigroupsig.so"))

    testImplementation("io.quarkus:quarkus-junit5:2.5.0.Final")
    testImplementation("io.rest-assured:rest-assured:4.4.0")
}

group = "com.ibm.converter"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}
