package com.hypoalien.covid_19indiatracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.hypoalien.covid_19indiatracker.MainActivity.distObj;
import static com.hypoalien.covid_19indiatracker.MainActivity.stateObj;


public class HomeActivity extends AppCompatActivity {
    JSONObject pos;
    JSONArray jsonArray;
    RecyclerView recyclerView;
    Adapter adapter;
    ConstraintLayout constraintLayout;
    LinearLayoutManager linearLayoutManager;

    public static String MY_PREFS_NAME = "Covid-19Prefs";

    public String confirmedDelta, deceasedDelta, recoveredDelta, activeDelta;

    public static int ANIMATION_SPEED_MS_FAST = 800;
    public static int ANIMATION_SPEED_MS_MEDIUM = 400;
    public static int ANIMATION_SPEED_MS_SLOW = 1200;

    ArrayList<String> state = new ArrayList<>();
    ArrayList<String> active = new ArrayList<>();
    ArrayList<String> confirmed = new ArrayList<>();
    ArrayList<String> recovered = new ArrayList<>();
    ArrayList<String> death = new ArrayList<>();
    ArrayList<String> delta = new ArrayList<>();
    ArrayList<String> status = new ArrayList<>();

    TextView mActive, mConfirmed, mRecovered, mDeath, mConfirmedDelta, mDeceasedDelta, mRecoveredDelta, mActiveDelta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        pos = new JSONObject();

        recyclerView = findViewById(R.id.mainRecycler);
        constraintLayout = findViewById(R.id.constraintLayout2);

        mActive = findViewById(R.id.aValue);
        mConfirmed = findViewById(R.id.cValue);

        mRecovered = findViewById(R.id.rValue);
        mDeath = findViewById(R.id.dValue);

        mConfirmedDelta = findViewById(R.id.cDelta);
        mDeceasedDelta = findViewById(R.id.dDelta);
        mRecoveredDelta = findViewById(R.id.rDelta);
        mActiveDelta = findViewById(R.id.aDelta);

        Intent i = new Intent(getApplicationContext(), ScheduledService.class);
        getApplicationContext().startService(i);

        init(stateObj);
        animate(constraintLayout, ANIMATION_SPEED_MS_FAST);
    }

    private void init(JSONObject response) {
        try {

            jsonArray = response.getJSONArray("statewise");
            for (int i = 0; i < jsonArray.length(); i++) {
                state.add(jsonArray.getJSONObject(i).getString("state"));
                active.add(jsonArray.getJSONObject(i).getString("active"));
                confirmed.add(jsonArray.getJSONObject(i).getString("confirmed"));
                recovered.add(jsonArray.getJSONObject(i).getString("recovered"));
                death.add(jsonArray.getJSONObject(i).getString("deaths"));
                delta.add(jsonArray.getJSONObject(i).getJSONObject("delta").getString("confirmed"));
            }
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putInt("confirmed", Integer.parseInt(confirmed.get(0)));
            editor.putInt("confirmeddelta", Integer.parseInt(delta.get(0)));
            editor.apply();

            for (int j = 0; j < state.size(); j++) {
                if (j == 0) {
                    pos.put(state.get(j), 2);
                    Log.d("status object", state.get(j));
                } else if (Integer.parseInt(confirmed.get(j)) == 0) {
                    pos.put(state.get(j), 2);
                } else {
                    pos.put(state.get(j), 0);
                }
            }
            confirmedDelta = response.getJSONArray("key_values").getJSONObject(0).getString("confirmeddelta");
            recoveredDelta = response.getJSONArray("key_values").getJSONObject(0).getString("recovereddelta");
            deceasedDelta = response.getJSONArray("key_values").getJSONObject(0).getString("deceaseddelta");
            activeDelta = "";

            mActiveDelta.setText(activeDelta);//set Delta Values
            animate(mActiveDelta, ANIMATION_SPEED_MS_MEDIUM);//animate

            mConfirmedDelta.setText("+" + confirmedDelta);
            animate(mConfirmedDelta, ANIMATION_SPEED_MS_MEDIUM);

            mRecoveredDelta.setText("+" + recoveredDelta);
            animate(mRecoveredDelta, ANIMATION_SPEED_MS_MEDIUM);

            mDeceasedDelta.setText("+" + deceasedDelta);
            animate(mDeceasedDelta, ANIMATION_SPEED_MS_MEDIUM);


            mDeath.setText(death.get(0));//set Total Values
            animate(mDeath, ANIMATION_SPEED_MS_MEDIUM); //Animate

            mConfirmed.setText(confirmed.get(0));
            animate(mConfirmed, ANIMATION_SPEED_MS_MEDIUM);

            mRecovered.setText(recovered.get(0));
            animate(mRecovered, ANIMATION_SPEED_MS_MEDIUM);

            mActive.setText(active.get(0));
            animate(mActive, ANIMATION_SPEED_MS_MEDIUM);
            Log.d("adapter", state.toString());

            linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            adapter = new Adapter(HomeActivity.this, active, confirmed, recovered, state, death, delta, distObj, pos);
            recyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void animate(View view, int speed) {
        YoYo.with(Techniques.SlideInUp)
                .duration(speed)
                .repeat(0)
                .playOn(view);

    }

    public static void animateOut(View view, int speed) {
        YoYo.with(Techniques.SlideOutDown)
                .duration(speed)
                .repeat(0)
                .playOn(view);

    }
}
