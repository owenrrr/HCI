package com.example.demo.util.aliyunOSS;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;

import java.io.*;

/**
 * @Author: Owen
 * @Date: 2021/6/7 19:55
 * @Description:
 */
public class AliyunOSSUtils {
    /**
     * 阿里云的配置参数
     */
    private static String accessKeyId = null;
    private static String accessKeySecret = null;
    private static String endpoint = null;
    private static String bucketName = null;
    /**
     * 存储在OSS中的前缀名
     */
    private static String file_prefix = null;

    /**
     * 项目resources绝对路径
     */
    public static String resources_path = null;


    /**
     * 静态块
     */
    static {
        //初始化AccessKey
        accessKeyId = PropertyReader.get("aliyun.AccessKeyID");
        //初始化AccessKeySecret
        accessKeySecret = PropertyReader.get("aliyun.AccessKeySecret");
        //初始化Endpoint
        endpoint = PropertyReader.get("aliyun.EndPoint");
        //初始化bucketName
        bucketName = PropertyReader.get("aliyun.Buckets");
        //初始化前缀
        file_prefix = PropertyReader.get("aliyun.prefix");
        // resources path(跟阿里云没关系)
        resources_path = PropertyReader.get("project.resources");
    }

    /**
     * 私有化构造
     */
    private AliyunOSSUtils() {

    }

    /**
     * 获取图片的URL头信息
     *
     * @return 返回url头信息
     */
    private static String getURLHead() {
        //从哪个位置截取
        int cutPoint = endpoint.lastIndexOf('/') + 1;
        //http头
        String head = endpoint.substring(0, cutPoint);
        //服务器地址信息
        String tail = endpoint.substring(cutPoint);
        //返回结果
        return head + bucketName + "." + tail + "/";
    }

    /**
     * 获取存储在服务器上的地址
     *
     * @param oranName 文件名
     * @return 文件URL
     */
    private static String getRealName(String oranName) {
        return getURLHead() + oranName;
    }

    /**
     * 下载文件(流式下载)
     *
     * @param filepath 文件路径
     * @return 文件流
     */
    public static void getBufferedReader(String filepath) {
        BufferedReader reader = null;
        OSS ossClient = null;
        try {
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            OSSObject ossObject = ossClient.getObject(bucketName, filepath);
            reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent()));

            while (true) {
                String line = reader.readLine();
                if (line == null) break;

                System.out.println("\n" + line);
            }

            reader.close();

            ossClient.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /***
     * 下载文件（下载到本地）
     *
     * @param filepath OSS路径
     * @param dst 本地路径
     * @return
     */
    public static void getSpecifiedFile(String filepath, String dst) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        System.out.println(filepath + "\n" + dst);
        ossClient.getObject(new GetObjectRequest(bucketName, filepath), new File(dst));

        ossClient.shutdown();
    }

    /**
     * 下载文件夹
     *
     * @param dir OSS路径(prefix)
     * @return
     */
    public static void getSpecifiedDirectory(String dir) {

        String dstPath = "";

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        ObjectListing objectListing = ossClient.listObjects(bucketName, dir);
        // objectListing.getObjectSummaries获取所有文件的描述信息。
        for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            dstPath = objectSummary.getKey();
            System.out.println(dstPath);
            File file = new File("src/main/" + dstPath);
            ossClient.getObject(new GetObjectRequest(bucketName, dstPath), file);
        }

        ossClient.shutdown();
    }

}
