package com.hotupdatetest.hotupdate;

/**
 * Created by wangze on 2017/12/15.
 */

public interface OnResponseListner {
    void onSucess(String response);
    void onError(String error);
}
