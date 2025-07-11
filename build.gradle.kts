plugins {
    kotlin("jvm") version "2.1.0"
    id("com.github.johnrengelman.shadow").version("7.1.2")
    id("io.gitlab.arturbosch.detekt") version("1.23.8")
}

group = "de.fabilucius"
version = "1.0-SNAPSHOT"

val koinVersion: String by project
val kotlinVersion: String by project
val spigotVersion: String by project

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    implementation("org.jetbrains.kotlin", "kotlin-reflect", kotlinVersion)
    implementation("org.spigotmc", "spigot-api", spigotVersion)
    implementation("io.insert-koin", "koin-core", koinVersion)

    detekt("io.gitlab.arturbosch.detekt", "detekt-formatting", "1.23.8")
    detekt("io.gitlab.arturbosch.detekt", "detekt-cli", "1.23.8")

    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(17)
}

detekt {
    toolVersion = "1.23.8"
    config.setFrom(project.files("config/detekt/detekt.yml"))
}

tasks {
    test {
        useJUnitPlatform()
    }

    processResources {
        filesMatching("**/plugin.yml") {
            expand(project.properties)
        }
    }
}