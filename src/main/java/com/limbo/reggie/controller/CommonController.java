package com.limbo.reggie.controller;


import com.limbo.reggie.common.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 处理文件上传和下载
 */
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}") //通过yml配置文件获取
    private String basePath;


    @RequestMapping("/upload")
    public Result<String> upload(MultipartFile file) {

        String originalFilename = file.getOriginalFilename();
//        assert originalFilename != null;

        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        String fileName = UUID.randomUUID() + suffix;

        File dir = new File(basePath);
        if (!dir.exists())
            dir.mkdirs();

        try {
            file.transferTo(new File(basePath + fileName));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.success(fileName);

    }

    /**
     * 通过name来获取图片
     * 功能：在新增菜品的时候需要上传图片，用户上传之后需要在那个页面进行显示
     *
     * @param name
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        try {
            FileInputStream inputStream = new FileInputStream(new File(basePath + name));

            ServletOutputStream outputStream = response.getOutputStream();

            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }

            inputStream.close();
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
