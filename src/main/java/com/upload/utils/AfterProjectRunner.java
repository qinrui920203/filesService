package com.upload.utils;

import com.upload.Vo.FileServerCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;

/* **
 * @Author RUI
 * @Description springboot 启动后加载资源到缓存对象中
 */
@Component
public class AfterProjectRunner implements ApplicationRunner {

    @Autowired
    private FileServerCache fileServerCache;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        File baseDir = new File(fileServerCache.getBasePath());
        if(!baseDir.exists()){
            baseDir.mkdir();
        }
        // 初始时缓存文件信息
        fileServerCache.initChace();
        fileServerCache.setFileInfoList(FileUtils.getDirChildFileInfos(fileServerCache.getBasePath()));
    }

}
