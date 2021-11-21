package com.example.demo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Author: Owen
 * @Date: 2021/6/18 20:35
 * @Description: set configuration
 */
public class FileHelper {

    private String prefix = "";

    public FileHelper(String prefix){
        this.prefix = prefix;

        setDFVoc2Voc();
    }

    private void setDFVoc2Voc(){
        String sourcePath = this.prefix + "default-voc";
        String newPath = this.prefix + "voc";
        try{
            copyDir(sourcePath, newPath);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void copyDir(String sourcePath, String newPath) throws IOException {
        File file = new File(sourcePath);
        String[] filePath = file.list();

        if (!(new File(newPath)).exists()) {
            (new File(newPath)).mkdir();
        }

        for (int i = 0; i < filePath.length; i++) {
            if ((new File(sourcePath + file.separator + filePath[i])).isDirectory()) {
                copyDir(sourcePath + file.separator + filePath[i], newPath + file.separator + filePath[i]);
            }
            if (new File(sourcePath + file.separator + filePath[i]).isFile()) {
                copyFile(sourcePath + file.separator + filePath[i], newPath + file.separator + filePath[i]);
            }
        }
    }

    public void copyFile(String oldPath, String newPath) throws IOException {
        File oldFile = new File(oldPath);
        File file = new File(newPath);
        FileInputStream in = new FileInputStream(oldFile);
        FileOutputStream out = new FileOutputStream(file);;

        byte[] buffer=new byte[2097152];
        int readByte = 0;
        while((readByte = in.read(buffer)) != -1){
            out.write(buffer, 0, readByte);
        }
        in.close();
        out.close();
    }
}
