package com.lmf.springtest.ftp.controller;

import com.lmf.springtest.utils.FtpFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author lmf
 * @date 2022/8/2
 */
@RestController
public class FtpController {

    @Autowired
    FtpFileUtil ftpFileUtil;

    @PostMapping("upload")
    public String upload(MultipartFile file){
        String dateStr = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now());

        String s = ftpFileUtil.uploadFileFromFTP(file, dateStr);
        return s;
    }

    @PostMapping("download")
    public void download(
            String fileName,
            String path,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setLocale(new java.util.Locale("zh","CN"));
        response.setHeader("Content-Disposition", "attachment; fileName=" + URLEncoder.encode(fileName, "UTF-8"));
        InputStream inputStream = ftpFileUtil.downloadFileFromFTP(fileName, path);
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }


}
