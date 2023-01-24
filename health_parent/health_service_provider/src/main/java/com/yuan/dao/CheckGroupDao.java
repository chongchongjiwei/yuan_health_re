package com.yuan.dao;

import com.github.pagehelper.Page;
import com.yuan.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {
    void add(CheckGroup checkGroup);
    void setCheckGroupAndCheckItem(Map map);
    public Page<CheckGroup> selectByCondition(String queryString);

    CheckGroup findById(Integer id);
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

//    删除和检查项的联系
    void deleteAssociation(Integer id);
    void edit(CheckGroup checkGroup);

    List<CheckGroup> findAll();

    //删除检查组和套餐的联系
    public void deleteAssociation_Setmeal(Integer id);
    //删除检查组
    public void deleteSelf(Integer id);
}
