package com.yuan;

import com.yuan.entity.PageResult;
import com.yuan.entity.QueryPageBean;
import com.yuan.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    //分页查询
    public PageResult pageQuery(QueryPageBean queryPageBean);

    //编辑
    CheckGroup findById(Integer id);
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);
    public void edit(CheckGroup checkGroup,Integer[] checkitemIds);

    //套餐相关
    //查找所有组
    List<CheckGroup> findAll();


}
