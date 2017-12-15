package com.hotupdatetest;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.facebook.react.ReactActivity;
import com.hotupdatetest.hotupdate.AsyncNetUtils;
import com.hotupdatetest.hotupdate.Constants;
import com.hotupdatetest.hotupdate.FileUtils;
import com.hotupdatetest.hotupdate.HotUpdate;
import com.hotupdatetest.hotupdate.HttpUtils;
import com.hotupdatetest.hotupdate.OnResponseListner;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class MainActivity extends ReactActivity {

    private long mDownLoadId;
    private int currentBundleVersion;
    private String currentUrl;
    private CompleteReceiver localReceiver;

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "HotUpdateTest";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registeReceiver();
        Log.i("tag-create:", "create");
        checkVersion();
    }

    /**
     * 检查版本号
     */
    private void checkVersion() {
        Log.i("checkVersion", "checkVersion");
        AsyncNetUtils.get("http://11.11.11.11:9999/vest/getCurrentVersion/ios", new AsyncNetUtils.Callback() {
            @Override
            public void onResponse(String response) {
                JSONObject jobj = JSON.parseObject(response);
                // 默认有最新版本
                Properties prop = FileUtils.loadConfig(Constants.LOCAL_CONFIG_PATH);
                String bundleVersion = prop.getProperty("bundleVersion", "0");
                if (Integer.parseInt(bundleVersion) < jobj.getInteger("bundleVersion")) {
                    currentBundleVersion = jobj.getInteger("bundleVersion");
                    currentUrl = jobj.getString("url");
                    downLoadBundle();
                }
            }
        });
    }

    /**
     * 下载最新Bundle
     */
    private void downLoadBundle() {

        // 1.下载前检查SD卡是否存在更新包文件夹
        HotUpdate.checkPackage(Constants.LOCAL_FOLDER);
        // 2.下载
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager
                .Request(Uri.parse(Constants.JS_BUNDLE_REMOTE_URL));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setDestinationUri(Uri.parse("file://" + Constants.JS_PATCH_LOCAL_PATH));
        mDownLoadId = downloadManager.enqueue(request);
    }

    private void registeReceiver() {
        localReceiver = new CompleteReceiver();
        registerReceiver(localReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public class CompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            long completeId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (completeId == mDownLoadId) {
                Log.i("download complete", "download complete");
                Properties prop = new Properties();
                prop.setProperty("bundleVersion", currentBundleVersion + "");
                prop.setProperty("downloadUrl", currentUrl);
                FileUtils.saveConfig(Constants.LOCAL_CONFIG_PATH, prop);
                HotUpdate.handleZIP(getApplicationContext());
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(localReceiver);
    }
}
