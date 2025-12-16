package com.dhlee.batch.job

import com.dhlee.core.domain.article.NewsArticle
import com.dhlee.core.domain.parser.YonhapNewsParser
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitBodyOrNull

@Component
class YonhapNewsCrawler(
  private val webClient: WebClient,
  private val parser: YonhapNewsParser
) {
  private val log = KotlinLogging.logger {}

  companion object {
    private const val BASE_URL = "https://www.yna.co.kr"
    private const val ECONOMY_URL = "$BASE_URL/economy/all"
    private const val MAX_PAGES = 3
    private const val MAX_CONCURRENT = 10
    private const val REQUEST_DELAY_MS = 100L
  }

  /**
   * 연합뉴스 경제 섹션 크롤링
   */
  suspend fun getEconomyNews(): List<NewsArticle> = coroutineScope {
    log.info { "=== 연합뉴스 경제 크롤링 시작 ===" }

    try {
      // 1단계: 기사 URL 수집
      val articleUrls = collectArticleUrls()
      log.info { "📋 수집된 기사 URL: ${articleUrls.size}개" }

      if (articleUrls.isEmpty()) {
        log.warn { "수집된 URL이 없습니다" }
        return@coroutineScope emptyList()
      }

      // 2단계: 각 기사 크롤링 (병렬 처리)
      val articles = articleUrls.asFlow()
        .buffer(MAX_CONCURRENT) // 동시 처리 제한
        .map { url ->
          delay(REQUEST_DELAY_MS) // 서버 부하 방지
          crawlArticleDetail(url)
        }
        .filterNotNull()
        .toList()

      log.info { "✅ 크롤링 완료: ${articles.size}개 기사" }

      // 3단계: 결과 로깅
      logCrawlingResults(articles)

      articles
    } catch (e: Exception) {
      log.error(e) { "크롤링 중 오류 발생" }
      emptyList()
    }
  }

  /**
   * 목록 페이지에서 기사 URL 수집
   */
  private suspend fun collectArticleUrls(): Set<String> {
    val urls = mutableSetOf<String>()

    repeat(MAX_PAGES) { page ->
      try {
        val pageUrl = if (page == 0) ECONOMY_URL else "$ECONOMY_URL/$page"

        log.info { "📄 목록 페이지 ${page + 1} 크롤링: $pageUrl" }

        val html = webClient.get()
          .uri(pageUrl)
          .retrieve()
          .awaitBodyOrNull<String>()

        if (html != null) {
          val pageUrls = parser.parseArticleLinks(html)
          urls.addAll(pageUrls)
          log.info { "   ➜ 발견된 링크: ${pageUrls.size}개" }
        }

        delay(REQUEST_DELAY_MS)
      } catch (e: Exception) {
        log.error(e) { "페이지 ${page + 1} 수집 실패" }
      }
    }

    return urls
  }

  /**
   * 기사 상세 페이지 크롤링
   */
  private suspend fun crawlArticleDetail(url: String): NewsArticle? {
    return try {
      log.debug { "📰 기사 크롤링: $url" }

      val html = webClient.get()
        .uri(url)
        .retrieve()
        .awaitBody<String>()

      parser.parseArticleDetail(url, html)?.also {
        log.debug { "   ✅ 파싱 완료: ${it.title}" }
      }
    } catch (e: Exception) {
      log.error(e) { "기사 크롤링 실패: $url" }
      null
    }
  }

  /**
   * 크롤링 결과 로깅
   */
  private fun logCrawlingResults(articles: List<NewsArticle>) {
    log.info { "\n" + "=".repeat(80) }
    log.info { "📊 크롤링 결과 요약" }
    log.info { "=".repeat(80) }
    log.info { "총 기사 수: ${articles.size}개" }
    log.info { "" }

    articles.forEachIndexed { index, article ->
      log.info {
        """
                [${index + 1}] ${article.title}
                   URL: ${article.url}
                   발행: ${article.publishedAt ?: "알 수 없음"}
                   기자: ${article.author ?: "알 수 없음"}
                   썸네일: ${article.thumbnailUrl ?: "없음"}
                   본문 길이: ${article.content?.length ?: 0} 자
                """.trimIndent()
      }
      log.info { "-".repeat(80) }
    }

    log.info { "=".repeat(80) }
  }
}