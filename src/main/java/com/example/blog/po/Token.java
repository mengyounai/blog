package com.example.blog.po;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "t_token")
public class Token {

    @Id
    @GeneratedValue
    private Long id;
    private String info;
    private Long userId;
    private String tokenUUID;
    private Date loginTime;     //登录时间
    private Date createTime;    //创建时间
    private Integer state;      //二维码是否失效



}
