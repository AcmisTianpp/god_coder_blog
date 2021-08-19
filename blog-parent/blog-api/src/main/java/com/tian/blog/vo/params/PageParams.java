package com.tian.blog.vo.params;

import lombok.Data;

/**
 * 分页参数
 */
@Data
public class PageParams {

    private int page = 1;

    private int pageSize = 10;

}
