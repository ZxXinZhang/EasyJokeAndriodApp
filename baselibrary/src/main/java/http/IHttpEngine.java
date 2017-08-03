package http;

import android.content.Context;

import java.util.Map;

/**
 * Created by xinzhang on 7/26/17.
 * Description: standard for http request engine.
 */

public interface IHttpEngine {

    //get request
    void get(Context context, String url, Map<String, Object> params, EngineCallBack callBack);

    //post request
    void post(Context context, String url, Map<String, Object> params, EngineCallBack callBack);

    //upload file

    //download file

    //https add a certificate


}
