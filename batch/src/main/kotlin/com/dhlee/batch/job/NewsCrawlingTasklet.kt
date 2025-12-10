package com.dhlee.batch.job

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.runBlocking
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.stereotype.Component
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.StepContribution
import org.springframework.batch.repeat.RepeatStatus
import java.time.Duration
import java.time.LocalDateTime

@Component
class NewsCrawlingTasklet(
  private val crawler: YonhapNewsCrawler
) : Tasklet {
  private val logger = KotlinLogging.logger {}

  override fun execute(
    contribution: StepContribution,
    chunkContext: ChunkContext
  ): RepeatStatus = runBlocking {

    val startTime = LocalDateTime.now()
    logger.info { "🚀 뉴스 크롤링 Tasklet 시작" }

    try {
      // 크롤링 실행
      val articles = crawler.crawlEconomyNews()

      val endTime = LocalDateTime.now()
      val duration = Duration.between(startTime, endTime)

      // 결과 저장
      val ctx = chunkContext.stepContext.stepExecution.executionContext
      ctx.put("totalCount", articles.size)
      ctx.put("startTime", startTime.toString())
      ctx.put("endTime", endTime.toString())
      ctx.put("durationSeconds", duration.seconds)

      // 최종 로그
      logger.info {
        """
                
                ╔════════════════════════════════════════════════════════╗
                ║            크롤링 완료                                  ║
                ╠════════════════════════════════════════════════════════╣
                ║  총 기사 수: ${articles.size}개
                ║  시작 시간: $startTime
                ║  종료 시간: $endTime
                ║  소요 시간: ${duration.seconds}초
                ╚════════════════════════════════════════════════════════╝
                
                """.trimIndent()
      }

      RepeatStatus.FINISHED
    } catch (e: Exception) {
      logger.error(e) { "크롤링 Tasklet 실행 실패" }
      throw e
    }
  }
}