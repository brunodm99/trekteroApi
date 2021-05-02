package com.github.brunodm99.trekteroApi.data.controller

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/test")
class TestController {

    @GetMapping("hello")
    fun hello() = "Hello World!!!"

}