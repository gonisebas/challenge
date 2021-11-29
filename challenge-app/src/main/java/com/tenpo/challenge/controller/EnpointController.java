package com.tenpo.challenge.controller;

import com.tenpo.challenge.model.EndpointEntryEntiy;
import com.tenpo.challenge.service.EndpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/history")
public class EnpointController {

    @Autowired
    private EndpointService endpointService;

    @GetMapping()
    public ResponseEntity<Page<EndpointEntryEntiy>> find(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(endpointService.findAll(PageRequest.of(page, size, Sort.by("id").ascending())));
    }
}
