package com.wsm.service.impl;
import com.wsm.domain.LoginUser;
import com.wsm.domain.User;
import com.wsm.domain.ResponseResult;
import com.wsm.service.LoginServcie;
import com.wsm.utils.JwtUtil;
import com.wsm.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Objects;

@Service
//LoginServcieImpl LoginServcie 的实现类;
public class LoginServcieImpl implements LoginServcie {
    @Autowired  //AuthenticationManager是负责处理用户认证的核心组件;
    private AuthenticationManager authenticationManager;
    @Autowired  //Redis 操作工具类;
    private RedisCache redisCache;

    @Override
    //实现登录函数逻辑：
    public ResponseResult login(User user) {
        //AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        //认证成功，提供者返回一个填充了用户详细信息和授权信息的新Authentication对象
        //这个对象随后被Spring Security框架接受并存储在SecurityContextHolder中，表示用户当前的认证状态
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断当前认证成功、失败, 抛出接口异常;
        if(Objects.isNull(authenticate)){ throw new RuntimeException("用户名或密码错误"); }
        //使用userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //authenticate存入redis
        redisCache.setCacheObject("login:"+userId,loginUser);
        //把token响应给前端
        HashMap<String,String> map = new HashMap<>();
        map.put("token",jwt);
        return new ResponseResult(200,"登陆成功",map);
    }

    @Override
    public ResponseResult logout() {
        //获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
        //删除redis中的值
        redisCache.deleteObject("login:"+userid);
        return new ResponseResult(200,"注销成功");
    }
}