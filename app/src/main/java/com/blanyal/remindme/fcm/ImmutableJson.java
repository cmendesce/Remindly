package com.blanyal.remindme.fcm;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by carlosmendes on 10/23/16.
 */
public class ImmutableJson {
    public static String jsonString(String... params) {
        JSONObject json = new JSONObject();

        for (int i = 0; i < params.length - 1; i++) {
            String key = params[i];
            String value = params[i+1];
            try {
                json.put(key, value);
            } catch (JSONException e) {
                // nothing. just continue.
            }
            i++;
        }

        return json.toString();
    }
}
