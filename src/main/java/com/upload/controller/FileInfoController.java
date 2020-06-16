package com.upload.controller;

import com.upload.Vo.FileVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileInfoController {

    @Value("${fileserver.basepath}")
    private String filePath;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<FileVo> listFile(){
        File baseDir = new File(filePath);
        return Arrays.stream(baseDir.list()).map(fileName -> {
            FileVo fileVo = new FileVo();
            String lastName = fileName.split(".")[1];
            fileVo.setType(lastName);
            fileVo.setName(fileName);
            fileVo.setSize("我也不知道鸭");
            return fileVo;
        }).collect(Collectors.toList());
    }

}
