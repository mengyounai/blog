package com.example.blog.dao;

import com.example.blog.po.Tag;
import com.example.blog.po.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.util.List;

public interface TypeRepository extends JpaRepository<Type, Long> {

    Type findByName(String name);

    @Query("select t from Type t")
    List<Type> findTop(org.springframework.data.domain.Pageable pageable);

}
