package util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.example.learn.R;

/**
 * Created by Liujilong on 16/1/22.
 * liujilong.me@gmail.com
 */
public final class OkHttpUtils {

    private static final String TAG = "OkHttpUtils";

    // reserve all the running calls for cancel.
    private final ArrayMap<String,Set<Call>> mRunningCalls;

    private static OkHttpUtils mInstance;
    private OkHttpClient mClient;
    private Handler mHandler;

    private OkHttpUtils(){
        mClient = new OkHttpClient();
        mHandler = new Handler(Looper.getMainLooper());
        mRunningCalls = new ArrayMap<>();
    }

    private synchronized static OkHttpUtils getInstance(){
        if(mInstance==null){
            mInstance = new OkHttpUtils();
        }
        return mInstance;
    }

    public static OkHttpClient getClient(){
        return getInstance().mClient;
    }

    @SuppressWarnings("unused")
    public static void post(String url, Map<String,String> params, OkCallBack callback){
        post(url, params, null, callback);
    }

    public static void post(String url, Map<String,String> params,String tag, OkCallBack callback){
        //Log.d("iThinker","register post1 url="+url);
    	JSONObject j = new JSONObject(params);
    	//Log.d("ithinker",j.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), j.toString());
        Request.Builder builder = new Request.Builder().url(url).post(body);
        if(tag!=null) builder.tag(tag);
        Request request = builder.build();
        //Log.d("ithinker",body.toString());
        getInstance().firePost(request, callback);
        //Log.d("iThinker","register post2 = "+request.toString());
    	//Log.d("ithinker",request.toString());
    }



    

    private void firePost(final Request request, final OkCallBack callback){
    	//Log.d("ithinker","firepost");
        Call call = mClient.newCall(request);
        cacheCallForCancel(call);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                if (isCancel(call)) return;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("iThinker","register post4");
                        if (isCancel(call, true)) return;
                        callback.onFailure(e);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                if (isCancel(call)) return;
                final String s = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Log.d("iThinker","register post3");
                        if (isCancel(call, true)) return;
                        callback.onResponse(s);
                    }
                });
            }
        });
    }

    private void cacheCallForCancel(Call call){
        String tag = (String) call.request().tag();
        if(tag!=null){
            synchronized (mRunningCalls) {
                //Log.d("ithinker","start caching Call: " + printCall(call) + " \ncurrent running calls: "+printRunningCalls());
                Set<Call> calls = mRunningCalls.get(tag);
                if (calls == null) {
                    calls = new HashSet<>();
                }
                calls.add(call);
                mRunningCalls.put(tag, calls);
                //Log.d("ithinker","finish caching Call: " + printCall(call) + " \ncurrent running calls: "+printRunningCalls());
            }
        }
    }

    private static String printRunningCalls(){
        return "\n"+getInstance().mRunningCalls.toString();
    }
    private String printCall(Call call){
        return "call: " + call.toString() + " with tag " + call.request().tag();
    }

    private boolean isCancel(Call call){
        return isCancel(call,false);
    }
    private boolean isCancel(Call call, boolean remove){
        String tag = (String) call.request().tag();
        if(tag==null) return false;
        synchronized (mRunningCalls) {
            //LogUtils.d(TAG,"start remove Call: " + printCall(call) + " \ncurrent running calls: "+printRunningCalls());
            Set<Call> calls = mRunningCalls.get(tag);
            if (calls == null || !calls.contains(call)) return true;
            if(remove) {
                calls.remove(call);
                if (calls.isEmpty()) {
                    mRunningCalls.remove(tag);
                    //LogUtils.d(TAG, "finish remove Call: " + printCall(call) + " \ncurrent running calls: " + printRunningCalls());
                }
            }
        }
        return false;
    }

    public static void cancel(String tag){
        //LogUtils.d(TAG,"start remove Calls with tag: " + tag + " \ncurrent running calls: "+printRunningCalls());
        getInstance().mRunningCalls.remove(tag);
        //LogUtils.d(TAG, "finish remove Calls with tag: " + tag + " \ncurrent running calls: " + printRunningCalls());
    }

    public interface OkCallBack{
        void onFailure(IOException e);
        void onResponse(String res);
    }
    public static class SimpleOkCallBack implements OkCallBack{
        @Override
        public void onFailure(IOException e) {
        }
        @Override
        public void onResponse(String s) {
        }
    }

    public static JSONObject parseJSON(Context context, String s){
        JSONObject j;
        try {
            j = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show();
            return null;
        }
        String state = j.optString("state");
        if (!state.equals("successful")) {
            Toast.makeText(context, j.optString("reason"), Toast.LENGTH_SHORT).show();
            return null;
        }
        return j;
    }

}
