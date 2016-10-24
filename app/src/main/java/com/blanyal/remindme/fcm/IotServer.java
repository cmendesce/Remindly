package com.blanyal.remindme.fcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by carlosmendes on 10/23/16.
 */
public class IotServer {

    private final OkHttpClient client;
    private final Context context;
    private final SharedPreferences preferences;

    public IotServer(Context context) {
        this.client = new OkHttpClient();
        this.context = context;
        this.preferences = this.context.getSharedPreferences(context.getPackageName(), 0);
    }

    public void register(final String email, final String token) {
        local(token);
        new Thread(new Runnable() {
            @Override
            public void run() {
                remote("https://iot-tdah.herokuapp.com/mobiles", ImmutableJson.jsonString("token", token, "owner", email));
            }
        }).start();
    }

    private void setRegistered(boolean registered) {
        this.preferences.edit().putBoolean("registered", registered).apply();
    }

    public boolean isRegistered() {
        return this.preferences.getBoolean("registered", false);
    }

    public String token() {
        return preferences.getString("token", "");
    }

    private void remote(String url, String json) {
        try {
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            setRegistered(response.isSuccessful());
        } catch (Exception e) {
            Log.d("IotServer", e.getMessage());
            setRegistered(false);
        }
    }

    private void local(String token) {
        this.preferences.edit().putString("token", token).apply();
    }

}
