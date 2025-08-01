package com.example.learningAPISpring.controllers;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @GetMapping
    public String test(){
        return "Hello world";
    }
}
