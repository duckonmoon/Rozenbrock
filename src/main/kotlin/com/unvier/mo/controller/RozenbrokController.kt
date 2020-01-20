package com.unvier.mo.controller

import com.unvier.mo.util.Rozenbrok
import org.springframework.web.bind.annotation.*

data class Answer(val result: String)

@CrossOrigin("{*}")
@RestController
@RequestMapping("api/calculate")
class RozenbrokController {
    @GetMapping("/")
    fun calculateRB(@RequestParam(required = true) alpha: Double,
                    @RequestParam(required = true) beta: Double,
                    @RequestParam(required = true) d1Input: Array<Double>,
                    @RequestParam(required = true) d2Input: Array<Double>,
                    @RequestParam(required = true) d3Input: Array<Double>,
                    @RequestParam(required = true) delta1: Double,
                    @RequestParam(required = true) delta2: Double,
                    @RequestParam(required = true) delta3: Double,
                    @RequestParam(required = true) eps: Double,
                    @RequestParam(required = true) x0: Array<Double>): Answer = Answer(Rozenbrok(
            alfa = alpha,
            beta = beta,
            d1_input = d1Input,
            d2_input = d2Input,
            d3_input = d3Input,
            delta1 = delta1,
            delta2 = delta2,
            delta3 = delta3,
            eps = eps,
            x_0 = x0
    ).calculate())
}