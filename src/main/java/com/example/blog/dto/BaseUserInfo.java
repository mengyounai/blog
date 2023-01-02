package com.example.blog.dto;

import lombok.Data;

@Data
public class BaseUserInfo {

    protected Integer userId;

    protected String userName;

    protected Integer isAdmin;
}
