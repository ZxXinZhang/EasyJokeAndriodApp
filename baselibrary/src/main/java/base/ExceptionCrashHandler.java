package base;

import android.content.Context;
import android.util.Log;


/**
 * Created by xinzhang on 7/4/17.
 */

public class ExceptionCrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG ="ExceptionCrashHandler";
    private static ExceptionCrashHandler mInstance;
    private static Thread.UncaughtExceptionHandler DefaultUncaughtExceptionHandler;

    public static ExceptionCrashHandler getmInstance(){
        if(mInstance == null){
            synchronized (ExceptionCrashHandler.class){
                if(mInstance == null){
                    mInstance = new ExceptionCrashHandler();
                }
            }
        }
        return mInstance;
    }

    //in order to get information of application
    private Context mContext;
    public void init(Context context){
        this.mContext = context;
        //set global Exception
        Thread.currentThread().setUncaughtExceptionHandler(this);

        DefaultUncaughtExceptionHandler = Thread.currentThread().getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        //global exceptions
        Log.e(TAG, "FIND EXCEPTIONS");
        //log information of exceptions into local file, including exceptions themselves and versions
        //error message
        //application message
        //information of phone
        //save current file
        //system default
        DefaultUncaughtExceptionHandler.uncaughtException(thread, ex);
    }
}
