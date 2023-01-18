package com.yuan.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yuan.SetmealService;
import com.yuan.constant.RedisConstant;
import com.yuan.dao.SetmealDao;
import com.yuan.entity.PageResult;
import com.yuan.entity.QueryPageBean;
import com.yuan.pojo.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

import static com.yuan.constant.RedisConstant.SETMEAL_PIC_RESOURCES;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    SetmealDao setmealDao;
    @Autowired
    private JedisPool jedisPool;
    //新增套餐
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.add(setmeal);

        if(checkgroupIds != null && checkgroupIds.length > 0){
            //绑定套餐和检查组的多对多关系
            setSetmealAndCheckGroup(setmeal.getId(),checkgroupIds);
        }
        //如果这个套餐真的加入的数据库，那么 对应的图片名称也要加入，但是是另一个redis表
        savePic2Redis(setmeal.getImg());
    }

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        PageHelper.startPage( queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<Setmeal> page = setmealDao.selectByCondition(queryPageBean.getQueryString());
        return  new PageResult(page.getTotal(),page.getResult());

    }



    //绑定套餐和检查组的多对多关系
    private void setSetmealAndCheckGroup(Integer id, Integer[] checkgroupIds) {
        for (Integer checkgroupId : checkgroupIds) {
            Map<String,Integer> map = new HashMap<>();
            map.put("setmeal_id",id);
            map.put("checkgroup_id",checkgroupId);
            setmealDao.setSetmealAndCheckGroup(map);
        }
    }

    //将图片名称保存到Redis
    private void savePic2Redis(String pic){

        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,pic);
    }

    @Override
    public void deleteSetmeal(Integer id,String img) {
        //删除套餐和检查组的关系
        setmealDao.delete_Association_Checkgroup(id);
        //删除本身
        setmealDao.delete_self(id);
        //删除redis数据库
        jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES,img);

    }
}
