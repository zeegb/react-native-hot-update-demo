package com.hotupdatetest.hotupdate;

import android.os.Environment;

import com.hotupdatetest.MainApplication;

import java.io.File;

/**
 * Created by wangze on 2017/12/15.
 */

public class Constants {
    /**
     * zip的文件名
     */
    public static final String ZIP_NAME = "android-bundle";

    /**
     * 配置文件的文件名
     */
    public static final String CONFIG_NAME = "version.dat";

    /**
     * bundle文件名
     */
    public static final String JS_BUNDLE_LOCAL_FILE = "index.android.bundle";

    /**
     * 第一次解压zip后的文件目录
     */
    public static final String JS_PATCH_LOCAL_FOLDER = Environment.getExternalStorageDirectory().toString()
            + File.separator + MainApplication.getInstance().getAppPackageName();


    public static final String LOCAL_FOLDER = JS_PATCH_LOCAL_FOLDER + "/" + ZIP_NAME;

    public static final String LOCAL_CONFIG_PATH = JS_PATCH_LOCAL_FOLDER + "/" + CONFIG_NAME;

    /**
     * zip文件
     */
    public static final String JS_PATCH_LOCAL_PATH = JS_PATCH_LOCAL_FOLDER + File.separator + ZIP_NAME + ".zip";

    /**
     * 合并后的bundle文件保存路径
     */
    public static final String JS_BUNDLE_LOCAL_PATH = JS_PATCH_LOCAL_FOLDER +File.separator + ZIP_NAME + File.separator + JS_BUNDLE_LOCAL_FILE;

    /**
     * 下载URL
     */
    public static final String JS_BUNDLE_REMOTE_URL = "http://119.29.244.84:9091/android-bundle.zip";
}
