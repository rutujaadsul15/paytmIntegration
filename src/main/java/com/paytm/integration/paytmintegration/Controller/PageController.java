package com.paytm.integration.paytmintegration.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PageController {
    @RequestMapping("/")
    public String homePage() {
        System.out.println("Request for home page");
        return "home";
    }
    @RequestMapping("/about")
    public String aboutPage() {
        System.out.println("Request for about page");
        return "about";
    }
}
