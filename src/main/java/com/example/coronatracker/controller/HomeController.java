package com.example.coronatracker.controller;

import com.example.coronatracker.models.LocationStats;
import com.example.coronatracker.services.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller     //This makes this Spring Controller
public class HomeController {

    @Autowired
    CoronaVirusDataService coronaVirusDataService;      //This is auto-wiring

    @GetMapping("/")        //Maps to the root URL
    public String home(Model model){

        List<LocationStats> allStats = coronaVirusDataService.getEveryStats();

        //This will get the sum of all latest Reported Cases
        int totalReportedCases = allStats.stream().mapToInt(stat -> stat.getLatestReportedCases()).sum();
        int totalNewCases = allStats.stream().mapToInt(stat -> stat.getDelta()).sum();

        model.addAttribute("LocationStats",allStats);
        model.addAttribute("totalReportedCases",totalReportedCases);        //These are to pass the values to home.html
        model.addAttribute("totalNewCases",totalNewCases);

        return "home";

    }

}