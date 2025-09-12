package com.javaBasic.javaBasic.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    private final AsyncService asyncService;

    public TestController(AsyncService asyncService) {
        this.asyncService = asyncService;
    }

    @GetMapping("/email")
    public String testEmail(@RequestParam String to) {
        asyncService.sendEmail(to); // 非同步執行
        return "寄信任務已提交：" + to;
    }
}
