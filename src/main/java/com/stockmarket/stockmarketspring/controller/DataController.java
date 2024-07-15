package com.stockmarket.stockmarketspring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/data")
public class DataController {

    @PostMapping("/db_create")
    public ResponseEntity<String> createDatabase() {

        return ResponseEntity.ok("Database and schemas created successfully.");
    }
}