package com.example.yunxu_ai;

//云絮ai智能对话助手
import com.example.yunxu_ai.config.YunxuAgentProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(YunxuAgentProperties.class)
@MapperScan("com.example.yunxu_ai.mapper")
@SpringBootApplication
public class YunXuAiApplication {
    public static void main(String[] args) {
        SpringApplication.run(YunXuAiApplication.class, args);
    }
}
