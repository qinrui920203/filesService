package com.upload.utils;

import com.upload.Vo.FileVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/* **
 * @Author RUI
 * @descprition 项目内部使用的file工具，功能不多
 */
public class FileUtils {

    public static final Map<String, String> TYPEMAPPING = new HashMap<String, String>();

    static {
        TYPEMAPPING.put("jpg","img");
        TYPEMAPPING.put("png","img");

        TYPEMAPPING.put("mp4","video");
        TYPEMAPPING.put("avi","video");
    }

    public static List<FileVo> getDirChildFileInfos(File baseDir){
        if(!baseDir.isDirectory()){
            return null;
        }

        List<FileVo> fileVoList = Arrays.stream(baseDir.listFiles())
                .map(FileUtils::getInfoFromFile)
                .sorted(Comparator.comparing(FileVo::getType))
                .collect(Collectors.toList());

        return fileVoList;
    }

    public static List<FileVo> getDirChildFileInfos(String baseDirPath){
        File baseDir = new File(baseDirPath);
        return getDirChildFileInfos(baseDir);
    }

    public static FileVo getInfoFromFile(File file) {
        FileVo fileVo = new FileVo();

        String fileName = file.getName();
        fileVo.setName(fileName);

        if(file.isDirectory()) {
            fileVo.setType("dir");
        } else {
            int lastIndexOfPoint = fileName.lastIndexOf(".");
            int nameSize = fileName.length();

            if(lastIndexOfPoint<0){
                fileVo.setType("unKonw");
            } else {
                String fileLastName = fileName.substring(lastIndexOfPoint+1, nameSize);
                String fileType = TYPEMAPPING.get(fileLastName);
                fileType = (null==fileType) ? "file" : fileType;
                fileVo.setType(fileType);
            }
        }

        fileVo.setSize(file.length()/1024);

        return fileVo;
    }
}
