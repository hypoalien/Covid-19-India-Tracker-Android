package com.hypoalien.covid_19indiatracker;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static com.hypoalien.covid_19indiatracker.HomeActivity.MY_PREFS_NAME;
import static com.hypoalien.covid_19indiatracker.App.CHANNEL_1_ID;

public class ScheduledService extends Service{
    int oldConfirmed;
    int oldDelta;
    String newConfirmed;
    String newDelta;
    String newDeath;
    JSONArray jsonArray;
    private Timer timer = new Timer();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate()
    {
        super.onCreate();
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        oldConfirmed = prefs.getInt("confirmed",-1);
        oldDelta = prefs.getInt("confirmeddelta",-1);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                apiCalls();

            }
        }, 0, 30*60*1000);//30 Minutes
    }
    private void apiCalls(){
        String url = "https://api.covid19india.org/data.json";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,

                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            jsonArray= response.getJSONArray("statewise");
                            newConfirmed=jsonArray.getJSONObject(0).getString("confirmed");
                            newDeath=jsonArray.getJSONObject(0).getString("deaths");
                            newDelta=jsonArray.getJSONObject(0).getJSONObject("delta").getString("confirmed");
                            if(Integer.parseInt(newConfirmed)!=oldConfirmed) {
                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putInt("confirmed", Integer.parseInt(newConfirmed));
                                editor.putInt("confirmeddelta",Integer.parseInt(newDelta));
                                editor.apply();
                                sendOnChannel1();
                            }else {

                                }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        queue.add(getRequest);
    }
    private void sendOnChannel1() {
        String title = newConfirmed+" Total Cases "+"(+ "+newDelta+" New )";
        String message = "Total Death Toll : "+newDeath;
        Intent resultIntent = new Intent(this,HomeActivity.class);
        PendingIntent resultPendingIntent=PendingIntent.getActivity(this,1,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID )
                .setSmallIcon(R.drawable.logo_small)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[] { 1000, 1000 })
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(1, notification);
        oldConfirmed=Integer.parseInt(newConfirmed);
    }

}

