package com.mall.system.controller;

import com.mall.file.FastDFSFile;
import com.mall.util.FastDFSUtil;
import entity.Result;
import entity.StatusCode;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/file")
@CrossOrigin
public class FileUploadController {

    @PostMapping("/upload")
    public Result upload(@RequestParam(value="file")MultipartFile file) throws Exception {

        //封装文件信息
        FastDFSFile fastDFSFile = new FastDFSFile(
                file.getOriginalFilename(), // 文件名 1.jpg
                file.getBytes(), // 文件字节数组
                StringUtils.getFilenameExtension(file.getOriginalFilename()) // 获取文件扩展名
        );
        String[] upload = FastDFSUtil.upload(fastDFSFile);
        // 拼接访问地址 url = http://192.168.10.106:8080/
        String url = "http://120.26.69.48:8081/" + upload[0] +"/" + upload[1];
        return new Result(true, StatusCode.OK, "上传成功", url);
    }

    /**
     * 下载文件
     * @param groupName
     * @param remoteFileName
     * @return
     */
    @GetMapping("/download")
    public InputStream download(String groupName, String remoteFileName) {
        return FastDFSUtil.downFile(groupName, remoteFileName);
    }

    /**
     * 删除文件
     * @param groupName
     * @param remoteFileName
     * @return
     */
    @GetMapping("/delete")
    public Result delete(String groupName, String remoteFileName) {
       FastDFSUtil.deleteFile(groupName, remoteFileName);
        return new Result(true, StatusCode.OK, "删除成功");
    }
}
