package com.tian.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tian.blog.dao.pojo.Category;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMapper extends BaseMapper<Category> {
    /**
     * 根据分类ID查分类
     * @param categoryId
     * @return
     */
    Category findCategoryById(Long categoryId);
}
