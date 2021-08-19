package com.tian.blog.dao.mapper;

import com.tian.blog.dao.pojo.Category;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMapper {
    /**
     * 根据分类ID查分类
     * @param categoryId
     * @return
     */
    Category findCategoryById(Long categoryId);
}
