package com.example.demo;

import com.example.demo.util.FileHelper;
import com.example.demo.util.aliyunOSS.AliyunOSSUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import java.nio.file.Paths;

@SpringBootApplication
public class DemoApplication  extends SpringBootServletInitializer{

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DemoApplication.class);
    }

    public static void main(String[] args) {

        SpringApplication.run(DemoApplication.class, args);
//        setConfiguration();
    }

    private static void setConfiguration(){
        // 初始化项目VOC
//        String resource_path = AliyunOSSUtils.resources_path;
        String resource_path = String.valueOf(Paths.get(System.getProperty("user.dir"), "src", "main", "resources"));
        FileHelper helper = new FileHelper(resource_path);
    }
}
