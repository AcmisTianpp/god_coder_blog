package com.tian.blog.service;

import com.tian.blog.dao.pojo.ArticleBody;

public interface ArticleBodyService {
    /**
     * 获取文章内容
     * @param bodyId
     * @return
     */
    ArticleBody findArticleBodyById(Long bodyId);
}
