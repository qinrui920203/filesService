package com.upload.controller;

import com.upload.Vo.FileServerCache;
import com.upload.Vo.FileVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

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
    @RequestMapping(value = "/single", method = RequestMethod.POST)
    public FileVo uploadfile(MultipartFile file){
        FileVo res = null;
        String fileName = file.getOriginalFilename();
        try {
            log.debug("上传开始，【 文件名 {}， 大小 {} kb 】", fileName, file.getSize()/1024);

            File dest = new File(fileServerCache.getBasePath() + "/" + fileName);
            dest.setExecutable(false);  // 防止恶意文件攻击，禁止执行权限
            file.transferTo(dest);
            res = fileServerCache.cacheFileInfo(dest);

            log.debug("上传完毕，【 文件名 {} 】", fileName);
        } catch (Exception ex){
            log.error("something wrong in your server");
            return res;
        }

        return res;
    }

}
