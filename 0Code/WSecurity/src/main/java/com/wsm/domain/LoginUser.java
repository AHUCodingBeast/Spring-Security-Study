package com.wsm.domain;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
//@AllArgsConstructor   lombok生成所有的构造函数,因为手动实现了所以不需要生成的了
//LoginUser 自定义 UserDetails对象,内部封装了当前登录用户信息: 获取用户名\密码\是否过期\...
public class LoginUser implements UserDetails {
    private User user;			//当然定义的User类,因为是重写所以暂时都是true,稍后做修改...

    //存储权限信息
    private List<String> permissions;
    public LoginUser(User user,List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    //定义存储权限信息的集合
    @JSONField(serialize = false)
    private List<GrantedAuthority> authorities; //因为其不支持序列化,serialize = false排除序列化

    //重写security 中UserDetails 封装处理用户权限操作函数;
    @Override					//加载用户详细信息，包括用户的权限（或角色）
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //首先判断权限信息是否存在, 存在直接返回;
        if(authorities!=null){ return authorities; }
        //不存在, 根据 UserDetailsService 获取用户信息\权限,传递过来的数据进行解析生成;
        //JAVA8 Stream API 将用户权限集合string数据转换为: GrantedAuthority类型集合,
        //GrantedAuthority 对象：用于表示用户被授予的权限或角色,构造函数接受一个字符串参数
        authorities = permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return authorities;
    }

    @Override					//获取用户密码
    public String getPassword() { return user.getPassword(); }
    @Override					//获取用户名
    public String getUsername() { return user.getUserName(); }
    @Override					//判断用户凭证是否已经过期
    public boolean isCredentialsNonExpired() { return true; }
    @Override					//判断帐号是否已经过期
    public boolean isAccountNonExpired() { return true; }
    @Override					//判断帐号是否已被锁定
    public boolean isAccountNonLocked() { return true; }
    @Override					//用户状态是否有效
    public boolean isEnabled() { return true; }
}