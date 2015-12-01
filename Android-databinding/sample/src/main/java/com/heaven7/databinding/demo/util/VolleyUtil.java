package com.heaven7.databinding.demo.util;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.data.ApiParams;
import com.android.volley.data.RequestManager;
import com.android.volley.extra.ExpandNetworkImageView;
import com.android.volley.extra.RoundedBitmapBuilder;
import com.android.volley.toolbox.StringRequest;

import org.heaven7.core.util.Logger;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Created by heaven7 on 2015/8/12.
 */
public class VolleyUtil {

    private static final String TAG = "VolleyUtil";
    private static final boolean sDebug = true;
    static final RetryPolicy sPolicy = new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT );
    /**
     * Created by heaven7 on 2015/8/12.
     */
    public static class HttpExecutor {
        public HttpExecutor() {
        }
        public void get(final String url,ApiParams params,final VolleyUtil.HttpCallback callback){
            VolleyUtil.get(url,params,this,callback);
        }
        public void getByRestful(String url, String [] params, VolleyUtil.HttpCallback callback){
            VolleyUtil.get(buildRestfulUrl(url, params), this, callback);
        }
        public void get(final String url,final VolleyUtil.HttpCallback callback){
            VolleyUtil.get(url,this,callback);
        }
        public void post(final String url,ApiParams params,final VolleyUtil.HttpCallback callback){
            VolleyUtil.post(url, params, this, callback);
        }
        public void loadImage(String url, ExpandNetworkImageView imageView, RoundedBitmapBuilder builder){
            VolleyUtil.loadImage(url,imageView,builder);
        }

        public void cancelAll(){
            VolleyUtil.cancelAll(this);
        }
    }

    /**
     * 由于后台才用restful 框架(post和get 处理相同)，get请求的url拼接不能才用传统的方式。
     * @param baseUrl
     * @param params
     * @return
     */
    public static String buildRestfulUrl(String baseUrl,String ...params){
        if(params == null||params.length == 0){
            return baseUrl;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(baseUrl);
        for(int i=0,size = params.length ; i<size ;i++){
            String param = params[i];
            param  = /*( param.startsWith("{") && param.endsWith("}") ) ? param :*/ URLEncoder.encode(param);
             sb.append("/")
                     .append(TextUtils.isEmpty(param ) ? "%20" :param);
        }
        return sb.toString();
    }
    public static String buildRestfulUrl(String baseUrl,List<String> params){
        if(params == null||params.size() == 0){
            return baseUrl;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(baseUrl);
        for(int i=0,size = params.size() ; i<size ;i++){
            String param = params.get(i);
            param  = /*( param.startsWith("{") && param.endsWith("}") ) ? param :*/ URLEncoder.encode(param);
            sb.append("/")
                    .append(TextUtils.isEmpty(param ) ? "%20" : param);
        }
        return sb.toString();
    }

    public static String buildGetUrl(String baseUrl, ApiParams params) {
        if (params.isEmpty())
            return baseUrl;
        StringBuilder sb = new StringBuilder();
        sb.append("?");
        for (Map.Entry<String, String> en : params.entrySet()) {
            sb.append(en.getKey())
                    .append("=")
                    .append(en.getValue())
                    .append("&");
        }
        sb.deleteCharAt(sb.length() - 1);//delete last "&"
        return baseUrl + sb.toString();
    }

    public static void post(final String url, final ApiParams params, Object tag, final HttpCallback callback) {
        executeRequest(new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        logIfNeed(url, response);
                        callback.onResponse(url, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        logIfNeed(url, error.getMessage());
                        callback.onErrorResponse(url, error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        }, tag);
    }
    public static void get(final String url,ApiParams params,Object tag,final HttpCallback callback){
        if(params == null ||params.isEmpty()){
            get(url, tag, callback);
        }else{
            get(buildGetUrl(url, params), tag, callback);
        }
    }

    /**
     * get 请求返回 string
     */
    public static void get(final String url, Object tag, final HttpCallback callback) {
        //	ImageRequest
        executeRequest(new StringRequest(Request.Method.GET, url,
                                   new Response.Listener<String>() {
                                       @Override
                                       public void onResponse(String response) {
                                           logIfNeed(url, response);
                                           callback.onResponse(url, response);
                                       }
                                   },
                                   new Response.ErrorListener() {
                                       @Override
                                       public void onErrorResponse(VolleyError error) {
                                           logIfNeed(url, error.getMessage());
                                           callback.onErrorResponse(url, error);
                                       }
                                   }
                               ) {
                       }, tag);
    }

    public static void loadImage(String url, ExpandNetworkImageView imageView, RoundedBitmapBuilder builder) {
        builder.url(url).into(imageView);
    }

    private static void logIfNeed(String url, String response) {
        if (sDebug)
            Logger.w(TAG, "url = " + url, "response =" + response);
    }

    /**
     * @param tag used to cancel request
     */
    private static <T> void executeRequest(Request<T> request, Object tag) {
        request.setRetryPolicy(sPolicy);
        RequestManager.addRequest(request, tag);
    }

    public static void cancelAll(Object tag) {
        RequestManager.cancelAll(tag);
    }


    public interface HttpCallback {
        /**
         * http 访问成功
         */
        void onResponse(String url, String response);

        void onErrorResponse(String url, VolleyError error);

    }

}
