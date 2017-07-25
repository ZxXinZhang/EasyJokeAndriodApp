package com.example.xinzhang.easyjoke;

import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.media.session.IMediaControllerCallback;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xinzhang.baselibrary.ioc.CheckNet;
import com.example.xinzhang.baselibrary.ioc.OnClick;
import com.example.xinzhang.baselibrary.ioc.ViewById;
import com.example.xinzhang.baselibrary.ioc.ViewUtils;
import com.example.xinzhang.framelibrary.BaseSkinActivity;
import com.example.xinzhang.framelibrary.DefaultNavigationBar;

import java.io.File;
import java.io.IOException;

import base.ExceptionCrashHandler;
import fixBug.FixDexManager;


public class MainActivity extends BaseSkinActivity {
    @ViewById(R.id.test_tv)
    private TextView mTestTv;
    @ViewById(R.id.test_iv)
    private ImageView mTestIv;

    @Override
    protected void initData() {
        fixDexBug();
        //fixBug();
    }

    private void fixDexBug() {
        File fixFile = new File(Environment.getExternalStorageDirectory(),"fix.dex");
        if(fixFile.exists()){
            FixDexManager fixDexManager = new FixDexManager(this);
            try {
                fixDexManager.fixDex(fixFile.getAbsolutePath());
                Toast.makeText(this, "fixed successfully", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, "failed to fix bug", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    private void fixBug() {
        //get information pf the last crash exception and upload it to server
//        File crashFile = ExceptionCrashHandler.getmInstance().getCrashFile();
//        if(crashFile.exists()){
//            //upload it to server
//
//        }
//        fix bug
        File fixFile = new File(Environment.getExternalStorageDirectory(),"fix.apatch");
        if(fixFile.exists()){
            try {
                BaseApplication.mPatchManager.addPatch(fixFile.getAbsolutePath());
                Toast.makeText(this,"fixed",Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this,"failed to fix bug",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initTitle() {
        DefaultNavigationBar navigationBar = new DefaultNavigationBar.Builder(this).setTitle("加油").setRightText("zx").build();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);

    }

}
