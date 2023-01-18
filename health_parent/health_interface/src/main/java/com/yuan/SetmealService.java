package com.yuan;

import com.yuan.entity.PageResult;
import com.yuan.entity.QueryPageBean;
import com.yuan.pojo.Setmeal;

public interface SetmealService {
    public void add(Setmeal setmeal, Integer[] checkgroupIds);
    public PageResult pageQuery(QueryPageBean queryPageBean);

    public void deleteSetmeal(Integer id,String img);

}
