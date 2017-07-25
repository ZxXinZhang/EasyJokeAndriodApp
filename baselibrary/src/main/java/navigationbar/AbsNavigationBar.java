package navigationbar;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;




/**
 * Created by xinzhang on 7/13/17.
 * a base class of header
 */

public abstract class AbsNavigationBar<P extends AbsNavigationBar.Builder.AbsNavigationParams> implements INavigationBar {

    static String TAG = "bar";
    private P mParams;
    private View mNavigationView;

    public AbsNavigationBar(P params){
        this.mParams = params;
        createAndBindView();
    }

    private void createAndBindView() {
        if(mParams.mParent == null){
            //get the root ViewGroup of activity
            ViewGroup activityRoot = (ViewGroup) ((Activity)(mParams.mContext)).findViewById(android.R.id.content);
            mParams.mParent = (ViewGroup) activityRoot.getChildAt(0);
        }
        mNavigationView = LayoutInflater.from(mParams.mContext).inflate(bindLayoutId(),mParams.mParent,false);
        mParams.mParent.addView(mNavigationView,0);
        applyView();
    }

    public P getParams() {
        return mParams;
    }

    protected void setText(int viewId, String text) {
        TextView tv = findViewById(viewId);

        if(!TextUtils.isEmpty(text)){
            Log.e(TAG, "setText: "+ text );
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
        }
    }

    protected void setOnClickListener(int viewId, View.OnClickListener listener){
        findViewById(viewId).setOnClickListener(listener);
    }

    public <T extends View>T findViewById(int viewId){
        return (T) mNavigationView.findViewById(viewId);
    }


    public abstract static class Builder{

        public Builder(Context context, ViewGroup parent){

        }

        public abstract AbsNavigationBar build();

        public static class AbsNavigationParams{
            public Context mContext;
            public ViewGroup mParent;
            public AbsNavigationParams(Context context, ViewGroup parent){
                this.mContext = context;
                this.mParent = parent;
            }
        }
    }
}
