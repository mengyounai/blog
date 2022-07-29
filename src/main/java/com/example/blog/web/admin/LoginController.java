package com.example.blog.web.admin;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.blog.config.WebSocketConfig;
import com.example.blog.config.WebSocketServer;
import com.example.blog.po.Token;
import com.example.blog.po.User;
import com.example.blog.service.ITokenService;
import com.example.blog.service.UserService;
import com.example.blog.service.impl.TokenServiceImpl;
import com.example.blog.util.EmptyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@CrossOrigin
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private ITokenService tokenService;

    @GetMapping
    public String loginPage() {
        return "admin/login.html";
    }

//    @GetMapping("/login")
//    public String login(@RequestParam String username,
//                        @RequestParam String password,
//                        HttpSession session,
//                        RedirectAttributes attributes) {
//        User user = userService.checkUser(username, password);
//        if (user != null) {
//            user.setPassword(null);
//            session.setAttribute("user", user);
//            return "admin/index.html";
//        } else {
//            attributes.addFlashAttribute("message", "用户名或密码错误");
//            return "redirect:/admin";
//        }
//
//    }


    @GetMapping("/login")
    public String adminLogin(@RequestParam String username,
                             @RequestParam String password, HttpServletRequest request,
                             HttpServletResponse response,RedirectAttributes attributes) {
        User user = userService.checkUser(username, password);
        if (EmptyUtil.isEmpty(user)) {
            attributes.addFlashAttribute("message", "用户名或密码错误");
            return "redirect:/admin";
        }
        //存token进数据库
        Token token = tokenService.findByUserName(username);
        if (EmptyUtil.isEmpty(token)){
            token =  new Token();
        }
        String tokenUUID = UUID.randomUUID().toString();

        token.setInfo(username);
        token.setTokenUUID(tokenUUID);
        tokenService.addToken(token);
        //存token进cookie
        Cookie cookie=new Cookie("token",tokenUUID);
        //这里需要注意要将cookie路径设置为根目录
        cookie.setPath("/");
        //设置到期时间为一个月 默认-1关闭浏览器即消失
        cookie.setMaxAge(60 * 60 * 24 * 30);
        response.addCookie(cookie);
//        session.setAttribute("user", user);
        request.getSession().setAttribute("user", user);
        return "admin/index";
    }

    @GetMapping("/loginNoCheck")
    public String adminLoginNoCheck(@RequestParam("token") String uuid ,HttpServletRequest request,
                             HttpServletResponse response,RedirectAttributes attributes) {
        String username = tokenService.getUserName(uuid);

        User user = userService.findUser(username);
        if (EmptyUtil.isEmpty(user)) {
            attributes.addFlashAttribute("message", "token失效");
            return "redirect:/admin";
        }
        //存token进数据库
        Token token = tokenService.findByUserName(username);
        if (EmptyUtil.isEmpty(token)){
            token =  new Token();
        }
        String tokenUUID = UUID.randomUUID().toString();

        token.setInfo(username);
        token.setTokenUUID(tokenUUID);
        tokenService.addToken(token);
        //存token进cookie
        Cookie cookie=new Cookie("token",tokenUUID);
        //这里需要注意要将cookie路径设置为根目录
        cookie.setPath("/");
        //设置到期时间为一个月 默认-1关闭浏览器即消失
        cookie.setMaxAge(60 * 60 * 24 * 30);
        response.addCookie(cookie);
        request.getSession().setAttribute("user", user);
        return "admin/index";
    }

    //获取登录二维码、放入Token
    @GetMapping( "/getLoginQr")
    public void createCodeImg(HttpServletRequest request, HttpServletResponse response){
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        try {
            String username = "季善乐";
            Token token =tokenService.findByUserName(username);
            if (EmptyUtil.isEmpty(token)){
                token = new Token();
            }
            String tokenUUID = UUID.randomUUID().toString();

            token.setInfo(username);
            token.setUserId(1L);
            token.setState(1);
            token.setCreateTime(new Date());
            token.setTokenUUID(tokenUUID);
            tokenService.addToken(token);
            //这里没啥操作 就是生成一个UUID插入 数据库的表里

            response.addHeader("Access-Control-Expose-Headers", "uuid");
            response.setHeader("uuid", tokenUUID);
            // 网址：http://hutool.mydoc.io/
//            QrCodeUtil.generate("http://hutool.cn/", 300, 300, FileUtil.file("d:/qrcode.jpg"));
            String url = "http://192.168.1.60:8083/admin/bindUserIdAndToken?token="+tokenUUID+"&userId=1";
            QrCodeUtil.generate(url, 300, 300, "jpg",response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 确认身份接口：确定身份以及判断是否二维码过期等
     * @param token
     * @param userId
     * @return
     */
    @GetMapping("/bindUserIdAndToken")
    public String bindUserIdAndToken(@RequestParam("token") String token ,
                                     @RequestParam("userId") Long userId,
                                     @RequestParam(required = false,value = "projId") Integer projId,
                                     HttpServletRequest request,HttpServletResponse response) throws IOException {

//        String requestSessionId = request.getRequestedSessionId();
//        if (!requestSessionId.equals("2DAA8DEAC46B85DEBF32850B7ACE08C2")){
//            return "非管理员用户";
//        }
        User user = new User();

        user = userService.bindUserIdAndToken(userId, token, projId);

        if (EmptyUtil.isNotEmpty(user)){
            request.getSession().setAttribute("user", user);
            Cookie cookie=new Cookie("token",token);
            //这里需要注意要将cookie路径设置为根目录
            cookie.setPath("/");
            //设置到期时间为一个月 默认-1关闭浏览器即消失
            cookie.setMaxAge(60 * 60 * 24 * 30);
            response.addCookie(cookie);

            WebSocketServer.sendInfo(JSONObject.toJSONString(user),null);

            return "admin/index";
        }else {
            return "redirect:/admin";
        }

    }


    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        session.removeAttribute("user");
        Cookie cookie=new Cookie("token","token");
        //这里需要注意要将cookie路径设置为根目录
        cookie.setPath("/");
        //设置到期时间为一个月 默认-1关闭浏览器即消失
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/admin";
    }
}
