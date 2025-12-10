plugins {
  id("org.springframework.boot")
  kotlin("jvm")
  kotlin("plugin.spring")
}

dependencies {
  // Validation
  api("jakarta.validation:jakarta.validation-api")

  // Web Scraping
  implementation("org.jsoup:jsoup:1.18.1")
}