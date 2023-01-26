package com.yuan.dao;

import com.yuan.pojo.User;

public interface UserDao {
    public User findByUsername(String username);
}
