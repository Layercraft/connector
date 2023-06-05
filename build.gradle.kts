import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    kotlin("jvm") version "1.8.20"
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "io.layercraft.connector"
version = "1.0-SNAPSHOT"


val vertxVersion = "4.4.2"
val junitJupiterVersion = "5.10.0-M1"

val mainName = "io.layercraft.connector.ConnectorKt"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.layercraft:packetlib:0.0.40")

    implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))

    implementation("io.vertx:vertx-core")
    implementation("io.vertx:vertx-config")
    implementation("io.vertx:vertx-rabbitmq-client")
    implementation("io.netty", "netty-transport-native-epoll", classifier = "linux-x86_64")
    implementation("io.netty", "netty-transport-native-epoll", classifier = "linux-aarch_64")
    implementation("io.netty", "netty-transport-native-kqueue", classifier = "osx-x86_64")
    implementation("io.netty", "netty-transport-native-kqueue", classifier = "osx-aarch_64")

    // Kotlin
    implementation("io.vertx:vertx-lang-kotlin")
    implementation("io.vertx:vertx-lang-kotlin-coroutines")

    // Tests
    testImplementation("io.vertx:vertx-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")

    // MacOS
    runtimeOnly("io.netty", "netty-resolver-dns-native-macos", classifier = "osx-aarch_64")
}

application {
    mainClass.set(mainName)
}

kotlin {
    jvmToolchain(17)
    // Use k2
    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }
}

tasks.withType<ShadowJar> {
    archiveClassifier.set("fat")
    mergeServiceFiles()
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events = setOf(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
    }
}
