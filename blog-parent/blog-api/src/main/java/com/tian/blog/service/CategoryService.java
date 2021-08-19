package com.tian.blog.service;

import com.tian.blog.dao.pojo.Category;
import com.tian.blog.vo.Result;

public interface CategoryService {
    Category findCategoryById(Long categoryId);

    Result category();
}
