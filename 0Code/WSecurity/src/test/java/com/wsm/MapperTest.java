package com.wsm;

import com.wsm.domain.User;
import com.wsm.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootTest
public class MapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;    //加密/解密对象;

    @Test
    //测试加密解密操作;
    public void TestBCryptPasswordEncoder(){
        //encode 加密操作\matches 判断原密码 密文是否匹配
        String encode1 = passwordEncoder.encode("123456");
        System.out.println(passwordEncoder.matches("123456",encode1));
        String encode2 = passwordEncoder.encode("123456");
        //且每次加密结果不一样,内部有加盐的操作
        System.out.println(encode1);
        System.out.println(encode2);
//        $2a$10$kprE4SNn50BqNHutahc3OO1hIxBl0vc.Dx9GJUMKhJsX7mragu/36
    }

    @Test
    //测试mybatis-plus引入成功
    public void testUserMapper(){
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }
}