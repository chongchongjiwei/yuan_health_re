package com.yuan;

import com.yuan.pojo.Member;

public interface MemberService {

    public Member findByTelephone(String telephone);
    public void  add(Member member);
}
