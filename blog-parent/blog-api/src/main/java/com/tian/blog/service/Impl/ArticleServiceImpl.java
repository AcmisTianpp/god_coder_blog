package com.tian.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tian.blog.dao.dos.Archives;
import com.tian.blog.dao.mapper.ArticleMapper;
import com.tian.blog.dao.mapper.TagMapper;
import com.tian.blog.dao.pojo.Article;
import com.tian.blog.dao.pojo.ArticleBody;
import com.tian.blog.dao.pojo.Category;
import com.tian.blog.service.*;
import com.tian.blog.vo.ArticleBodyVo;
import com.tian.blog.vo.ArticleVo;
import com.tian.blog.vo.CategoryVo;
import com.tian.blog.vo.Result;
import com.tian.blog.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ArticleBodyService articleBodyService;
    @Autowired
    private CategoryService categoryService;

    @Override
    public Result listArticle(PageParams pageParams) {
        /**
         * 1.分页查询 article 数据库表
         */
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        // 查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
            // 倒序，先按置顶排序，再按创建时间排序
        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);

        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        List<Article> records = articlePage.getRecords();
        // 不能直接返回，直接返回的是数据库的对象，得返回页面展示的对象
        // 数据转换
        List<ArticleVo> articleVoList = copyList(records,true,true);

        return Result.success(articleVoList);
    }

    // 数据库对象 向 页面展示对象
    // 多个数据 转换的方法
    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor) {
        // 循环
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor));
        }
        return articleVoList;
    }

    // 单个数据转换的方法
    private ArticleVo copy(Article article,boolean isTag,boolean isAuthor){
        ArticleVo articleVo = new ArticleVo();
        // 使用spring提供的类，将数据对象复制到页面展示对象
        BeanUtils.copyProperties(article,articleVo);
        // 只有数据返回类型相同的才复制，不同的单独拿出来复制
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        // 并不是所有的接口 都需要标签和作者信息
        if(isTag){
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if(isAuthor){
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }

        return articleVo;
    }

    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getAuthorId,Article::getTitle);
        queryWrapper.last("limit "+limit);// 注意空格
        //select id,title from article order by view_counts desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result newArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getAuthorId,Article::getTitle);
        queryWrapper.last("limit "+limit);// 注意空格
        //select id,title from article order by CreateDate desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    @Autowired
    private ThreadService threadService;

    @Override
    public Result findArticleById(long articleId) {
        /**
         * 1.根据id查询 文章信息
         * 2.根据bodyId和categoryID 去做关联查询
         */
        Article article = articleMapper.findArticleById(articleId);
        ArticleVo articleVo = copy(article,true,true);

        // 获取文章内容
        Long bodyId = article.getBodyId();
        ArticleBody articleBody = articleBodyService.findArticleBodyById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        BeanUtils.copyProperties(articleBody,articleBodyVo);

        //获取文章分类
        Long categoryId = article.getCategoryId();
        Category category = categoryService.findCategoryById(categoryId);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);

        articleVo.setBody(articleBodyVo);
        articleVo.setCategory(categoryVo);

        //查看完文章了，新增阅读数，有没有问题呢？
        //查看完文章之后，本应该直接返回数据了，这时候做了一个更新操作，更新时加写锁，阻塞其他的读操作，性能就会比较低
        // 更新 增加了此次接口的 耗时 如果一旦更新出问题，不能影响 查看文章的操作
        //线程池  可以把更新操作 扔到线程池中去执行，和主线程就不相关了
        threadService.updateArticleViewCount(articleMapper,article);

        return Result.success(articleVo);
    }
}
