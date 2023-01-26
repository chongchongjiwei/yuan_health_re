package com.yuan.dao;

import com.yuan.pojo.Permission;

import java.util.Set;

public interface PermissionDao {
    public Set<Permission> findByRoleId(Integer roleId);
}
