package com.yuan.job;

import com.yuan.constant.RedisConstant;
import com.yuan.utils.TecentCloudUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;
    public void clearImg(){
        //根据Redis中保存的两个set集合进行差值计算，获得垃圾图片名称集合
        Set<String> set =
                jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES,
                        RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if(set != null){
            for (String picName : set) {
                System.out.println(picName);
                //腾讯云删除
                TecentCloudUtils.deleteFile(picName);
                //从Redis集合中删除图片名称
                jedisPool.getResource().
                        srem(RedisConstant.SETMEAL_PIC_RESOURCES,picName);
            }
        }
        System.out.println("清理多余的图片文件");
    }
}
