val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val kGraphqlVersion: String by project
val koinVersion: String by project
val kMongoVersion: String by project
val bCryptVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.5.10"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

tasks.withType<Jar> {
    manifest {
        attributes(
            mapOf(
                "Main-Class" to application.mainClass
            )
        )
    }
}

group = "com.example"
version = "0.0.1"

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

dependencies {
    implementation("org.koin:koin-ktor:${koinVersion}")
    implementation("org.litote.kmongo:kmongo:$kMongoVersion")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.0")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("com.apurebase:kgraphql:${kGraphqlVersion}")
    implementation("com.apurebase:kgraphql-ktor:$kGraphqlVersion")
    implementation("io.ktor:ktor-auth:$ktorVersion")
    implementation("io.ktor:ktor-auth-jwt:$ktorVersion")
    implementation("at.favre.lib:bcrypt:$bCryptVersion")
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
}

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("testresources")