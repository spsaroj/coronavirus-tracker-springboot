package com.example.coronatracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller     //This makes this Spring Controller
public class HomeController {

    @GetMapping("/")        //Maps to the root URL
    public String home(){
        return "home";
    }

}