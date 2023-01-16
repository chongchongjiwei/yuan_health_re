package com.yuan.dao;

import com.yuan.pojo.CheckGroup;

import java.util.Map;

public interface CheckGroupDao {
    void add(CheckGroup checkGroup);
    void setCheckGroupAndCheckItem(Map map);
}
