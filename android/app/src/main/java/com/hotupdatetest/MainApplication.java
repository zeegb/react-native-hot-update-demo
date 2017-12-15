package com.hotupdatetest;

import android.app.Application;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;
import com.hotupdatetest.hotupdate.Constants;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MainApplication extends Application implements ReactApplication {
    private static MainApplication instance;

    private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
        @Nullable
        @Override
        protected String getJSBundleFile() {
            File file = new File(Constants.JS_BUNDLE_LOCAL_PATH);
            if (file.exists()) {
                Log.i("getJSBundleFile:", "exists");
                return Constants.JS_BUNDLE_LOCAL_PATH;
            } else {
                Log.i("getJSBundleFile:", "no-exists");
                return super.getJSBundleFile();
            }
        }

        @Override
        public boolean getUseDeveloperSupport() {
            return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
            return Arrays.<ReactPackage>asList(
                    new MainReactPackage()
            );
        }

        @Override
        protected String getJSMainModuleName() {
            return "index";
        }
    };

    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        SoLoader.init(this, /* native exopackage */ false);
    }

    public static MainApplication getInstance() {
        return instance;
    }

    public String getAppPackageName() {
        return this.getPackageName();
    }
}
