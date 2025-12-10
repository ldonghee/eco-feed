package com.dhlee.core.domain.article

import java.time.LocalDateTime

data class NewsArticle(
  val url: String,
  val title: String,
  val content: String?,
  val publishedAt: LocalDateTime?,
  val thumbnailUrl: String?,
  val author: String?
)