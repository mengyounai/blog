package com.example.blog.web.admin;

import cn.hutool.core.lang.UUID;
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

@Controller
@RequestMapping("/admin")
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
