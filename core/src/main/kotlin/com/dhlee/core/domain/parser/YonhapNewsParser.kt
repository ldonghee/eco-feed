package com.dhlee.core.domain.parser

import com.dhlee.core.domain.article.NewsArticle
import io.github.oshai.kotlinlogging.KotlinLogging
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class YonhapNewsParser {
  private val logger = KotlinLogging.logger {}
  private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

  /**
   * 목록 페이지에서 기사 URL 추출
   */
  fun parseArticleLinks(html: String): List<String> {
    return try {
      val doc = Jsoup.parse(html)

      // 연합뉴스 경제 섹션의 기사 링크 선택자
      val links = doc.select("div.list-type038 ul li a.link-news, div.section01 ul li a")
        .mapNotNull { element ->
          val href = element.attr("href")
          when {
            href.startsWith("http") -> href
            href.startsWith("/") -> "https://www.yna.co.kr$href"
            else -> null
          }
        }
        .filter { it.contains("/view/") }
        .distinct()

      logger.debug { "파싱된 링크 수: ${links.size}" }
      links
    } catch (e: Exception) {
      logger.error(e) { "기사 링크 파싱 실패" }
      emptyList()
    }
  }

  /**
   * 기사 상세 페이지 파싱
   */
  fun parseArticleDetail(url: String, html: String): NewsArticle? {
    return try {
      val doc = Jsoup.parse(html)

      // 제목
      val title = doc.select("h1.tit, h1.article-tit").text()
      if (title.isBlank()) {
        logger.warn { "제목 없음: $url" }
        return null
      }

      // 본문
      val content = doc.select("article.story-news, div.article-txt")
        .text()
        .takeIf { it.isNotBlank() }

      // 발행 시간
      val publishedAt = parsePublishDate(doc)

      // 썸네일
      val thumbnailUrl = doc.select("figure.img-caption img, div.img-wrap img")
        .firstOrNull()
        ?.attr("src")
        ?.let { if (it.startsWith("http")) it else "https:$it" }

      // 기자명
      val author = doc.select("em.reporter, span.writer").text()
        .takeIf { it.isNotBlank() }

      NewsArticle(
        url = url,
        title = title,
        content = content,
        publishedAt = publishedAt,
        thumbnailUrl = thumbnailUrl,
        author = author
      ).also {
        logger.debug { "파싱 완료: $title" }
      }
    } catch (e: Exception) {
      logger.error(e) { "기사 파싱 실패: $url" }
      null
    }
  }

  private fun parsePublishDate(doc: Document): LocalDateTime? {
    return try {
      val dateText = doc.select("p.date-time, span.txt-time")
        .text()
        .replace(Regex("송고시간|입력|수정"), "")
        .trim()

      if (dateText.isBlank()) return null

      LocalDateTime.parse(dateText, dateFormatter)
    } catch (e: Exception) {
      logger.warn { "날짜 파싱 실패: ${e.message}" }
      null
    }
  }
}