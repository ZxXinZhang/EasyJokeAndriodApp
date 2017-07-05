package com.example.xinzhang.easyjoke;

import android.os.Environment;
import android.support.v4.media.session.IMediaControllerCallback;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xinzhang.baselibrary.ioc.CheckNet;
import com.example.xinzhang.baselibrary.ioc.OnClick;
import com.example.xinzhang.baselibrary.ioc.ViewById;
import com.example.xinzhang.baselibrary.ioc.ViewUtils;
import com.example.xinzhang.framelibrary.BaseSkinActivity;

import java.io.File;
import java.io.IOException;

import base.ExceptionCrashHandler;


public class MainActivity extends BaseSkinActivity {
    @ViewById(R.id.test_tv)
    private TextView mTestTv;
    @ViewById(R.id.test_iv)
    private ImageView mTestIv;
    @Override
    protected void initData() {
        //get information pf the last crash exception and upload it to server
        File crashFile = ExceptionCrashHandler.getmInstance().getCrashFile();
        if(crashFile.exists()){
            //upload it to server

        }
        //fix bug
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

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
        startActivity(MainActivity.class);

    }

}
