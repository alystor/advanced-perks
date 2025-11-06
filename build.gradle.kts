import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    kotlin("jvm") version "2.1.0"
    id("io.gitlab.arturbosch.detekt") version ("1.23.8")
}

group = "de.fabilucius"
version = project.version

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
val postgresqlJdbcVersion: String by project
val h2JdbcVersion: String by project
val reflectionsVersion: String by project
val jacksonVersion: String by project
val vaultVersion: String by project
val luckPermsVersion: String by project
val archunitVersion: String by project
val xSeriesVersion: String by project
val placeholderApiVersion: String by project
val classGraphVersion: String by project

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.extendedclip.com/releases")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("org.spigotmc", "spigot-api", spigotVersion)
    compileOnly("com.github.MilkBowl", "VaultAPI", vaultVersion)
    compileOnly("net.luckperms", "api", luckPermsVersion)
    compileOnly("me.clip", "placeholderapi", placeholderApiVersion)

    runtimeOnly("org.xerial", "sqlite-jdbc", sqliteJdbcVersion)
    runtimeOnly("org.postgresql", "postgresql", postgresqlJdbcVersion)
    implementation("org.jetbrains.exposed", "exposed-core", exposedVersion)
    implementation("org.jetbrains.exposed", "exposed-jdbc", exposedVersion)
    implementation("org.jetbrains.exposed", "exposed-dao", exposedVersion)
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", kotlinCoroutinesVersion)
    implementation("org.jetbrains.kotlin", "kotlin-reflect", kotlinVersion)
    implementation("io.insert-koin", "koin-core", koinVersion)
    implementation("com.fasterxml.jackson.core", "jackson-databind", jacksonVersion)
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin", jacksonVersion)
    implementation("com.github.cryptomorin", "XSeries", xSeriesVersion)

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
    testImplementation("com.h2database", "h2", h2JdbcVersion)
    testImplementation("org.reflections", "reflections", reflectionsVersion)
    testImplementation("com.tngtech.archunit", "archunit-junit5", archunitVersion)
    testImplementation("io.github.classgraph", "classgraph", classGraphVersion)
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
        testLogging {
            events("passed", "skipped", "failed")
            showStandardStreams = true
            showStackTraces = true
            exceptionFormat = TestExceptionFormat.FULL
        }
    }

    processResources {
        filesMatching("**/plugin.yml") {
            expand(project.properties)
        }
    }
}