package com.hoang.grpcstudy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GrpcStudyApplication

fun main(args: Array<String>) {
    runApplication<GrpcStudyApplication>(*args)
}
