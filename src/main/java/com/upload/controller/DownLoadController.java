package com.upload.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.*;

@Slf4j
@Controller
@RequestMapping("/download")
public class DownLoadController {

    @Value("${fileserver.basepath}")
    private String filePath;

    /**
     * 文件下载功能
     * @param fileName 下载文件名字，要求与服务器上名字一致
     * @param response HttpServletResponse
     */

    @RequestMapping(value = "/getFileByName", method = RequestMethod.GET)
    public void downloadFile(@PathVariable String fileName,
                             HttpServletResponse response) throws IOException {

        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        byte[] buff = new byte[1024];
        BufferedInputStream bufferedInputStream = null;
        OutputStream outputStream = null;

        try {
            outputStream = response.getOutputStream();
            bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(filePath+'/'+ fileName)));
            int count = bufferedInputStream.read(buff);
            while (count > 0) {
                outputStream.write(buff,0, buff.length);
                outputStream.flush();
                count = bufferedInputStream.read(buff);
            }
        } catch(Exception ex) {
            log.error("download erro");
        } finally {
            bufferedInputStream.close();
        }
    }
}
