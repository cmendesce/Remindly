package com.blanyal.remindme.fcm;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

/**
 * Created by carlosmendes on 10/23/16.
 */
public class RegisterMobileRequest  extends JsonRequest<String> {

    public RegisterMobileRequest(final String token, final String email, Response.Listener<String> listener,
                                 Response.ErrorListener errorListener) {
        super(Method.POST, "https://iot-tdah.herokuapp.com/mobiles", ImmutableJson.object("token", token, "email", email).toString(), listener, errorListener);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        Log.d("RegisterMobileRequest", "------------ response capturado");
        return Response.success(response.headers.get("Location"), HttpHeaderParser.parseCacheHeaders(response));
    }
}
