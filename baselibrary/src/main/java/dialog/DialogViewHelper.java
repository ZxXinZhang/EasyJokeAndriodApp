package dialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * description: helper class for dialog view
 * Created by xinzhang on 7/10/17.
 */

class DialogViewHelper {
    private View mContentView = null;
    private SparseArray<WeakReference<View>> mViews;

    public DialogViewHelper() {
        mViews = new SparseArray<>();

    }
    public DialogViewHelper(Context context, int mViewLayoutResId) {
        this();
        mContentView = LayoutInflater.from(context).inflate(mViewLayoutResId,null);
    }


    public void setContentView(View mView) {
        this.mContentView = mView;
    }

    public void setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        if(tv != null){
            tv.setText(text);
        }
    }



    /**
     * avoid to findViewById too many times
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        WeakReference<View> viewReference = mViews.get(viewId);
        View view = null;
        if(viewReference != null){
            view = viewReference.get();
            if(view == null){
                view = mContentView.findViewById(viewId);
                if(view != null){
                    mViews.put(viewId, new WeakReference<>(view));
                }

            }
        }
        return (T) view;
    }

    public void setOnclickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        if(view != null){
            view.setOnClickListener(listener);
        }
    }

    public View getContentView() {
        return mContentView;
    }
}
