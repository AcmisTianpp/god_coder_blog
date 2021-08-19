package com.tian.blog.service;

import com.tian.blog.vo.Result;
import com.tian.blog.vo.TagVo;

import java.util.List;

public interface TagService {

    List<TagVo> findTagsByArticleId(Long articleId);

    Result hots(int limit);

}
