package com.hypoalien.covid_19indiatracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements VolleyResponse {

    public static String TAG_STATE="STATEWISE";
    public static String TAG_DISTRICT="DISTRICTWISE";
    public static JSONObject stateObj=new JSONObject();
    public static JSONObject distObj=new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestQueue queue = Volley.newRequestQueue(this);
        final CustomJSONObjectRequest request1= new CustomJSONObjectRequest(Request.Method.GET, "https://api.covid19india.org/data.json",
                new JSONObject(), "STATEWISE", this);
        final CustomJSONObjectRequest request2= new CustomJSONObjectRequest(Request.Method.GET, "https://api.covid19india.org/state_district_wise.json",
                new JSONObject(), "DISTRICTWISE", this);
        queue.add(request1.getJsonObjectRequest());
        queue.add(request2.getJsonObjectRequest());

    }

    @Override
    public void onResponse(JSONObject object, String tag) {
        if(tag.equals(TAG_STATE)){
            stateObj=object;
        }
        if (tag.equals(TAG_DISTRICT)){
            distObj=object;
            openHomeActivity();
        }
    }

    @Override
    public void onError(VolleyError error, String tag) {
        Toast.makeText(getApplicationContext(),"CHECK YOUR INTERNET CONNECTION",Toast.LENGTH_LONG).show();
    }
    public  void  openHomeActivity(){
        Intent myIntent = new Intent(MainActivity.this, HomeActivity.class);
        MainActivity.this.startActivity(myIntent);
    }
}
