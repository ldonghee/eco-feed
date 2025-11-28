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