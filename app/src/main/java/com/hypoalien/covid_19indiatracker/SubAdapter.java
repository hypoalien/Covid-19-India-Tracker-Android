package com.hypoalien.covid_19indiatracker;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.hypoalien.covid_19indiatracker.HomeActivity.ANIMATION_SPEED_MS_MEDIUM;
import static com.hypoalien.covid_19indiatracker.HomeActivity.animate;


public class SubAdapter extends RecyclerView.Adapter<SubAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private JSONArray mDistrict;
    private JSONArray mConfirmed;

    SubAdapter(Activity activity, JSONArray mDistrict, JSONArray mConfirmed){
        this.layoutInflater=LayoutInflater.from(activity);
        this.mDistrict=mDistrict;
        this.mConfirmed =mConfirmed;
    }

    @NonNull
    @Override
    public SubAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =layoutInflater.inflate(R.layout.sub_recycler,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubAdapter.ViewHolder holder, int position) {
        try {
            if(position!=0){
                holder.textDistrict.setText(mDistrict.get(position-1).toString());
                animate(holder.textDistrict,ANIMATION_SPEED_MS_MEDIUM);
                holder.textConfirmed.setText(mConfirmed.get(position-1).toString());
                animate(holder.textConfirmed,ANIMATION_SPEED_MS_MEDIUM);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mDistrict.length()+1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textDistrict,textConfirmed;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textDistrict=itemView.findViewById(R.id.subRvDist);
            textConfirmed=itemView.findViewById(R.id.subRvCon);
        }
    }
}
