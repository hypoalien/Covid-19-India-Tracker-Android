package com.hypoalien.covid_19indiatracker;


import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface VolleyResponse {
    void onResponse(JSONObject object, String tag);

    void onError(VolleyError error, String tag);
}
