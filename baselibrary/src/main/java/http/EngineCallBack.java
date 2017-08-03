package http;

import android.content.Context;

import java.util.Map;

/**
 * Created by xinzhang on 7/26/17.
 */

public interface EngineCallBack {

    //callBack function before executing
    public void onPreExecute(Context context, Map<String, Object> params);

    // callBack funtion when http requestion fails
    public void onError(Exception e);

    // callBack funtion when http requestion sucesses
    public void onSuccess(String result);

    public final EngineCallBack DEFAULT_CALL_BACK = new EngineCallBack() {


        @Override
        public void onPreExecute(Context context, Map<String, Object> params) {

        }

        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onSuccess(String result) {

        }
    };
}
