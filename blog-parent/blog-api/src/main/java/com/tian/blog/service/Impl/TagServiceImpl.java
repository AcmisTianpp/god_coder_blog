package com.tian.blog.service.Impl;

import com.tian.blog.dao.mapper.TagMapper;
import com.tian.blog.dao.pojo.Tag;
import com.tian.blog.service.TagService;
import com.tian.blog.vo.Result;
import com.tian.blog.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<TagVo> findTagsByArticleId(Long articleId) {
        // mybatis plus 无法进行多表查询
        List<Tag> tags = tagMapper.findTagsByArticleId(articleId);
        return copyList(tags);
    }

    // 数据库对象 向 页面展示对象
    // 多个数据 转换的方法
    private List<TagVo> copyList(List<Tag> records) {
        // 循环
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag record : records) {
            tagVoList.add(copy(record));
        }
        return tagVoList;
    }

    // 单个数据转换的方法
    private TagVo copy(Tag tag) {
        TagVo tagVo = new TagVo();
        // 使用spring提供的类，将数据对象复制到页面展示对象
        BeanUtils.copyProperties(tag, tagVo);
        return tagVo;
    }

    @Override
    public Result hots(int limit) {
        /*
         * 1. 标签所拥有的文章数最多，最热标签
         * 2. 查询 根据 tag_id 分组 计数，从小到大排序，取前 limit 个
         */
        List<Long> tagIds = tagMapper.findHotsTagIds(limit);
        // 判空
        if (CollectionUtils.isEmpty(tagIds)) {
            return Result.success(Collections.emptyList());
        }
        // 最终需要的是包含 tagId 和 tagName 的 tag 对象
        // select * from tag where id in (1,2,3)
        List<Tag> tagList = tagMapper.findTagsByTagIds(tagIds);
        return Result.success(tagList);
    }
}
