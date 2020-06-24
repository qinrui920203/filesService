package com.upload.controller;

import com.upload.Vo.FileServerCache;
import com.upload.Vo.FileVo;
import com.upload.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;

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

            File dest = new File(fileServerCache.getBasePath() + File.separator + fileName);
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

    /**
     * 大文件分片上传
     * @Author Rui
     *
     */
    @RequestMapping(value = "/big", method = RequestMethod.POST)
    public FileVo uploadBigFile(@RequestParam(value = "data", required = false) MultipartFile file
            , HttpServletRequest request) throws IOException {
        Integer blockCount = Integer.parseInt(request.getParameter("totalCount"));
        Integer nowIndex = Integer.parseInt(request.getParameter("index"));
        String fileName = request.getParameter("fileName");

        try {
            log.debug("======= 分块{}开始上传 =======", nowIndex);

            File dest = new File(fileServerCache.getBasePath() + File.separator + fileName + ".tmp_" + nowIndex);
            file.transferTo(dest);

            FileVo blockinfo = new FileVo();
            blockinfo.setName(dest.getName());
            blockinfo.setType("block");
            blockinfo.setSize(dest.length());
            fileServerCache.addFileBlockToCache(fileName, blockinfo);
            fileServerCache.changeFileBlockStatu(fileName, "uploading");

            log.debug("======= 分块{}结束上传 =======", nowIndex);
        } catch (IOException e) {
            log.error("something wrong in your server, model write failed");
            e.printStackTrace();
        }

        if(blockCount <= fileServerCache.getBlockInfoByName(fileName).size() ){
            fileServerCache.changeFileBlockStatu(fileName, "finished");
            return mergeFiles(fileName);
        }

        return null;

    }



    private FileVo mergeFiles(String fileName) throws IOException {
        File mergedFile = new File(fileServerCache.getBasePath() + File.separator + fileName);
        List<FileVo> blocks = fileServerCache.getFileInfoList();

        FileOutputStream fileOutputStream = null;
        FileInputStream temp = null;
        // 文件追加写入 这里测试没问题后改成
        try{
            fileOutputStream = new FileOutputStream(mergedFile, true);
            byte[] byt = new byte[10 * 1024 * 1024];
            int readLen = 0;

            List<FileVo> fileVos = fileServerCache.getBlockInfoByName(fileName);
            for(FileVo fileVo : fileVos){
                File tempFile = new File(fileServerCache.getBasePath() + File.separator + fileVo.getName());
                temp = new FileInputStream(tempFile);
                while( (readLen = temp.read(byt)) != -1){
                    fileOutputStream.write(byt, 0, readLen);
                }
                temp.close();
                FileUtils.deleteFileByFile(tempFile);
            }
            fileServerCache.removeFileBlockFromCache(fileName);
        } catch (IOException ioex){
            log.debug("some thing wrong when merge file");
            ioex.printStackTrace();
        } finally {
            fileOutputStream.close();
            if(temp!=null){temp.close();}
        }

        return FileUtils.getInfoFromFile(mergedFile);
    }
}
