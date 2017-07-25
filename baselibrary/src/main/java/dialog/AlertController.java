package dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.xinzhang.baselibrary.R;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xinzhang on 7/10/17.
 */

class AlertController {
    private AlertDialog mDialog;
    private Window mWindow;
    private DialogViewHelper mViewHelper;

    public AlertController(AlertDialog alertDialog, Window window) {
        this.mDialog = alertDialog;
        this.mWindow = window;
    }

    public void setViewHelper(DialogViewHelper viewHelper){
        this.mViewHelper = viewHelper;
    }

    public AlertDialog getDialog() {
        return mDialog;
    }

    public Window getWindow() {
        return mWindow;
    }

    public void setText(int viewId, CharSequence text) {
        mViewHelper.setText(viewId, text);
    }

    /**
     * avoid to findViewById too many times
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        return mViewHelper.getView(viewId);
    }

    public void setOnclickListener(int viewId, View.OnClickListener listener) {
        mViewHelper.setOnclickListener(viewId, listener);
    }

    public static class AlertParams{
        public Context mContext;
        public int mThemeResId;
        //can cancel the dialog by clicking somewhere else
        public boolean mCancelable = true;
        public DialogInterface.OnCancelListener mOnCancelListener;
        public DialogInterface.OnDismissListener mOnDismissListener;
        public DialogInterface.OnKeyListener mOnKeyListener;
        public View mView;
        public int mViewLayoutResId;
        public SparseArray<CharSequence> mTextArray = new SparseArray<>();
        public SparseArray<View.OnClickListener> mClickArray = new SparseArray<>();
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        public int mAnimation = 0;
        public int mGravity = Gravity.CENTER;
        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;


        public AlertParams(Context context, int themeResId){
            this.mContext = context;
            this.mThemeResId = themeResId;
        }


        /**
         * set parameters
         * @param mAlert
         */
        public void apply(AlertController mAlert){
            //1. set layout
            DialogViewHelper viewHelper = null;
            if(mViewLayoutResId != 0){
                viewHelper = new DialogViewHelper(mContext, mViewLayoutResId);
            }
            if (mView != null){
                viewHelper = new DialogViewHelper();
                viewHelper.setContentView(mView);
            }
            if(viewHelper == null){
                throw new IllegalArgumentException("Please set layout in setContentView()");
            }

            mAlert.getDialog().setContentView(viewHelper.getContentView());

            //set a viewHelper for controller
            mAlert.setViewHelper(viewHelper);

            //2. set text
            int textArraySize = mTextArray.size();
            for(int i = 0; i < textArraySize; i++){
                mAlert.setText(mTextArray.keyAt(i), mTextArray.valueAt(i));
            }
            //3. set onclick
            int clickArraySize = mClickArray.size();
            for(int i = 0; i < clickArraySize; i++){
                mAlert.setOnclickListener(mClickArray.keyAt(i), mClickArray.valueAt(i));
            }



            //4. configure some customized effects

            Window window = mAlert.getWindow();
            window.setGravity(mGravity);
            if(mAnimation != 0){
                window.setWindowAnimations(mAnimation);
            }
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = mWidth;
            params.height = mHeight;
            window.setAttributes(params);

        }
    }


}
