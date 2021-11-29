package com.tenpo.challenge.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/add")
public class SumOperationController {

    @GetMapping()
    public ResponseEntity<Float> add(@RequestParam float x, @RequestParam float y) {
        return ResponseEntity.ok(x+y);
    }
}
