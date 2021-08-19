package com.tian.blog.service;

import com.tian.blog.vo.Result;
import com.tian.blog.vo.params.CommentParam;

public interface CommentsService {
    /**
     * 根据文章ID查询评论
     * @param id
     * @return
     */
    Result commentsByArticleId(Long id);

    /**
     * 评论
     * @param commentParam
     * @return
     */
    Result comment(CommentParam commentParam);
}
