package com.example.blog.po;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_token")
public class Token {

    @Id
    @GeneratedValue
    private Long id;
    private String info;
    private String tokenUUID;

}
