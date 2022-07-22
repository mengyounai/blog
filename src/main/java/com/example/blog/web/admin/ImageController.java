package com.example.blog.web.admin;

import com.UpYun;
import com.example.blog.config.UpYunConfig;
import com.example.blog.util.ResultVOUtil;
import com.example.blog.vo.ResultVO;
import com.upyun.Result;
import com.upyun.UpException;
import org.json.JSONObject;
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

    @GetMapping("/q")
    public String test(){
        return "你好";
    }


    @PostMapping("/upload")
    public ResultVO upload(@RequestParam("file_data") MultipartFile multipartFile) throws IOException, UpException {
        UpYun upYun = new UpYun(upYunConfig.getBucketName(), upYunConfig.getUsername(), upYunConfig.getPassword());
        String fileName = String.format("%s.%s",
                UUID.randomUUID().toString(),
                multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1)
        );
        upYun.writeFile(fileName, multipartFile.getInputStream(), true, new HashMap<>());
        Map map = new HashMap<>();
        map.put("fileName", fileName);
        map.put("fileUrl", "http://" + upYunConfig.getBucketName() + "." + upYunConfig.getHostName() + "/" + fileName);
        return ResultVOUtil.success(map);
    }

//    @RequestMapping("/product_add")
//    public void addProduct(Product product, @RequestParam("file") MultipartFile file)throws Exception{
//        //上传的文件名
//        String filename = file.getOriginalFilename();
//        //获取文件的后缀
//        String suffixName = filename.substring(filename.lastIndexOf("."));
//        //生成一个新的文件名
//        filename= UUID.randomUUID()+suffixName;
//        System.out.println("要上传服务器的文件名是:"+filename);
//        //上传又拍云
//        byte [] byteArr=file.getBytes();
//        Result upyunData = testSync(byteArr,filename);
//        //将json转换为Object对象(这里需要引入fastjson依赖)
//        JSONObject photoMsg = JSONObject.parseObject(upyunData.getMsg());
//        //记得将<域名前缀>替换成你自己的域名
//        String photoUrl = "<域名前缀>" + photoMsg.getString("url");
//        System.out.println("文件上传成功，地址为："+photoUrl);
//        product.setPimage(photoUrl);
//    }

}
