package com.blanyal.remindme.fcm;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

/**
 * Created by carlosmendes on 10/23/16.
 */
public class IotServer implements Response.Listener<String>, Response.ErrorListener {

    private final Context mContext;

    public IotServer(Context context) {
        mContext = context;
    }

    public void register(String email, String token) {
        Request request = new RegisterMobileRequest(token, email, this, this);
        RequestQueue queue = Volley.newRequestQueue(context());
        queue.add(request);
        queue.start();
    }

    public Context context() {
        return mContext;
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(String location) {

    }
}
