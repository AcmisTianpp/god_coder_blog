package com.tian.blog.vo.params;

import lombok.Data;

/**
 * 创建评论的参数
 */

@Data
public class CommentParam {

    private Long articleId;

    private String content;

    // 存父评论的 id
    private Long parent;

    // 被评论的用户 id
    private Long toUserId;
}
