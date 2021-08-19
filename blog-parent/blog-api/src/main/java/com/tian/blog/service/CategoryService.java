package com.tian.blog.service;

import com.tian.blog.dao.pojo.Category;

public interface CategoryService {
    Category findCategoryById(Long categoryId);
}
