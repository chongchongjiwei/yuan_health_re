package com.yuan;

import com.yuan.entity.Result;

import java.util.Map;

public interface OrderService {
    //体检预约
    public Result order(Map map) throws Exception;

    public Map findById(Integer id) throws Exception;
}
