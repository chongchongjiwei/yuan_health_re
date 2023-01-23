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
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yuan.constant.RedisConstant.SETMEAL_PIC_RESOURCES;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    //注入静态页面类
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

//    值的注入
    @Value("${out_put_path}")//从属性文件读取输出目录的路径
    private String outputpath ;

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

//       静态页面处理
        generateMobileStaticHtml();

    }

    //生成静态页面
    public void generateMobileStaticHtml() {
        //准备模板文件中所需的数据
        List<Setmeal> setmealList = this.findAll();
        //生成套餐列表静态页面
        generateMobileSetmealListHtml(setmealList);
        //生成套餐详情静态页面（多个）
        generateMobileSetmealDetailHtml(setmealList);
    }

    //生成套餐列表静态页面
    public void generateMobileSetmealListHtml(List<Setmeal> setmealList) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("setmealList", setmealList);
        this.generateHtml("mobile_setmeal.ftl","m_setmeal.html",dataMap);
    }

    //生成套餐详情静态页面（多个）
    public void generateMobileSetmealDetailHtml(List<Setmeal> setmealList) {
        for (Setmeal setmeal : setmealList) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("setmeal", this.findById(setmeal.getId()));
            this.generateHtml("mobile_setmeal_detail.ftl",
                    "setmeal_detail_"+setmeal.getId()+".html",
                    dataMap);
        }
    }

    public void generateHtml(String templateName,String htmlPageName,Map<String, Object> dataMap){
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer out = null;
        try {
            // 加载模版文件
            Template template = configuration.getTemplate(templateName);
            template.setEncoding("UTF-8");
            // 生成数据
            File docFile = new File(outputpath + "\\" + htmlPageName);
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile),"UTF-8"));
            // 输出文件
            template.process(dataMap, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.flush();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

//    套餐条件查询
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
//        刷新静态页面
        generateMobileStaticHtml();
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();

    }
    public Setmeal findById(int id) {
        return setmealDao.findById(id);
    }
}
