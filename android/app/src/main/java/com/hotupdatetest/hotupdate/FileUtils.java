package com.hotupdatetest.hotupdate;

import android.content.Context;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by wangze on 2017/12/15.
 */

public class FileUtils {

    /**
     * 解压 ZIP 包
     */
    public static void decompression(String filePath) {
        try {
            ZipInputStream inZip = new ZipInputStream(new FileInputStream(Constants.JS_PATCH_LOCAL_PATH));
            ZipEntry zipEntry;
            String szName;
            try {

                while((zipEntry = inZip.getNextEntry()) != null) {

                    szName = zipEntry.getName();
                    if(zipEntry.isDirectory()) {
                        szName = szName.substring(0,szName.length()-1);
                        File folder = new File(filePath + File.separator + szName);
                        folder.mkdirs();
                    }else{
                        File file1 = new File(filePath + File.separator + szName);
                        file1.createNewFile();
                        FileOutputStream fos = new FileOutputStream(file1);
                        int len;
                        byte[] buffer = new byte[1024];
                        while((len = inZip.read(buffer)) != -1) {
                            fos.write(buffer, 0 , len);
                            fos.flush();
                        }
                        fos.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            inZip.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除指定File
     * @param filePath
     */
    public static void deleteFile(String filePath) {
        File patFile = new File(filePath);
        if(patFile.exists()) {
            patFile.delete();
        }
    }

    public static Properties loadConfig(String file) {
        Properties properties = new Properties();
        try {
            FileInputStream s = new FileInputStream(file);
            properties.load(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static void saveConfig(String file, Properties properties) {
        try {
            FileOutputStream s = new FileOutputStream(file, false);
            properties.store(s, "");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}