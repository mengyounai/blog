package com.example.blog.web.admin;

import com.UpYun;
import com.example.blog.config.UpYunConfig;
import com.example.blog.util.ResultVOUtil;
import com.example.blog.vo.ResultVO;
import com.upyun.UpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private UpYunConfig upYunConfig;


    @PostMapping("/upload")
    public ResultVO upload(@RequestParam("file_data")MultipartFile multipartFile) throws IOException,UpException{
        UpYun upYun=new UpYun(upYunConfig.getBucketName(),upYunConfig.getUsername(),upYunConfig.getPassword());
        String fileName=String.format("%s.%s",
                UUID.randomUUID().toString(),
                multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".")+1)
        );
        upYun.writeFile(fileName,multipartFile.getInputStream(),true,new HashMap<>());
        Map map=new HashMap<>();
        map.put("fileName",fileName);
        map.put("fileUrl","http://"+upYunConfig.getBucketName()+"."+upYunConfig.getHostName()+"/"+fileName);
        return ResultVOUtil.success(map);
    }
}
