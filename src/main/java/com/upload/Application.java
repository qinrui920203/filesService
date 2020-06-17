package com.upload;

import com.upload.Vo.FileServerCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public FileServerCache fileServerCache(){
        return new FileServerCache();
    }
}
