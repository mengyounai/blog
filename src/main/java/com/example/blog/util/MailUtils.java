package com.example.blog.util;

import org.apache.commons.mail.HtmlEmail;

public class MailUtils {

    // 发件人邮箱
    private static String MAIL_ADDRESS = "2193873302@qq.com";
    //    // 发件人
//    private static String MAIL_USER = "无限缤纷";
//    // 邮件的名称
//    private static String MAIL_NAME = "无限缤纷博客, 网址: 2193873302@qq.com";
    // 上面获取的客户端授权码
    private static String MAIL_PASS = "kwkndeablbltecaf";

    public static boolean sendHtmlEmail(String emailaddress, String msg, String MAIL_USER, String MAIL_NAME) {
        try {
            HtmlEmail email = new HtmlEmail();// 创建一个HtmlEmail实例对象
            email.setHostName("smtp.qq.com");// 设置服务器 126邮箱为smtp.126.com,163邮箱为163.smtp.com，QQ为smtp.qq.com
            email.setCharset("UTF-8"); // 设置编码
            email.addTo(emailaddress);// 设置收件人地址地址

            email.setFrom(MAIL_ADDRESS, MAIL_USER);// 此处填发件人的邮箱地址和用户名,用户名可以任意填写

            email.setAuthentication(MAIL_ADDRESS, MAIL_PASS);// 发件人邮箱地址和客户端授权码
            email.setSSLOnConnect(true); // 使用qq邮箱发送邮件时需要设置，否则上传到服务器就无法使用了（亲身经历）

            email.setSubject(MAIL_NAME);// 设置邮件名，邮件名可任意填写
            email.setContent(msg, "text/html");// 设置内容解析为html, 可以编写样式, 因为原来的字体在手机上展示时太小了, 设置后可以用html的style属性自定义样式
            email.setMsg(msg);//此处填写邮件内容
            email.send(); // 发送邮件
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
