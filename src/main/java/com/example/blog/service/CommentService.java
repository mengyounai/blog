package com.example.blog.service;

import com.example.blog.po.Blog;
import com.example.blog.po.Comment;
import com.example.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {

    List<Comment> listCommentByBlogId(Long blogId);

    Page<Comment> listCommentByBlogId(Long blogId, Pageable pageable);

    Comment saveComment(Comment comment);

    void deleteComment(Long id);
}
