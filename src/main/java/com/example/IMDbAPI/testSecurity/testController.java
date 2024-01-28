package com.example.IMDbAPI.testSecurity;


import org.springframework.web.bind.annotation.*;

@RestController
public class testController {

    @GetMapping("/hello")
    public String sayHi() {
        return "Hello there!";
    }
}
