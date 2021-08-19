package com.tian.blog.service.Impl;

import com.tian.blog.dao.mapper.CategoryMapper;
import com.tian.blog.dao.pojo.Category;
import com.tian.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Category findCategoryById(Long categoryId) {
        return categoryMapper.findCategoryById(categoryId);
    }
}
