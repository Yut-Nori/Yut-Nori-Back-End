package com.example.yutnoribackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tinylog.Logger;

@RestController
public class TestController {
    @GetMapping("/")
    public void test(){
        Logger.info("tttt");
    }

}
