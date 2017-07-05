package com.example.xinzhang.baselibrary.ioc;

import android.app.Activity;
import android.view.View;

/**
 * Created by xinzhang on 6/29/17.
 * Description: helper class of View to findViewById
 */

public class ViewFinder {
    private Activity mActivity;
    private View mView;

    public ViewFinder(Activity activity) {
        this.mActivity = activity;
    }

    public ViewFinder(View view) {
        this.mView = view;
    }

    public View findViewById(int viewId){
        return mActivity != null ? mActivity.findViewById(viewId) : mView.findViewById(viewId);
    }
}
