package com.example.xinzhang.easyjoke;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.alipay.euler.andfix.patch.PatchManager;

import base.ExceptionCrashHandler;

/**
 * Created by xinzhang on 7/5/17.
 */

public class BaseApplication extends Application {

    public static PatchManager mPatchManager;

    @Override
    public void onCreate() {
        super.onCreate();
        ExceptionCrashHandler.getmInstance().init(this);
        //AndFix is a library that offers hot-fix for Android App from alibaba
        mPatchManager = new PatchManager(this);
        //get the version name of application
        PackageManager packageManager = this.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo();
        String versionName = packageInfo.versionName;
        mPatchManager.init(versionName);
        //
        mPatchManager.loadPatch();
    }
}
