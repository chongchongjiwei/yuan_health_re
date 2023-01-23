package com.yuan.dao;

import com.github.pagehelper.Page;
import com.yuan.entity.PageResult;
import com.yuan.entity.QueryPageBean;
import com.yuan.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    public void add(Setmeal setmeal);
    public void setSetmealAndCheckGroup(Map<String, Integer> map);

    public Page<Setmeal> selectByCondition(String queryString);

    //删除套餐和检查组关系
    public void delete_Association_Checkgroup(Integer id);

    //删除检查组本身
    public void delete_self(Integer id);

    //查询所有套餐信息
    public List<Setmeal> findAll();

    public Setmeal findById(int id);
}
