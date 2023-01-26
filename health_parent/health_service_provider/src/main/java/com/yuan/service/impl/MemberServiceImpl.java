package com.yuan.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.yuan.MemberService;
import com.yuan.dao.MemberDao;
import com.yuan.pojo.Member;
import com.yuan.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberDao memberDao;
    @Override
    public Member findByTelephone(String telephone) {

        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
//        if(member.getPassword() != null){
//            member.setPassword(MD5Utils.md5(member.getPassword()));
//        }
        memberDao.add(member);
    }
}
