package com.tian.blog.service.Impl;

import com.tian.blog.dao.mapper.ArticleBodyMapper;
import com.tian.blog.dao.pojo.ArticleBody;
import com.tian.blog.service.ArticleBodyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleBodyServiceImpl implements ArticleBodyService {

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    @Override
    public ArticleBody findArticleBodyById(Long bodyId) {
        return articleBodyMapper.findArticleBodyById(bodyId);
    }
}
