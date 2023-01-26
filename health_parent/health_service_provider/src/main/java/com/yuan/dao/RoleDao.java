package com.yuan.dao;

import com.yuan.pojo.Role;

import java.util.Set;

public interface RoleDao {
    public Set<Role> findByUserId(Integer userId);
}
