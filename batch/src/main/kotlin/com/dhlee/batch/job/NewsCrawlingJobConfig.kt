package com.dhlee.batch.job

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
  @Bean
  fun yonhapNewsCrawlingJob(): Job {
    return JobBuilder("yonhapNewsCrawlingJob", jobRepository)
      .start(crawlYonhapNewsStep())
      .incrementer(RunIdIncrementer())
      .build()
  }

  @Bean
  fun crawlYonhapNewsStep(): Step {
    return StepBuilder("crawlYonhapNewsStep", jobRepository)
      .tasklet(newsCrawlingTasklet, transactionManager)
      .build()
  }
}