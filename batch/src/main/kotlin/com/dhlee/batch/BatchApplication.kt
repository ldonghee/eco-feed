package com.dhlee.batch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.dhlee"])
class BatchApplication

fun main(args: Array<String>) {
  runApplication<BatchApplication>(*args)
}

