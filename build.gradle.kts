plugins {
    kotlin("jvm") version "2.1.0"
    id("com.github.johnrengelman.shadow").version("7.1.2")
    id("io.gitlab.arturbosch.detekt") version ("1.23.8")
}

group = "de.fabilucius"
version = "1.0-SNAPSHOT"

val koinVersion: String by project
val kotlinCoroutinesVersion: String by project
val kotlinVersion: String by project
val spigotVersion: String by project
val mockkVersion: String by project
val kotestVersion: String by project
val junitVersion: String by project
val dataFakerVersion: String by project
val mockBukkitVersion: String by project
val exposedVersion: String by project
val sqliteJdbcVersion: String by project

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("org.spigotmc", "spigot-api", spigotVersion)

    //TODO add to plugin.yml
    implementation("org.xerial", "sqlite-jdbc", sqliteJdbcVersion)
    //TODO add to plugin.yml
    implementation("org.jetbrains.exposed", "exposed-core", exposedVersion)
    implementation("org.jetbrains.exposed", "exposed-jdbc", exposedVersion)
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", kotlinCoroutinesVersion)
    implementation("org.jetbrains.kotlin", "kotlin-reflect", kotlinVersion)
    implementation("io.insert-koin", "koin-core", koinVersion)

    detekt("io.gitlab.arturbosch.detekt", "detekt-formatting", "1.23.8")
    detekt("io.gitlab.arturbosch.detekt", "detekt-cli", "1.23.8")

    testImplementation(kotlin("test"))
    testImplementation("org.mockbukkit.mockbukkit", "mockbukkit-v1.21", mockBukkitVersion)
    testImplementation("io.mockk", "mockk", mockkVersion)
    testImplementation("io.kotest", "kotest-assertions-core-jvm", kotestVersion)
    testImplementation("org.junit.jupiter", "junit-jupiter", junitVersion)
    testImplementation("net.datafaker", "datafaker", dataFakerVersion) {
        //To resolve classpath dependency problems because spigot uses an older version which gets overwritten by datafaker
        exclude(group = "org.yaml", module = "snakeyaml")
    }
}

kotlin {
    jvmToolchain(21)
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