import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "me.alvin0319.proxyban"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.waterdog.dev/artifactory/main")
    }
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("dev.waterdog.waterdogpe:waterdog:1.1.3-SNAPSHOT")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

tasks {
    shadowJar {
        dependencies {
            exclude(dependency("dev.waterdog.waterdogpe:waterdog:1.1.3-SNAPSHOT"))
        }
        archiveClassifier.set("")
    }
}
