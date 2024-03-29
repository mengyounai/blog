package com.example.blog.web;

import com.example.blog.po.Blog;
import com.example.blog.po.Comment;
import com.example.blog.po.User;
import com.example.blog.service.BlogService;
import com.example.blog.service.CommentService;
import com.example.blog.util.MailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

/**
 * Created by limi on 2017/10/22.
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;


    @Value("${comment.avatar}")
    private String avatar;

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model) {
        model.addAttribute("comments", commentService.listCommentByBlogId(blogId));
        return "blog :: commentList";
    }

    @GetMapping("/comments2/{blogId}")
    public String comments2(@PathVariable Long blogId, Model model) {
        model.addAttribute("comments", commentService.listCommentByBlogId(blogId));
        return "/admin/comments :: commentList";
    }


    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session) {
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(blogId));
        User user = (User) session.getAttribute("user");
        if (user != null) {
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
        } else {
            comment.setAvatar(avatar);
        }
        Blog blog = blogService.getBlog(comment.getBlog().getId());
        String blogname = blog.getTitle();

        commentService.saveComment(comment);
        MailUtils.sendHtmlEmail("2193873302@qq.com", comment.getContent(), comment.getNickname(), blogname);
        return "redirect:/comments/" + blogId;
    }


}
