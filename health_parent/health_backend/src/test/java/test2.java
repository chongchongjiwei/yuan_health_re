import com.yuan.utils.TecentCloudUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import org.junit.runner.RunWith;


public class test2 {
    @Test
    public void t1()
    {
        String a ="马甲加上bug护甲.jpg";
        System.out.println(FilenameUtils.getExtension(a));
    }

    //桶里的图片删除
    @Test
    public void t2()
    {
        String key ="img/48cf5f0c-24bd-402a-899f-269df50ee2ac.jpg";
        TecentCloudUtils.deleteFile(key);
    }

}
