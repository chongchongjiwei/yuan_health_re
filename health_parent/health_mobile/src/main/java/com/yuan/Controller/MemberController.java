package com.yuan.Controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.yuan.MemberService;
import com.yuan.constant.MessageConstant;
import com.yuan.entity.Result;
import com.yuan.pojo.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Reference
    MemberService memberService;
    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/login")
    public Result login(HttpServletResponse response, @RequestBody Map map)
    {
        String telephone = (String) map.get("telephone");

        //判断当前用户是否为会员
        Member member = memberService.findByTelephone(telephone);
        if(member == null){
            //当前用户不是会员，自动完成注册
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberService.add(member);
        }
        //登录成功
        //写入Cookie，跟踪用户
        Cookie cookie = new Cookie("login_member_telephone",telephone);
        cookie.setPath("/");//路径
        cookie.setMaxAge(60*60*24*30);//有效期30天
        response.addCookie(cookie);
        //保存会员信息到Redis中
        String json = JSON.toJSON(member).toString();
        jedisPool.getResource().setex(telephone,60*30,json);

        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }
}
