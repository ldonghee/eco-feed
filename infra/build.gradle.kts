plugins {
  id("org.springframework.boot")
  kotlin("jvm")
  kotlin("plugin.spring")
}

dependencies {
  api(project(":core"))

  // WebFlux (WebClient)
  api("org.springframework.boot:spring-boot-starter-webflux")

  // Netty DNS resolver for macOS
  runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.104.Final:osx-aarch_64")

  // Spring Data R2DBC
//  api("org.springframework.boot:spring-boot-starter-data-r2dbc")

  // R2DBC Drivers
//  runtimeOnly("org.postgresql:r2dbc-postgresql")

  // Connection Pool
//  implementation("io.r2dbc:r2dbc-pool")

  // Database
  runtimeOnly("com.h2database:h2")

  // Redis Reactive
  api("org.springframework.boot:spring-boot-starter-data-redis-reactive")

  // Test
  testImplementation("io.projectreactor:reactor-test")
}