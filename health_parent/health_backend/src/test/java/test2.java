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
}
