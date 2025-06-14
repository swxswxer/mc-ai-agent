package com.swx.swxaiagent.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: swx-ai-agent
 * @ClassName: HealthController
 * @description:
 * @author:
 * @create: 2025/5/26 16:19
 */
@RestController
@RequestMapping("/health")
public class HealthController {
    @GetMapping
    public String health() {
        return "OK";
    }

}
