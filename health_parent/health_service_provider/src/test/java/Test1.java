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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-dao.xml","classpath:com/yuan/dao/*Dao.xml"})
public class Test1 {
@Autowired
private CheckItemDao checkItemDao ;


    @Test
    public void test1()
    {
//        CheckItem checkItem =new CheckItem();
//        checkItem.setName("hahha");
//        checkItem.setCode("2312312");
//        checkItemDao.add(checkItem);
//        Page<CheckItem> checkItems = checkItemDao.selectByCondition("1000");
//        System.out.println(checkItems.getResult());
    }
}
