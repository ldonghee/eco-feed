package com.dhlee.infra.config

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import java.time.Duration
import java.util.concurrent.TimeUnit


@Configuration
class WebClientConfig {
  companion object {
    private const val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
  }

  @Bean
  fun connectionProvider(): ConnectionProvider {

    /**
     * ConnectionProvider 설정
     * - 연결 풀 관리
     */
    return ConnectionProvider.builder("eco-feed-pool")
      .maxConnections(100) // 최대 동시 연결 수
      .maxIdleTime(Duration.ofSeconds(20)) // 유휴 연결 최대 시간 (20초 동안 재사용이 되지 않으면 자동으로 연결 종료 / 연결 사용 완료 시, 풀에 반환)
      .maxLifeTime(Duration.ofSeconds(60)) // 연결 최대 수명 (60초 후 연결 종료(사용중이여도) / 사용 -> 반환 -> 재사용 -> 반환(반복)
      .pendingAcquireTimeout(Duration.ofSeconds(60)) // 연결 획득 대기 시간 (60초 동안 연결 획득 대기, 초과 시 예외 발생(PoolAcquireTimeoutException))
      .evictInBackground(Duration.ofSeconds(120)) // 백그라운드 유휴 연결 검사 주기 (120초 마다 유휴 연결 검사 및 종료), 좀비 연결 방지
      .build()
  }

  /**
   * HttpClient HTTP 통신 설정
   */
  @Bean
  fun httpClient(connectionProvider: ConnectionProvider): HttpClient {
    return HttpClient.create(connectionProvider)
      .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000) // 연결 타임아웃 10초
      .responseTimeout(Duration.ofSeconds(30))   // 응답 타임아웃 30초
      .doOnConnected { conn ->
        conn.addHandlerLast(ReadTimeoutHandler(30, TimeUnit.SECONDS)) // 읽기 타임아웃 30초, 서버가 데이터를 30초 동안 안 보내면 타임아웃
          .addHandlerLast(WriteTimeoutHandler(30, TimeUnit.SECONDS))  // 쓰기 타임아웃 30초, 클라이언트가 서버에 데이터를 30초 동안 못 보내면 타임아웃
      }
  }

  /**
   * Webclient 최종 HTTP 클라이언트 설정
   */
  @Bean
  fun webClient(httpClient: HttpClient): WebClient {
    /**
     * ExchangeStrategies 설정 - 메모리 버퍼 크기 조정
     * 1. 응답 수신 시작
     * 2. 메모리 버퍼에 누적
     * 3. 10MB 초과? → DataBufferLimitException 예외 발생
     */
    val exchangeStrategies = ExchangeStrategies.builder()
      .codecs { configurer ->
        configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024) // 10MB
      }
      .build()

    return WebClient.builder()
      .clientConnector(ReactorClientHttpConnector(httpClient)) // HttpClient 연결 설정
      .exchangeStrategies(exchangeStrategies) // 메모리 버퍼 크기 설정
      .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
      .defaultHeader(HttpHeaders.ACCEPT, MediaType.TEXT_HTML_VALUE)
      .defaultHeader(HttpHeaders.ACCEPT_LANGUAGE, "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
      .build()
  }
}