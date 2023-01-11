package com.example.blog.web.admin;

import com.UpYun;
import com.example.blog.config.UpYunConfig;
import com.example.blog.dto.BaseUserInfo;
import com.example.blog.po.Blog;
import com.example.blog.po.User;
import com.example.blog.service.BlogService;
import com.example.blog.service.CommentService;
import com.example.blog.service.TagService;
import com.example.blog.service.TypeService;
import com.example.blog.util.CodeCreateUtils;
import com.example.blog.vo.BlogQuery;
import com.example.blog.vo.BlogVo;
import com.upyun.UpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;


@Controller
@RequestMapping("/admin")
public class BlogController extends BaseController {

    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "admin/blogs-input";
    private static final String COMMENT = "admin/comments";

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UpYunConfig upYunConfig;

    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, Model model,
                        BlogQuery blog) {

        model.addAttribute("types", typeService.listType());
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return LIST;
    }

    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable, Model model, BlogQuery blog) {
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/blogs :: blogList";
    }


    @GetMapping("/blogs/input")
    public String input(Model model) {
        setTypeAndTag(model);
        model.addAttribute("blog", new Blog());
        return INPUT;
    }

    private void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
    }

    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        setTypeAndTag(model);
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog", blog);
        return INPUT;
    }

    //新增&更新博客
    @PostMapping("/blogs")
    public String post(Blog blog, @RequestParam("file00") MultipartFile file, RedirectAttributes attributes, HttpSession session) throws IOException, UpException {

        String firstPicture = blog.getFirstPicture();
        //如果file不为空，则上传图片到upyun
        if (!file.isEmpty()) {
            UpYun upYun = new UpYun(upYunConfig.getBucketName(), upYunConfig.getUsername(), upYunConfig.getPassword());
            String format = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            String name = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
            String fileName = "/blog/" +name + CodeCreateUtils.get4Code(4) + "." + format;
            boolean b1 = upYun.writeFile(fileName, file.getInputStream(), true, new HashMap<>());
            if (!b1) {
                attributes.addFlashAttribute("message", "新增失败");
                return "redirect:/admin/blogs";
            }
             firstPicture ="http://" + upYunConfig.getBucketName() + "." + upYunConfig.getHostName() + "/" + fileName;
        }


        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        blog.setFirstPicture(firstPicture);

        Blog b;
        if (blog.getFlag().equals("")) {
            blog.setFlag("原创");
        }
        if (blog.getId() == null) {
            b = blogService.saveBlog(blog);
        } else {
            b = blogService.updateBlog(blog.getId(), blog);
        }

        if (b == null) {
            attributes.addFlashAttribute("message", "新增失败");
        } else {
            attributes.addFlashAttribute("message", "新增成功");
        }
        return "redirect:/admin/blogs";
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        System.out.println(id);
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除博客成功");
        return "redirect:/admin/blogs";
    }

    @GetMapping("/blogs/{id}/comments")
    public String comment(@PageableDefault(size = 3, sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable, @PathVariable Long id, Model model) {
        model.addAttribute("blog", blogService.getAndConvert(id));
        model.addAttribute("page", commentService.listCommentByBlogId(id, pageable));
        return COMMENT;
    }

    @GetMapping("/blogs/{id}/comment2")
    public String delete2(@PathVariable Long id, Long blogId, RedirectAttributes attributes) {
        commentService.deleteComment(id);
        attributes.addFlashAttribute("message", "删除评论成功");
        return "redirect:/admin/blogs/" + blogId + "/comments";
    }

}
