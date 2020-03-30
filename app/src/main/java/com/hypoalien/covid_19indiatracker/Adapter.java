package com.hypoalien.covid_19indiatracker;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.hypoalien.covid_19indiatracker.HomeActivity.ANIMATION_SPEED_MS_FAST;
import static com.hypoalien.covid_19indiatracker.HomeActivity.ANIMATION_SPEED_MS_MEDIUM;
import static com.hypoalien.covid_19indiatracker.HomeActivity.ANIMATION_SPEED_MS_SLOW;
import static com.hypoalien.covid_19indiatracker.HomeActivity.animate;
import static com.hypoalien.covid_19indiatracker.HomeActivity.animateOut;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    private Activity activity;
    private LayoutInflater layoutInflater;
    private ArrayList<String > aActive;
    private ArrayList<String> aConfirmed;
    private ArrayList<String > aRecovered;
    private ArrayList<String> aState;
    private ArrayList<String > aDeath;
    private ArrayList<String > aDelta;


    JSONObject aStatus;
    JSONObject jsonObject;
    JSONArray districtConfirmed=new JSONArray();
    JSONArray districtName=new JSONArray();

    Adapter(Activity activity, ArrayList<String>aActive,ArrayList<String>aConfirmed,ArrayList<String>aRecovered,ArrayList<String>aState,ArrayList<String>aDeath,ArrayList<String>aDelta,JSONObject jsonObject,JSONObject aStatus ){
        this.layoutInflater=LayoutInflater.from(activity);
        this.aActive=aActive;
        this.aConfirmed=aConfirmed;
        this.aRecovered=aRecovered;
        this.aState=aState;
        this.aDeath=aDeath;
        this.aDelta=aDelta;
        this.jsonObject=jsonObject;
        this.aStatus=aStatus;
        this.activity=activity;
        this.jsonObject=jsonObject;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =layoutInflater.inflate(R.layout.recycler,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.textState.setText(aState.get(position));
        animate(holder.textState,ANIMATION_SPEED_MS_SLOW);
        holder.textDeath.setText(aDeath.get(position));
        animate(holder.textDeath,ANIMATION_SPEED_MS_SLOW);
        holder.textRecovered.setText(aRecovered.get(position));
        animate(holder.textRecovered,ANIMATION_SPEED_MS_SLOW);
        holder.textConfirmed.setText(aConfirmed.get(position));
        animate(holder.textConfirmed,ANIMATION_SPEED_MS_SLOW);
        holder.textActive.setText(aActive.get(position));
        animate(holder.textActive,ANIMATION_SPEED_MS_SLOW);
        if(!aDelta.get(position).equals("0")){
            holder.textDelta.setText("+"+aDelta.get(position));
            animate(holder.textDelta,ANIMATION_SPEED_MS_SLOW);
        }

        try {
            districtName=jsonObject.getJSONObject(aState.get(position)).getJSONObject("districtData").names();
            for(int i=0;i<districtName.length();i++){
                districtConfirmed.put(jsonObject.getJSONObject(aState.get(position)).getJSONObject("districtData").getJSONObject(districtName.getString(i)).getString("confirmed"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(aState.get(position)!="Total"){
            SubAdapter subAdapter=new SubAdapter(activity,districtName,districtConfirmed);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            holder.subRecycler.setLayoutManager(linearLayoutManager);
            holder.subRecycler.setAdapter(subAdapter);
            districtConfirmed=new JSONArray();
            districtName=new JSONArray();
        }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(aStatus.getInt(aState.get(position))==2){
                        holder.subRecycler.setVisibility(View.GONE);
                    }
                    else if(aStatus.getInt(aState.get(position))==0){
                        holder.subRecycler.setVisibility(View.VISIBLE);
                        animate(holder.subRecycler,ANIMATION_SPEED_MS_FAST);
                        holder.imageView.setImageResource(R.drawable.ic_expand_more_black_24dp);
                        animate(holder.imageView,ANIMATION_SPEED_MS_MEDIUM);
                        aStatus.put(aState.get(position),1);
                    }
                    else if(aStatus.getInt(aState.get(position))==1){
                        holder.subRecycler.setVisibility(View.GONE);
                        animateOut(holder.subRecycler,ANIMATION_SPEED_MS_FAST);
                        holder.imageView.setImageResource(R.drawable.ic_chevron_right_black_24dp);
                        animate(holder.imageView,ANIMATION_SPEED_MS_MEDIUM);
                        aStatus.put(aState.get(position),0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return aState.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textState,textActive,textConfirmed,textRecovered,textDeath,textDelta;
        RecyclerView subRecycler;
        LinearLayout linearLayout;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textState=itemView.findViewById(R.id.rvState);
            textActive=itemView.findViewById(R.id.rvActive);
            textConfirmed=itemView.findViewById(R.id.rvConfirmed);
            textRecovered=itemView.findViewById(R.id.rvRecovered);
            textDeath=itemView.findViewById(R.id.rvDeceased);
            textDelta=itemView.findViewById(R.id.cDelta);
            subRecycler=itemView.findViewById(R.id.sub_rv);
            linearLayout=itemView.findViewById(R.id.linear_layout_horizontal);
            imageView=itemView.findViewById(R.id.imageView);
        }
    }



}
