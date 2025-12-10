plugins {
  id("org.springframework.boot") apply false
}

dependencies {
  api(project(":core"))

  // Spring Data R2DBC
  api("org.springframework.boot:spring-boot-starter-data-r2dbc")

  // R2DBC Drivers
  runtimeOnly("org.postgresql:r2dbc-postgresql")
  runtimeOnly("io.asyncer:r2dbc-mysql:1.2.0")

  // Connection Pool
  implementation("io.r2dbc:r2dbc-pool")

  // Redis Reactive
  api("org.springframework.boot:spring-boot-starter-data-redis-reactive")

  // Flyway for DB Migration (blocking, 초기 마이그레이션용)
  implementation("org.flywaydb:flyway-core")
  implementation("org.flywaydb:flyway-mysql")
  runtimeOnly("com.mysql:mysql-connector-j")

  // Test
  testImplementation("io.projectreactor:reactor-test")
}