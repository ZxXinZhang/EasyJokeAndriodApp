package com.example.xinzhang.framelibrary;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import navigationbar.AbsNavigationBar;

/**
 * Created by xinzhang on 7/13/17.
 */

public class DefaultNavigationBar extends AbsNavigationBar<DefaultNavigationBar.Builder.DefaultAbsNavigationParams> {

    static String TAG = "bar";
    public DefaultNavigationBar(DefaultNavigationBar.Builder.DefaultAbsNavigationParams params) {
        super(params);
    }

    @Override
    public int bindLayoutId() {
        return R.layout.title_bar;
    }

    @Override
    public void applyView() {
        //bind parameters
        setText(R.id.title, getParams().mTitle);
        setText(R.id.right_text,getParams().mRightText);

        TextView tx = findViewById(R.id.right_text);
        Log.e(TAG, "applyView: " + tx.getText());
        setOnClickListener(R.id.right_text, getParams().mRightClickListener);
        //default: end Activity on the left
        setOnClickListener(R.id.back, getParams().mLeftClickListener);
    }



    public static class Builder extends AbsNavigationBar.Builder{
        DefaultAbsNavigationParams P;

        public Builder(Context context, ViewGroup parent) {
            super(context, parent);
            P = new DefaultAbsNavigationParams(context, parent);
        }

        public Builder(Context context) {
            super(context, null);
            P = new DefaultAbsNavigationParams(context, null);
        }

        @Override
        public DefaultNavigationBar build() {
            DefaultNavigationBar navigationBar = new DefaultNavigationBar(P);
            return navigationBar;
        }

        //set all the features

        public Builder setTitle(String title){
            P.mTitle = title;
            return this;
        }

        public Builder setRightText(String text){
            P.mRightText = text;
            Log.e(TAG, "setRightText: ");
            return this;
        }

        public Builder setRightIcon(int rightRes){
            return this;
        }

        public Builder setRightClickListener(View.OnClickListener rightListener){
            P.mRightClickListener = rightListener;
            return this;
        }

        public Builder setLeftClickListener(View.OnClickListener leftListener){
            P.mLeftClickListener = leftListener;
            return this;
        }

        public static class DefaultAbsNavigationParams extends AbsNavigationParams{
            public String mTitle;
            public String mRightText;
            public View.OnClickListener mRightClickListener;
            public View.OnClickListener mLeftClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //default operation when clicking up-left part of navigation bar: if no end current activity
                    ((Activity)(mContext)).finish();
                }
            };
            //set all features

            public DefaultAbsNavigationParams(Context context, ViewGroup parent) {
                super(context, parent);
            }
        }
    }
}
