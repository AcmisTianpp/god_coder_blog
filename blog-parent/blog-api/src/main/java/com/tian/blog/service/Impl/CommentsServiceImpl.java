package com.tian.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tian.blog.dao.mapper.ArticleMapper;
import com.tian.blog.dao.mapper.CommentMapper;
import com.tian.blog.dao.mapper.SysUserMapper;
import com.tian.blog.dao.pojo.Comment;
import com.tian.blog.dao.pojo.SysUser;
import com.tian.blog.service.CommentsService;
import com.tian.blog.service.SysUserService;
import com.tian.blog.utils.UserThreadLocal;
import com.tian.blog.vo.UserVo;
import com.tian.blog.vo.CommentVo;
import com.tian.blog.vo.Result;
import com.tian.blog.vo.params.CommentParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public Result commentsByArticleId(Long id) {
        /**
         * 1.根据文章ID查询评论列表，从comment表中查询
         * 2.根据作者的id 查询作者的信息
         * 3.判断 如果level=1，要判断有没有子评论（本项目只设置两级评论）
         * 4.如果有子评论，根据id进行查询 (parent_id)
         */
        /*
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId,id);
        queryWrapper.eq(Comment::getLevel,1);
        List<Comment> commentList = commentMapper.selectList(queryWrapper);*/
        List<Comment> commentList = commentMapper.commentsByArticleId(id);
        List<CommentVo> commentVoList = copyList(commentList);
        return Result.success(commentVoList);
    }

    // 数据库对象 向 页面展示对象
    // 多个数据 转换的方法
    private List<CommentVo> copyList(List<Comment> records) {
        // 循环
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment record : records) {
            commentVoList.add(copy(record));
        }
        return commentVoList;
    }

    // 单个数据转换的方法
    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        // 使用spring提供的类，将数据对象复制到页面展示对象
        BeanUtils.copyProperties(comment, commentVo);

        // 其他属性
        //作者信息
        Long authorId = comment.getAuthorId();
        UserVo author = sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(author);
        //子评论
        Integer level = comment.getLevel();
        if (1 == level) {
            List<CommentVo> commentVoList = findCommentsById(comment.getId());
            commentVo.setChildrens(commentVoList);
        }
        // to User 给谁评论
        if (comment.getLevel() > 1) {
            Long toUid = comment.getToUid();
            UserVo toUserVo = sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUserVo);
        }
        return commentVo;
    }

    // 通过 parent_id 查子评论
    private List<CommentVo> findCommentsById(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId, id);
        queryWrapper.eq(Comment::getLevel, 2);
        return copyList(commentMapper.selectList(queryWrapper));
    }

    @Override
    public Result comment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        Long parent = commentParam.getParent();
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        // 评论分级
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        } else {
            comment.setLevel(2);
        }
        commentMapper.insert(comment);
        return Result.success(null);
    }
}
