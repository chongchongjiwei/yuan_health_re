package com.yuan.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.yuan.SetmealService;
import com.yuan.constant.MessageConstant;
import com.yuan.entity.Result;
import com.yuan.pojo.Setmeal;
import com.yuan.utils.TecentCloudUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile file)
    {
        try {
            //获取原始文件名
            String originalFilename = file.getOriginalFilename();
            int lastIndexof = originalFilename.lastIndexOf(".");
            //使用UUID随机产生文件名称
            String suffix= originalFilename.substring(lastIndexof-1);
            String filename= UUID.randomUUID().toString()+suffix;
            String re1= TecentCloudUtils.uploadimg(file.getInputStream(),filename);
            System.out.println(re1);

            //图片上传成功
            Result result = new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,re1);

            return  new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,re1);
        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            return  new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
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
}
