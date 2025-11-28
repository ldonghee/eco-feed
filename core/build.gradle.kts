plugins {
  kotlin("plugin.jpa")
  kotlin("plugin.allopen")
}

dependencies {
  // Kotlin
  implementation(kotlin("stdlib"))
  implementation(kotlin("reflect"))

  // JPA
  api("jakarta.persistence:jakarta.persistence-api:3.1.0")

  // Validation
  api("jakarta.validation:jakarta.validation-api:3.0.2")
}

// JPA Entity를 위한 설정
allOpen {
  annotation("jakarta.persistence.Entity")
  annotation("jakarta.persistence.MappedSuperclass")
  annotation("jakarta.persistence.Embeddable")
}