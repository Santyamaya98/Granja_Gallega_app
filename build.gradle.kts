import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    // 1. CHANGE THIS: Downgrade Kotlin to a stable 1.9.x version
    kotlin("jvm") version "2.1.10" // Changed from "2.0.0-RC1"
    kotlin("plugin.serialization") version "2.1.10" // Make sure serialization plugin matches Kotlin version
    application
}

repositories {
    mavenCentral()
    // Keep other repositories if needed, but mavenCentral should cover most common ones.
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}

dependencies {
    // 2. You can remove explicit kotlin("stdlib") if using kotlin("jvm") plugin, it's usually implied.
    // implementation(kotlin("stdlib"))

    implementation(kotlin("stdlib"))
    implementation("org.slf4j:slf4j-nop:2.0.12")
    implementation("io.ktor:ktor-client-core:3.1.0")
    implementation("io.ktor:ktor-client-cio:3.1.0")
    implementation("io.ktor:ktor-client-content-negotiation:3.1.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.1.0")
    implementation("io.ktor:ktor-client-auth:3.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.json:json:20240303")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.8.1")
}

application {
    mainClass.set("MainKt")
}