package com.example.blog.web;


import com.alibaba.fastjson.JSON;
import com.example.blog.service.UserService;
import com.example.blog.util.HttpClientsUtils;
import com.example.blog.util.ResultVOUtil;
import com.example.blog.util.URLEncodeUtil;
import com.example.blog.vo.ResultVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 *
 **/
@RestController
@RequestMapping("/qqLogin/")
public class QqLoginController {


    private String qqAppId = "1018";//appid
    private String qqAppSecret = "5964bae5e12b80256e9";//app key
    private String qqRedirectUrl = "http://127.0.0.1:8091/qqLogin/callback";//qq配置的回调


    @GetMapping("/getCode")
    public String getCode() throws Exception {
        //拼接url
        StringBuilder url = new StringBuilder();
        url.append("https://graph.qq.com/oauth2.0/authorize?");
        url.append("response_type=code");
        url.append("&client_id=" + qqAppId);
        //回调地址 ,回调地址要进行Encode转码
        String redirect_uri = qqRedirectUrl;
        //转码
        url.append("&redirect_uri=" + URLEncodeUtil.getURLEncoderString(redirect_uri));
        url.append("&state=ok");
        String result = HttpClientsUtils.get(url.toString(), "UTF-8");
        System.out.println(url.toString());
        return url.toString();
    }

    /**
     * 获取token,该步骤返回的token期限为一个月
     *
     * @param code
     * @return
     * @throws Exception
     */
    @GetMapping("callback")
    public ResultVO getAccessToken(String code) throws Exception {
        if (code != null) {
            System.out.println(code);
        }
        StringBuilder url = new StringBuilder();
        url.append("https://graph.qq.com/oauth2.0/token?");
        url.append("grant_type=authorization_code");
        url.append("&client_id=" + qqAppId);
        url.append("&client_secret=" + qqAppSecret);
        url.append("&code=" + code);
        //回调地址
        String redirect_uri = qqRedirectUrl;
        //转码
        url.append("&redirect_uri=" + URLEncodeUtil.getURLEncoderString(redirect_uri));
        String result = HttpClientsUtils.get(url.toString(), "UTF-8");
        System.out.println("url:" + url.toString());
        //把token保存
        String[] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(result, "&");

        String accessToken = StringUtils.substringAfterLast(items[0], "=");
        Long expiresIn = new Long(StringUtils.substringAfterLast(items[1], "="));
        String refreshToken = StringUtils.substringAfterLast(items[2], "=");
        Map<String, Object> qqProperties = new HashMap<>();
        if (qqProperties.get("accessToken") != null) {
            qqProperties.remove("accessToken");
        }
        if (qqProperties.get("expiresIn") != null) {
            qqProperties.remove("expiresIn");
        }
        if (qqProperties.get("refreshToken") != null) {
            qqProperties.remove("refreshToken");
        }
        qqProperties.put("accessToken", accessToken);
        qqProperties.put("expiresIn", expiresIn);
        qqProperties.put("refreshToken", refreshToken);
        return ResultVOUtil.success(qqProperties);
    }

    @GetMapping("/refreshToken")
    public ResultVO refreshToken(@RequestParam String refreshToken) throws Exception {
        // 获取refreshToken
        StringBuilder url = new StringBuilder("https://graph.qq.com/oauth2.0/token?");
        url.append("grant_type=refresh_token");
        url.append("&client_id=" + qqAppId);
        url.append("&client_secret=" + qqAppSecret);
        url.append("&refresh_token=" + refreshToken);
        System.out.println("url:" + url.toString());
        String result = HttpClientsUtils.get(url.toString(), "UTF-8");
        // 把新获取的token存到map中
        String[] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(result, "&");
        String accessToken = StringUtils.substringAfterLast(items[0], "=");
        Long expiresIn = new Long(StringUtils.substringAfterLast(items[1], "="));
        String newRefreshToken = StringUtils.substringAfterLast(items[2], "=");
        //重置信息
        Map<String, Object> qqProperties = new HashMap<>();
        qqProperties.put("accessToken", accessToken);
        qqProperties.put("expiresIn", String.valueOf(expiresIn));
        qqProperties.put("refreshToken", newRefreshToken);
        return ResultVOUtil.success(qqProperties);
    }


    /**
     * 获取用户openId
     *
     * @return
     * @throws Exception
     */
    @GetMapping("getOpenId")
    public ResultVO getOpenId(@RequestParam String accessToken) throws Exception {
        StringBuilder url = new StringBuilder("https://graph.qq.com/oauth2.0/me?");
        //获取保存的用户的token
//		String accessToken = (String) qqProperties.get("accessToken");
        if (!StringUtils.isNotEmpty(accessToken)) {
            return ResultVOUtil.error(101, "失败");
        }
        url.append("access_token=" + accessToken);
        String result = HttpClientsUtils.get(url.toString(), "UTF-8");
        String openId = StringUtils.substringBetween(result, "\"openid\":\"", "\"}");
        System.out.println(openId);
        Map<String, Object> qqProperties = new HashMap<String, Object>();
        //把openId存到map中
        if (qqProperties.get("openId") != null) {
            qqProperties.remove("openId");
        }
        qqProperties.put("openId", openId);
        qqProperties.put("accessToken", accessToken);
        return ResultVOUtil.success(qqProperties);
    }


    /**
     * 根据openId获取用户信息
     */
    @RequestMapping("getUserInfo")
    public ResultVO getUserInfo(@RequestParam String accessToken, @RequestParam String openId) throws Exception {
        StringBuilder url = new StringBuilder("https://graph.qq.com/user/get_user_info?");
        //取token
        if (!StringUtils.isNotEmpty(accessToken) || !StringUtils.isNotEmpty(openId)) {
            return null;
        }
        url.append("access_token=" + accessToken);
        url.append("&oauth_consumer_key=" + qqAppId);
        url.append("&openid=" + openId);
        String result = HttpClientsUtils.get(url.toString(), "UTF-8");
//        Object json = JSON.parseObject(result,QQUserInfo.class);
//        QQUserInfo QQUserInfo = (QQUserInfo)json;
//        return ResultVOUtil.success(QQUserInfo);
        return null;
    }
}