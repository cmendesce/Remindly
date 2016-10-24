package com.blanyal.remindme.fcm;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by carlosmendes on 10/23/16.
 */
public class InstanceService  extends FirebaseInstanceIdService {
    private static final String TAG = "InstanceService";

    private SharedPreferences preferences;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        this.preferences = this.getSharedPreferences("remindly", 0);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "------------------------- Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        persite(token);
        IotServer server = new IotServer(this);
        UserInfo userInfo = new UserInfo(this);
        server.register(userInfo.email(), token);
    }


    private void persite(String token) {
        preferences.edit().putString("token", token).apply();
    }
}
