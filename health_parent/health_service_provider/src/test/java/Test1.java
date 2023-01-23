import com.github.pagehelper.Page;
import com.yuan.dao.CheckItemDao;

import com.yuan.pojo.CheckItem;
import com.yuan.pojo.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-dao-test.xml","classpath:com/yuan/dao/*Dao.xml"})
public class Test1 {
@Autowired
private CheckItemDao checkItemDao ;


//查询远程数据库
    @Test
    public void test1()
    {
//        CheckItem checkItem =new CheckItem();
//        checkItem.setName("hahha");
//        checkItem.setCode("2312312");
//        checkItemDao.add(checkItem);
//        Page<CheckItem> checkItems = checkItemDao.selectByCondition("1000");
//        System.out.println(checkItems.getResult());
        List<CheckItem> test= checkItemDao.findAll();

        for (CheckItem checkItem : test) {
            System.out.println(checkItem.toString());
        }
        System.out.println("这是test_setmeal分支写的");
    }
}
