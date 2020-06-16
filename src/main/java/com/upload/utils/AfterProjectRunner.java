package com.upload.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class AfterProjectRunner implements ApplicationRunner {

    @Value("${fileserver.basepath}")
    private String filePath;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        File baseDir = new File(filePath);
        if(!baseDir.exists()){
            baseDir.mkdir();
        }
    }

}
