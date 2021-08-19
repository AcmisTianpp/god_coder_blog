package com.tian.blog.vo.params;

import lombok.Data;

/**
 * 登录参数
 */
@Data
public class LoginParam {

    private String account;

    private String password;

    private String nickname;
}
