package com.emsportal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class BasicRestAPI {


    @GetMapping("/")
    public ResponseEntity<String> publicHome() {
        return new ResponseEntity<>("Ok", HttpStatus.OK);
    }
    
}
