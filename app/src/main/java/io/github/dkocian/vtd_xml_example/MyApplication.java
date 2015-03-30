package io.github.dkocian.vtd_xml_example;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import io.github.dkocian.vtd_xml_example.network.OkHttpStack;

/**
 * Created by dkocian on 3/30/2015.
 */
public class MyApplication extends Application {
    public static final String TAG = MyApplication.class.getSimpleName();
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
    public static RequestQueue getVolleyQueue() {
        return RequestQueueHolder.queue;
    }

    public static Request addToQueue(Request request) {
        return getVolleyQueue().add(request);
    }

    private static class RequestQueueHolder {
        private static final RequestQueue queue = Volley.newRequestQueue(context, new OkHttpStack());
    }
}
