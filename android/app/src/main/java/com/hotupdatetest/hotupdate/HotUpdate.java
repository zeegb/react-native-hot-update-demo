package com.hotupdatetest.hotupdate;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.Properties;

/**
 * Created by wangze on 2017/12/15.
 */

public class HotUpdate {

    public static void checkVersion() {
        // 检查版本是否需要更新
    }

    public static void checkPackage(String filePath) {
        // 1.下载前检查SD卡是否存在更新包文件夹,存在则删除
        Log.i("file-path", filePath);
        File bundleFile = new File(filePath);
        if(bundleFile.exists()) {
            Log.i("file-path", "文件存在");
            bundleFile.delete();
        }
    }

    public static void handleZIP(final Context context) {

        // 开启单独线程，解压，合并。
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 解压到根目录
                FileUtils.decompression(Constants.JS_PATCH_LOCAL_FOLDER);
                // 删除ZIP压缩包
                FileUtils.deleteFile(Constants.JS_PATCH_LOCAL_PATH);
            }
        }).start();
    }
}