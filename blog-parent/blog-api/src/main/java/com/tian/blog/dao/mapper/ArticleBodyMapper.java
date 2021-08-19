package com.tian.blog.dao.mapper;

import com.tian.blog.dao.pojo.ArticleBody;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleBodyMapper {
    /**
     * 根据内容Id查文章内容
     * @param bodyId
     * @return
     */
    ArticleBody findArticleBodyById(Long bodyId);
}
