package com.lmf.springtest.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

/**
 * 通过手动创建bean来做的注入 在类FtpConfig中
 * @author admin
 */
@Component
public class FtpFileUtil {

    private final Logger logger = LoggerFactory.getLogger(FtpFileUtil.class);

    @Value("${ftp.ftpIp}")
    private String ftpIp;
    @Value("${ftp.ftpUser}")
    private String ftpUser;
    @Value("${ftp.ftpPwd}")
    private String ftpPwd;
    @Value("${ftp.ftpPort}")
    private Integer ftpPort;

    public void setFtpIp(String ftpIp) {
        this.ftpIp = ftpIp;
    }

    public void setFtpUser(String ftpUser) {
        this.ftpUser = ftpUser;
    }
    public void setFtpPwd(String ftpPwd) {
        this.ftpPwd = ftpPwd;
    }

    public void setFtpPort(Integer ftpPort) {
        this.ftpPort = ftpPort;
    }

    /**
     *  上传
     * @param file
     * @param remotePath 服务器上的路径
     * @return
     */
    public String uploadFileFromFTP(MultipartFile file, String remotePath) {
        File temporaryFiles = MultipartFileToFile(file);
        String fileName = uploadFileFromFTP(temporaryFiles, remotePath);
        temporaryFiles.delete();
        return fileName;
    }

    /**
     *  上传
     * @param file
     * @param remotePath 服务器上的路径
     * @return
     */
    public String uploadFileFromFTP(File file, String remotePath) {
        logger.error("ftp文件夹路径" + remotePath);
        FTPClient ftpClient = new FTPClient();
        FileInputStream fis = null;
        String newFileName = null;
        String[] split = remotePath.split("/");
        try {
            logger.error("开始连接ftp服务器 ip:{}  port:{}  user:{}", ftpIp, ftpPort, ftpUser);
            ftpClient.setControlEncoding("gbk");
            ftpClient.connect(ftpIp, ftpPort);
            ftpClient.login(ftpUser, ftpPwd);
            ftpClient.enterLocalPassiveMode();
            // 设置上传目录
            logger.error("连接成功。。。。。。。");
            ftpClient.setBufferSize(1024 * 100);
            ftpClient.setConnectTimeout(60 * 1000);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            for (String str : split) {
                if (StringUtils.isEmpty(str)) {
                    continue;
                }
                if (!ftpClient.changeWorkingDirectory(str)) {
                    ftpClient.makeDirectory(str);
                    ftpClient.changeWorkingDirectory(str);
                }
            }
            //获取扩展名（abc.jpg）----.jpg
            String fileName = file.getName();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            //设置一个新的文件扩展名
            newFileName = UUID.randomUUID().toString().replace("-","")+"."+suffix;
            // 保存文件
            logger.error("准备开始上传文件。。。。。。");
            logger.error("新文件名为：{}", newFileName);
            fis = new FileInputStream(file);
            ftpClient.storeFile(newFileName, fis);
            logger.error("文件上传完成。。。。。");
        } catch (NumberFormatException e) {
            logger.error("FTP端口配置错误:不是数字:{}", e.getMessage());
        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException:{}", e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("IOException:{}", e.getMessage());
            e.printStackTrace();
        } catch (Exception e){
            logger.error("Exception:{}", e.getMessage());
            e.printStackTrace();
        } finally{
            IOUtils.closeQuietly(fis);
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                throw new RuntimeException("关闭FTP连接发生异常！", e);
            }
        }
        logger.error("上传完成。。。。。。");
        return newFileName;
    }

    /**
     *  下载
     * @param remotePath 服务器上的路径
     * @param fileName   要下载的文件
     * @return 返回文件流，如果为空表示为获取到
     */
    public InputStream downloadFileFromFTP(String fileName, String remotePath) {
        boolean success = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            // 1. 连接ftp服务器, 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftp.connect(ftpIp, ftpPort);
            // 2. 登录ftp服务器
            ftp.login(ftpUser, ftpPwd);
            ftp.enterLocalPassiveMode();
            ftp.setControlEncoding("gbk");
            // 3. 登录是否成功
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                // logger.error("连接服务器异常");
                return null;
            }
            // 设置上传目录
            ftp.changeWorkingDirectory(remotePath);
            FTPFile[] fs = ftp.listFiles();
            for (FTPFile ff : fs) {
                String name = new String(ff.getName().getBytes("GBK"), "ISO-8859-1");
                if (name.equals(fileName)) {
                    InputStream is = ftp.retrieveFileStream(fileName);
                    return is;
                }
            }
            ftp.logout();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 将file转换成fileItem
     * @param file
     * @param fieldName
     * @return
     */
    private static FileItem getMultipartFile(File file, String fieldName){
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem(fieldName, "text/plain", true, file.getName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }

    /**
     * MultipartFile 转 File
     *
     * @param multipartFile
     * @throws Exception
     */
    public File MultipartFileToFile(MultipartFile multipartFile) {

        File file = null;
        //判断是否为null
        if (multipartFile.equals("") || multipartFile.getSize() <= 0) {
            return file;
        }
        //MultipartFile转换为File
        InputStream ins = null;
        OutputStream os = null;
        try {
            ins = multipartFile.getInputStream();
            file = new File(multipartFile.getOriginalFilename());
            os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(os != null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(ins != null){
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

}
