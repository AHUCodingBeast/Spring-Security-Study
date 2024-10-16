package com.wsm.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    //定义一个简单接口：
    @RequestMapping("/hello")
//    @PreAuthorize("hasAuthority('test')")
//    @PreAuthorize("hasAuthority('system:dept:index')")
    @PreAuthorize("@diy.hasAuthority('system:dept:index')")
    //测试案例： hasAuthority('xx'); Security中的一个表达式,检查当前经过认证的用户是否拥有指定的权限;
    public String hello(){ return "hello world"; }
}