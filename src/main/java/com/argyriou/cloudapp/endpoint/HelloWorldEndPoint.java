package com.argyriou.cloudapp.endpoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hi")
public class HelloWorldEndPoint {
    @GetMapping
    public String sayHi() {
        return "hi";
    }
}
