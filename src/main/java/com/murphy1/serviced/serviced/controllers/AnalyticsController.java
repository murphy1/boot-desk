package com.murphy1.serviced.serviced.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class AnalyticsController {

    @GetMapping("/analytics")
    public String openAnalytics(){

        return "/analytics.html";
    }

}
