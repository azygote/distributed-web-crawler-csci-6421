package org.gty.crawler.master.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CrawlerDemoController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
