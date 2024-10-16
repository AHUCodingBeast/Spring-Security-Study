package com.wsm.service;

import com.wsm.domain.ResponseResult;
import com.wsm.domain.User;

public interface LoginServcie {
    //定义登录函数：
    ResponseResult login(User user);
    //退出登录函数;
    ResponseResult logout();
}
