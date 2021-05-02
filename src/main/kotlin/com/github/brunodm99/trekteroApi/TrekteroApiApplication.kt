package com.github.brunodm99.trekteroApi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@SpringBootApplication
class TrekteroApiApplication

fun main(args: Array<String>) {
	runApplication<TrekteroApiApplication>(*args)
}
