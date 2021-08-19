package com.tian.blog.controller;

import com.tian.blog.dao.mapper.CategoryMapper;
import com.tian.blog.service.CategoryService;
import com.tian.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("categorys")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public Result category(){
        return categoryService.category();
    }
}
