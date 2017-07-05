package com.example.xinzhang.baselibrary.ioc;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by xinzhang on 6/29/17.
 */

public class ViewUtils {
    public static void inject(Activity activity){
        inject(new ViewFinder(activity), activity);
    }

    public static void inject(View view){
        inject(new ViewFinder(view), view);
    }

    public static void inject(View view, Object object){
        inject(new ViewFinder(view), object);
    }

    //for the compatibility of all the methods above
    //object is the class needed reflection
    private static void inject(ViewFinder finder, Object object){
        injectField(finder, object);
        injectEvent(finder, object);
    }

    private static void injectField(ViewFinder finder, Object object) {
        //1. get all the fields
        Class<?> clazz = object.getClass();
        //getDeclaredFields(): Declared means find all fields including private feilds
        Field[] fields = clazz.getDeclaredFields();
        //2. get value
        for(Field field : fields){
            ViewById viewById = field.getAnnotation(ViewById.class);
            if(viewById != null){
                //3. find view by id
                int viewId = viewById.value();
                View view = finder.findViewById(viewId);
                if(view != null){
                    field.setAccessible(true);
                    //4. inject view dynamically
                    try {
                        field.set(object,view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    private static void injectEvent(ViewFinder finder, Object object) {
        //1. get all the methods
        Class<?> clazz = object.getClass();
        Method[] methods =  clazz.getDeclaredMethods();
        //2. get value
        for(Method method : methods){
            OnClick onClick = method.getAnnotation(OnClick.class);
            if(onClick != null){
                int[] viewIds = onClick.value();
                //3. find view by id
                for(int viewId : viewIds){
                    View view = finder.findViewById(viewId);
                    // extention function: check network connection
                    boolean isCheckNet = method.getAnnotation(CheckNet.class) != null;

                    if(view != null){
                        //4. view.setOnClickListener
                        view.setOnClickListener(new DelaredOnClickListsener(method, object, isCheckNet));
                    }
                }
            }
        }

    }


    private static class DelaredOnClickListsener implements View.OnClickListener{
        private Method mMethod;
        private Object mObject;
        private boolean mIsCheckNet;
        public DelaredOnClickListsener(Method method, Object object, boolean isCheckNet){
            this.mMethod = method;
            this.mObject = object;
            this.mIsCheckNet = mIsCheckNet;
        }
        @Override
        public void onClick(View v) {
            if(mIsCheckNet){
                if(!networkAvailable(v.getContext())){
                    Toast.makeText(v.getContext(), "Network connect error.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            try {
                mMethod.setAccessible(true);
                //5. execute methods by reflection
                mMethod.invoke(mObject, v);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    mMethod.invoke(mObject, null);
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (InvocationTargetException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }


    /**
     * to see current network works or not
     */
    private static boolean networkAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
