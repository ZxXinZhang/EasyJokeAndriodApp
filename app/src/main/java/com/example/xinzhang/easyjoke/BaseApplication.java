package com.example.xinzhang.easyjoke;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.alipay.euler.andfix.patch.PatchManager;

import base.ExceptionCrashHandler;
import fixBug.FixDexManager;

/**
 * Created by xinzhang on 7/5/17.
 */

public class BaseApplication extends Application {

    public static PatchManager mPatchManager;

    @Override
    public void onCreate() {
        super.onCreate();
//        ExceptionCrashHandler.getmInstance().init(this);
//        //AndFix is a library that offers hot-fix for Android App from alibaba
//        mPatchManager = new PatchManager(this);
//        //get the version name of application
//        PackageManager packageManager = this.getPackageManager();
//        PackageInfo packageInfo = null;
//        try {
//            packageInfo = packageManager.getPackageInfo(this.getPackageName(),0);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        String versionName = packageInfo.versionName;
//        mPatchManager.init(versionName);
//        //load all the previous apatch files
//        mPatchManager.loadPatch();



        try {
            FixDexManager fixDexManager = new FixDexManager(this);
            fixDexManager.loadFixDex();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
