package com.tian.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tian.blog.dao.dos.Archives;
import com.tian.blog.dao.pojo.Article;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 文章归档
     * @return
     */
    List<Archives> listArchives();

    /**
     * 根据文章Id查找对应的文章
     * @param articleId
     * @return
     */
    Article findArticleById(long articleId);
}
