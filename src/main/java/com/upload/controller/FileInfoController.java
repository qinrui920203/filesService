package com.upload.controller;

import com.upload.Vo.FileServerCache;
import com.upload.Vo.FileVo;
import com.upload.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileInfoController {

    @Autowired
    private FileServerCache fileServerCache;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<FileVo> listFile(){
        return fileServerCache.getFileInfoList();
    }

    @RequestMapping(value = "/refreshList", method = RequestMethod.GET)
    public List<FileVo> refreshList(){
        String basePath = fileServerCache.getBasePath();

        List<FileVo> fileVoList = FileUtils.getDirChildFileInfos(basePath);
        fileServerCache.setFileInfoList(fileVoList);

        return fileVoList;
    }
}
