package base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.util.StringBuilderPrinter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


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
        //information of mobile
        //save current file
        //system default
        String crashFileName = saveInfoToSD(ex);
        Log.e(TAG, "fileName --> " + crashFileName);
        cacheCrashFile(crashFileName);
        DefaultUncaughtExceptionHandler.uncaughtException(thread, ex);
    }

    public File getCrashFile(){
        String crashFileName = mContext.getSharedPreferences("crash",Context.MODE_PRIVATE).getString("CRASH_FILE_NAME","");
        return new File(crashFileName);
    }

    private void cacheCrashFile(String fileName) {
        SharedPreferences sp = mContext.getSharedPreferences("crash", Context.MODE_PRIVATE);
        sp.edit().putString("CRASH_FILE_NAME", fileName).commit();
    }

    /**
     * get information of mobile and application and error message. Save them as a file in SD card
     * @param ex
     * @return
     */
    private String saveInfoToSD(Throwable ex) {
        String fileName = null;
        StringBuilder sb = new StringBuilder();
        //get information of mobile and application
        for(Map.Entry<String, String> entry : obtainSimpleInfo(mContext).entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + " " + value);
            sb.append("\n");
        }
        //get information of exception
        sb.append(obtainExceptionIfo(ex));

        //save all the information as a file
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File dir = new File(mContext.getFilesDir()+ File.separator + "crash" + File.separator);
            //delete all the previous exception information
            if(dir.exists()){
                deleteDir(dir);
            }

            if (!dir.exists()) {
                dir.mkdir();
            }

            fileName = dir.toString() + File.separator + getAssignTime("yyyy_MM_dd_HH_mm") + ".txt";
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(fileName);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        return fileName;
    }

    private String getAssignTime(String dateFormatStr) {
        DateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
        long currentTime = System.currentTimeMillis();
        return dateFormat.format(currentTime);
    }

    /**
     * @param dir
     * @return boolean Returns "true" if all deletions were successful. If a
     * deletion fails, the method stops attempting to delete and returns
     * "false".
     */
    private boolean deleteDir(File dir) {
        if(dir.isDirectory()){
            File[] files = dir.listFiles();
            //recursively delete child files
            for(File file : files){
                file.delete();
            }
        }
        return true;
    }


    /**
     * get information of exception
     * @param ex
     * @return
     */
    private String obtainExceptionIfo(Throwable ex) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        ex.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }

    /**
     * get information of mobile and application
     * @param mContext
     * @return
     */
    private Map<String, String> obtainSimpleInfo(Context mContext) {
        Map<String, String> map = new HashMap<>();
        PackageManager mPackageManager = mContext.getPackageManager();
        PackageInfo mPackageInfo = null;
        try {
            mPackageInfo = mPackageManager.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        map.put("versionName", mPackageInfo.versionName);
        map.put("versionCode", "" + mPackageInfo.versionCode);
        map.put("MODEL", "" + Build.MODEL);
        map.put("SDK_INT", "" + Build.VERSION.SDK_INT);
        map.put("PRODUCT", "" + Build.PRODUCT);
        map.put("MOBLE_INFO", getMobileInfo());
        return map;
    }


    /**
     * get information of mobile
     * @return
     */
    private String getMobileInfo() {
        StringBuilder sb = new StringBuilder();
        Field[] fields = Build.class.getDeclaredFields();
        for(Field field : fields){
            field.setAccessible(true);
            String name = field.getName();
            String value = null;
            try {
                value = field.get(null).toString();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            sb.append(name + "=" + value);
            sb.append("\n");

        }
        return sb.toString();
    }
}
