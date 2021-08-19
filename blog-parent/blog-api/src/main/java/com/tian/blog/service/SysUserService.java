package com.tian.blog.service;

import com.tian.blog.dao.pojo.SysUser;
import com.tian.blog.vo.Result;
import com.tian.blog.vo.UserVo;

public interface SysUserService {

    SysUser findUserById(long id);

    UserVo findUserVoById(Long id);

    SysUser findUser(String account, String password);

    /**
     * 根据账户查找用户
     * @param account
     * @return
     */
    SysUser findUserByAccount(String account);

    void save(SysUser sysUser);

    /**
     * 根据 token 查询用户信息
     * @param token
     * @return
     */
    Result findUserByToken(String token);
}
