package de.bgs

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BgsApplication

fun main(args: Array<String>) {
	runApplication<BgsApplication>(*args)
}
