package com.swx.swxaiagent;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.swx.swxaiagent.web.mapper")
public class SwxAiAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwxAiAgentApplication.class, args);
    }

}
