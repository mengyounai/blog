package com.example.blog;

import com.example.blog.po.GirlFriend;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BlogApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void createGirlFriend() {
        GirlFriend girlFriend=new GirlFriend();
        girlFriend.setName("如花");
        girlFriend.setType("肤白貌美大长腿，温柔体贴又多金");
        System.out.println("恭喜！成功new出了女朋友！女朋友的属性为...");
        System.out.println("姓名："+girlFriend.getName());
        System.out.println("属性："+girlFriend.getType());
    }

}
