package com.tian.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tian.blog.dao.mapper.CategoryMapper;
import com.tian.blog.dao.pojo.Category;
import com.tian.blog.service.CategoryService;
import com.tian.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Category findCategoryById(Long categoryId) {
        return categoryMapper.findCategoryById(categoryId);
    }

    @Override
    public Result category() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Category::getId,Category::getAvatar,Category::getCategoryName);
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        return Result.success(categories);
    }
}
