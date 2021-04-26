package com.github.brunodm99.trekteroApi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TrekteroApiApplication

fun main(args: Array<String>) {
	runApplication<TrekteroApiApplication>(*args)
}
