package com.dhlee.batch.job

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager


@Configuration
class NewsCrawlingJobConfig(
  private val jobRepository: JobRepository,
  private val transactionManager: PlatformTransactionManager,
  private val newsCrawlingTasklet: NewsCrawlingTasklet
) {
  private val logger = KotlinLogging.logger {}

  @Bean
  fun yonhapNewsCrawlingJob(): Job {
    return JobBuilder("yonhapNewsCrawlingJob", jobRepository)
      .incrementer(RunIdIncrementer())
      .start(crawlYonhapNewsStep())
      .listener(object : org.springframework.batch.core.JobExecutionListener {
        override fun beforeJob(jobExecution: org.springframework.batch.core.JobExecution) {
          logger.info { "🎬 Job 시작: ${jobExecution.jobInstance.jobName}" }
        }

        override fun afterJob(jobExecution: org.springframework.batch.core.JobExecution) {
          logger.info { "🏁 Job 종료: ${jobExecution.status}" }
        }
      })
      .build()
  }

  @Bean
  fun crawlYonhapNewsStep(): Step {
    return StepBuilder("crawlYonhapNewsStep", jobRepository)
      .tasklet(newsCrawlingTasklet, transactionManager)
      .build()
  }
}