package com.upload.utils;

import com.upload.Vo.FileVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/* **
 * @Author RUI
 * @descprition 项目内部使用的file工具，功能不多
 */
public class FileUtils {

    public static List<FileVo> getDirChildFileInfos(File baseDir){
        if(!baseDir.isDirectory()){
            return null;
        }

        List<FileVo> fileVoList = Arrays.stream(baseDir.listFiles()).map(file -> {
            FileVo fileVo = new FileVo();
            String fileName = file.getName();
            fileVo.setName(fileName);
            fileVo.setType(file.isDirectory()?"dir":fileName.split("\\.")[fileName.split("\\.").length-1]);
            fileVo.setSize(file.length()/1024);

            return fileVo;
        }).collect(Collectors.toList());

        return fileVoList;
    }

    public static List<FileVo> getDirChildFileInfos(String baseDirPath){
        File baseDir = new File(baseDirPath);
        return getDirChildFileInfos(baseDir);
    }

}
