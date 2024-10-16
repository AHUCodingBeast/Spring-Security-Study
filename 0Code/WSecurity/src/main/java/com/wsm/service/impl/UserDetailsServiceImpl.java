package com.wsm.service.impl;
import com.wsm.domain.LoginUser;
import com.wsm.domain.User;
import com.wsm.mapper.MenuMapper;
import com.wsm.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    //UserDetailsService 接口实现类,获取用户信息\权限 封装封装成UserDetails对象返回
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(wrapper);
        //如果查询不到数据就通过抛出异常来给出提示
        if(Objects.isNull(user)){ throw new RuntimeException("用户名或密码错误"); }

        //TODO 根据用户查询权限信息 添加到LoginUser中 自定义,测试)
        //手动创建用户的权限数组,封装UserDetails对象;
//        List<String> list = new ArrayList<>(Arrays.asList("test","admin"));
        //自动：从数据库中获取当前用户权限列表
        List<String> list =  menuMapper.selectPermsByUserId(user.getId());
        return new LoginUser(user,list);
    }
}