package com.yuan;

import com.yuan.entity.PageResult;
import com.yuan.entity.QueryPageBean;
import com.yuan.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {

    public void add(CheckItem checkItem);
    public PageResult pageQuery(QueryPageBean queryPageBean);
    public void delete(Integer id);
    public CheckItem findById(Integer id);
    public void edit(CheckItem checkItem);

    public List<CheckItem> findAll();


}
