package base;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.xinzhang.baselibrary.ioc.ViewById;
import com.example.xinzhang.baselibrary.ioc.ViewUtils;

/**
 * Created by xinzhang on 7/3/17.
 * Description: include functions required by most basic activities
 */

public abstract class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setup layout
        setContentView();
        ViewUtils.inject(this);
        //initialize title
        initTitle();
        //initialize View
        initView();
        //initialize data
        initData();
    }

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void initTitle();

    protected abstract void setContentView();


    /**
     * startActivity
     */
    protected void startActivity(Class<?> clazz){
        Intent intent = new Intent(this, clazz);
        startActivity(intent);

    }

    /**
     * viewById
     * @return
     */
    protected <T extends View> T ViewById(int viewId){
        return (T) findViewById(viewId);
    }
}
