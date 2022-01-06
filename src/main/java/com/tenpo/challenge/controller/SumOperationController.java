package com.tenpo.challenge.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/add")
public class SumOperationController {

    @GetMapping()
    public Float add(@RequestParam float x, @RequestParam float y) {
        return x+y;
    }
}
