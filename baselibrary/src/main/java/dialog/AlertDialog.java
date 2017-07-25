package dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xinzhang.baselibrary.R;

import java.lang.ref.WeakReference;

/**
 * Created by xinzhang on 7/10/17.
 */

public class AlertDialog extends Dialog {
    public AlertController mAlert;

    public AlertDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        mAlert = new AlertController(this, getWindow());
    }

    public void setText(int viewId, CharSequence text) {
        mAlert.setText(viewId, text);
    }



    /**
     * avoid to findViewById too many times
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        return mAlert.getView(viewId);
    }

    public void setOnclickListener(int viewId, View.OnClickListener listener) {
        mAlert.setOnclickListener(viewId, listener);
    }

    public static class Builder{
        private AlertController.AlertParams P;

        public Builder(Context context){
            this(context, R.style.dialog);
        }

        public Builder(Context context, int themeResId) {
            P = new AlertController.AlertParams(context, themeResId);
        }

        public Builder setText(int viewId, CharSequence text) {
            P.mTextArray.put(viewId, text);
            return this;
        }

        public Builder setOnClickListener(int viewId, View.OnClickListener listener) {
            P.mClickArray.put(viewId, listener);
            return this;
        }


        public Builder setContentView(int layoutId) {
            P.mView = null;
            P.mViewLayoutResId = layoutId;
            return this;
        }

        public Builder setContentView(View view) {
            P.mViewLayoutResId = 0;
            P.mView = view;
            return this;
        }

        public Builder fullWidth(){
            P.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        public Builder fromBottom(boolean isAnimation){
            if(isAnimation){
                P.mAnimation = R.style.dialog_from_bottom_anim;
            }
            P.mGravity = Gravity.BOTTOM;
            return this;
        }

        public Builder setWidthAndHeight(int width, int height){
            P.mWidth = width;
            P.mHeight = height;
            return this;
        }

        public Builder setDefaultAnimation(){
            P.mAnimation = R.style.dialog_scale_anim;
            return this;
        }

        public Builder setAnimations(int styleAnimation){
            P.mAnimation = styleAnimation;
            return this;
        }

        public AlertDialog create(){
            // new AlertDialog object
            final AlertDialog dialog = new AlertDialog(P.mContext, P.mThemeResId);
            //apply all parameters into dialog
            P.apply(dialog.mAlert);
            dialog.setCancelable(P.mCancelable);
            if (P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(P.mOnCancelListener);
            if (P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener);
            }
            return dialog;
        }


        /**
         * Creates an {@link AlertDialog} with the arguments supplied to this
         * builder and immediately displays the dialog.
         * <p/>
         * Calling this method is functionally identical to:
         * <pre>
         *     AlertDialog dialog = builder.create();
         *     dialog.show();
         * </pre>
         */
        public AlertDialog show() {
            final AlertDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }






}
