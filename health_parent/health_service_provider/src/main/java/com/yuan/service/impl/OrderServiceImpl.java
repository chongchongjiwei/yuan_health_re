package com.yuan.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.yuan.OrderService;
import com.yuan.constant.MessageConstant;
import com.yuan.dao.MemberDao;
import com.yuan.dao.OrderDao;
import com.yuan.dao.OrderSettingDao;
import com.yuan.entity.Result;
import com.yuan.pojo.Member;
import com.yuan.pojo.Order;
import com.yuan.pojo.OrderSetting;
import com.yuan.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

//体检预约服务
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;


    @Override
    public Result order(Map map) throws Exception {

        String orderDate = (String) map.get("orderDate");

        //将字符串转为日期对象
        Date date = DateUtils.parseString2Date(orderDate);
//        查找日期看是否存在
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
        if(orderSetting == null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }



        //检查预约日期是否预约已满
        int number = orderSetting.getNumber();//可预约人数
        int reservations = orderSetting.getReservations();//已预约人数
        if(reservations >= number){
            //预约已满，不能预约
            return new Result(false,MessageConstant.ORDER_FULL);
        }
        //检查当前用户是否为会员，根据手机号判断
        String telephone = (String) map.get("telephone");

        Member member = memberDao.findByTelephone(telephone);

        //防止重复预约，查询预约表，看是否有数据，有则
        if(member != null){
//            通过上面查询到的对象获取它的id
            Integer memberId = member.getId();
            int setmealId = Integer.parseInt((String) map.get("setmealId"));
//            封装一个对象，包含预约人的id，预约的套餐id，套餐id是前端当前前端传进来的
            Order order = new Order(memberId,date,null,null,setmealId);
//            查看是否能找到，找到说明已经预约过了

//            DAO
            List<Order> list = orderDao.findByCondition(order);


            if(list != null && list.size() > 0){
                //已经完成了预约，不能重复预约
                return new Result(false,MessageConstant.HAS_ORDERED);
            }


        }

        //可以预约，设置预约人数加一
        orderSetting.setReservations(orderSetting.getReservations()+1);

        orderSettingDao.editReservationsByOrderDate(orderSetting);
        if(member == null){
            //当前用户不是会员，需要添加到会员表
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }

        //保存预约信息到预约表
        Order order = new Order(member.getId(),
                date,
                (String)map.get("orderType"),
                Order.ORDERSTATUS_NO,
                Integer.parseInt((String) map.get("setmealId")));
        orderDao.add(order);

        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    @Override
    public Map findById(Integer id) throws Exception
    {
        Map map = orderDao.findById4Detail(id);
        if(map != null){
            //处理日期格式
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
        }
        return map;

    }
}
