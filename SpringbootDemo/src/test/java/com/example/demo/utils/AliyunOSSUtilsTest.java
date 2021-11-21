package com.example.demo.utils;

import com.example.demo.util.aliyunOSS.AliyunOSSUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

import static org.junit.Assert.assertTrue;

/**
 * @Author: Owen
 * @Date: 2021/6/7 21:08
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AliyunOSSUtilsTest {

    @Test
    public void downloadFile(){
        String src = "resources/jar/hanlp-1.8.1.jar";
        String dst = "src/main/resources/target.jar";

        AliyunOSSUtils.getSpecifiedFile(src, dst);

        File file = new File(dst);
        if (file.exists()){
            file.delete();
            assertTrue(true);
        }else{
            assertTrue(false);
        }
    }

    /**
     * 不要运行
     */
//    @Test
//    public void downloadMutiFiles(){
//        String src = "resources/data";
//
//        AliyunOSSUtils.getSpecifiedDirectory(src);
//    }
}
