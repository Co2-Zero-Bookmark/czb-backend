package com.carbonhater.co2zerobookmark.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class WebController {

    @GetMapping("/")
    public String home(Model model) {
        log.info("=============log Test=============");
        model.addAttribute("message", "Hello, Thymeleaf!");
            return "index"; // 'index.html' 파일을 렌더링
    }
}
