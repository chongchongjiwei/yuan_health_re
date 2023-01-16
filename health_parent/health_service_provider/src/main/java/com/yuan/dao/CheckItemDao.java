package com.yuan.dao;

import com.github.pagehelper.Page;
import com.yuan.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {
    public  void add(CheckItem checkItem);
    public Page<CheckItem> selectByConditon(String queryString);


    public void  deleteById(Integer id);
    public long findCountByCheckItemId(Integer checkitem_id);

    //编辑
    public void edit(CheckItem checkItem);
    public CheckItem findById(Integer id);

    public List<CheckItem> findAll();

}
