package com.example.xinzhang.easyjoke;

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


public class MainActivity extends BaseSkinActivity {
    @ViewById(R.id.test_tv)
    private TextView mTestTv;
    @ViewById(R.id.test_iv)
    private ImageView mTestIv;
    @Override
    protected void initData() {

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
