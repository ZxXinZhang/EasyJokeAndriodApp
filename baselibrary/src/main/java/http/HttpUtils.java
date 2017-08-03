package http;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.ArrayMap;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xinzhang on 7/26/17.
 */

public class HttpUtils {
    //
    private String mUrl;

    //type of http request
    private int mType = GET_TYPE;

    private static final int GET_TYPE = 0x0011;
    private static final int POST_TYPE = 0x0022;

    private Context mContext;
    private Map<String, Object> mParams;

    private HttpUtils(Context context){
        mContext = context;
        mParams = new HashMap<>();
    }

    public static HttpUtils with(Context context) {
        return new HttpUtils(context);
    }

    public HttpUtils url(String url){
        mUrl = url;
        return this;
    }

    public HttpUtils post(){
        mType = POST_TYPE;
        return this;
    }

    public HttpUtils get(){
        mType = GET_TYPE;
        return this;
    }

    //add parameters
    public HttpUtils addParams(String key, Object value){
        mParams.put(key, value);
        return this;
    }

    public HttpUtils addParams(Map<String, Object> params){
        mParams.putAll(params);
        return this;
    }

    //add call back function
    public void execute(EngineCallBack callBack){



        if(callBack == null){
            callBack = EngineCallBack.DEFAULT_CALL_BACK;
        }
        callBack.onPreExecute(mContext, mParams);
        if(mType == POST_TYPE){
            post(mUrl, mParams, callBack);
        } else if(mType == GET_TYPE) {
            get(mUrl,mParams, callBack);
        }
    }

    public void execute(){
        execute(null);
    }

    //default: OkHttpEngine
    private static IHttpEngine mHttpEngine = new OkHttpEngine();

    //initialize engine in application
    public static void init(IHttpEngine httpEngine){
        mHttpEngine = httpEngine;
    }

    public HttpUtils exchangeEngine(IHttpEngine httpEngine){
        mHttpEngine = httpEngine;
        return this;
    }


    private void get(String url, Map<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.get(mContext, url, params, callBack);
    }

    private void post(String url, Map<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.post(mContext, url, params, callBack);
    }

    /**
     * joint parameters as a string
     */
    public static String jointParams(String url, Map<String, Object> params) {
        if (params == null || params.size() <= 0) {
            return url;
        }

        StringBuffer stringBuffer = new StringBuffer(url);
        if (!url.contains("?")) {
            stringBuffer.append("?");
        } else {
            if (!url.endsWith("?")) {
                stringBuffer.append("&");
            }
        }

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
        }

        stringBuffer.deleteCharAt(stringBuffer.length() - 1);

        return stringBuffer.toString();
    }

    /**
     * parse the information of a class object
     */
    public static Class<?> analysisClazzInfo(Object object) {
        Type genType = object.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }
}
