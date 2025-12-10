import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "3.4.12" apply false
  id("io.spring.dependency-management") version "1.1.7" apply false

  kotlin("jvm") version "2.0.21"
  kotlin("plugin.spring") version "2.0.21" apply false
  kotlin("plugin.jpa") version "2.0.21" apply false
  kotlin("plugin.allopen") version "2.0.21" apply false
}

allprojects {
  group = "com.dhlee"
  version = "0.0.1-SNAPSHOT"

  repositories {
    mavenCentral()
  }
}

subprojects {
  apply(plugin = "org.jetbrains.kotlin.jvm")
  apply(plugin = "org.jetbrains.kotlin.plugin.spring")
  apply(plugin = "io.spring.dependency-management")


  dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.9.0")

    // Jackson for Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Logging
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.0")
  }

  java {
    toolchain {
      languageVersion.set(JavaLanguageVersion.of(21))
    }
  }

  tasks.withType<KotlinCompile> {
    kotlinOptions {
      freeCompilerArgs = listOf("-Xjsr305=strict")
      jvmTarget = "21"
    }
  }

  tasks.withType<Test> {
    useJUnitPlatform()
  }
}