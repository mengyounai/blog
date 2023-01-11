package com.example.blog.web.admin;

import com.UpYun;
import com.example.blog.config.UpYunConfig;
import com.example.blog.po.Blog;
import com.example.blog.po.User;
import com.example.blog.util.CodeCreateUtils;
import com.example.blog.util.ResultVOUtil;
import com.example.blog.vo.ResultVO;
import com.upyun.Result;
import com.upyun.UpException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private UpYunConfig upYunConfig;

    @GetMapping
    public String imageInput() {
        return "image.html";
    }

    //新增&更新博客
    @PostMapping("/blogs")
    public String post(Blog blog, @RequestParam("file00") MultipartFile file, RedirectAttributes attributes, HttpSession session) throws IOException, UpException {

        String firstPicture = blog.getFirstPicture();
        //如果file不为空，则上传图片到upyun
        if (!file.isEmpty()) {
            UpYun upYun = new UpYun(upYunConfig.getBucketName(), upYunConfig.getUsername(), upYunConfig.getPassword());
            String format = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            String name = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
            String fileName = "/test/" +name + CodeCreateUtils.get4Code(4) + "." + format;

            boolean b1 = upYun.writeFile(fileName, file.getInputStream(), true, new HashMap<>());
            if (!b1) {
                attributes.addFlashAttribute("message", "新增失败");
                return "redirect:/admin/blogs";
            }
            firstPicture ="http://" + upYunConfig.getBucketName() + "." + upYunConfig.getHostName() + "/" + fileName;
        }

        return "redirect:/admin/blogs";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file_data") MultipartFile multipartFile,RedirectAttributes attributes, HttpSession session) throws IOException, UpException {
        UpYun upYun = new UpYun(upYunConfig.getBucketName(), upYunConfig.getUsername(), upYunConfig.getPassword());
//        String fileName = String.format("%s.%s",
//                UUID.randomUUID().toString(),
//                multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1)
//        );
        String format = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1);
        String name = multipartFile.getOriginalFilename().substring(0,multipartFile.getOriginalFilename().lastIndexOf("."));
//        String fileName = "/blog/" +name + CodeCreateUtils.get4Code(4) + "." + format;
        String fileName = "/blog/" +name +  "." + format;
        try{
            String s = upYun.readFile(fileName);
            fileName = "/blog/" +name + CodeCreateUtils.get4Code(4) + "." + format;
        }catch (Exception e){

        }

        upYun.writeFile(fileName, multipartFile.getInputStream(), true, new HashMap<>());
        Map map = new HashMap<>();
//        map.put("fileName", fileName);
        map.put("fileUrl", "http://" + upYunConfig.getBucketName() + "." + upYunConfig.getHostName() + "/" + fileName);
        attributes.addFlashAttribute("map", map);

        return "redirect:/image";
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
