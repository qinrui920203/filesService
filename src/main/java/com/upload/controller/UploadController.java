package com.upload.controller;

import com.upload.Vo.FileServerCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private FileServerCache fileServerCache;

    /**
     * 单文件上传功能
     * @Author Rui
     * @param file need upload
     * @return file description params <map>
     */
    @RequestMapping("/single")
    public Map<String, Object> uploadfile(MultipartFile file){

        Map<String, Object> res = new HashMap<String, Object>();
        String fileName = file.getOriginalFilename();
        try {
            log.debug("上传开始，【 文件名 {}， 大小 {} kb 】", fileName, file.getSize()/1024);
            File dest = new File(fileServerCache.getBasePath() + "/" + fileName);
            dest.setExecutable(false);  // 防止恶意文件攻击，禁止执行权限
            file.transferTo(dest);
            res.put("fileName", fileName);
            res.put("fileSize", file.getSize()/1024);
            fileServerCache.cacheFileInfo(file.getOriginalFilename(), file.getSize());
        } catch (Exception ex){
            log.error("something wrong in your server");
            res.put("statu", "fall");
            return res;
        }
        res.put("statu", "success");

        return res;
    }

}
