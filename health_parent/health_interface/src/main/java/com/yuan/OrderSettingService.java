package com.yuan;

import com.yuan.pojo.OrderSetting;

import java.util.List;
import java.util.Map;


public interface OrderSettingService {
    public void add(List<OrderSetting> list);

//    获取当月预约人数
    public List<Map> getOrderSettingByMonth(String date);

    public void editNumberByDate(OrderSetting orderSetting);
}
