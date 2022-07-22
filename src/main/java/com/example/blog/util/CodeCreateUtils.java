package com.example.blog.util;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
public class CodeCreateUtils {


    //得到32位的uuid
    public String getUUID32() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }


    public static int length(int number) {
        int length = (number + "").length();
        return length;
    }

    public static String get4Code(int id) {
        int length = length(id);
        StringBuffer sb = new StringBuffer();
        String codes = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
        Random random = new Random();
        for (int i = 0; i < codes.length(); i++) {
            char c = codes.charAt(random.nextInt(codes.length()));
            if (length+sb.length() == 4) break;
            if (!sb.toString().contains(c + "")) sb.append(c);
        }
        sb.append(id);
        return sb.toString();
    }

    public static void main(String[] args) {
        CodeCreateUtils createUtils=new CodeCreateUtils();
        System.out.println(createUtils.get4Code(23));
    }
}

