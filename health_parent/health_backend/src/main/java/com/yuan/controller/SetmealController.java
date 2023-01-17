package com.yuan.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.yuan.SetmealService;
import com.yuan.constant.MessageConstant;
import com.yuan.constant.RedisConstant;
import com.yuan.entity.PageResult;
import com.yuan.entity.QueryPageBean;
import com.yuan.entity.Result;
import com.yuan.pojo.Setmeal;
import com.yuan.utils.TecentCloudUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile file)
    {
        try {
            //获取原始文件名
            String originalFilename = file.getOriginalFilename();
            //使用UUID随机产生文件名称
            String test = "."+ FilenameUtils.getExtension(originalFilename);
            String filename= UUID.randomUUID().toString()+test;

            String re1= TecentCloudUtils.uploadimg(file.getInputStream(),filename);
            System.out.println(re1);

            //图片上传成功
            Result result = new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,re1);
            //将上传图片名称存入Redis，基于Redis的Set集合存储
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,re1);

            return  new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,re1);
        }catch (Exception e)
        {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    //新增
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        try {
            setmealService.add(setmeal,checkgroupIds);
        }catch (Exception e){
            //新增套餐失败
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
        //新增套餐成功
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = setmealService.pageQuery(queryPageBean);
        return pageResult;
    }

}
