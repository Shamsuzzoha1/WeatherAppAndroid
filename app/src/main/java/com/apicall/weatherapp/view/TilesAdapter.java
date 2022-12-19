package com.apicall.weatherapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apicall.weatherapp.R;
import com.apicall.weatherapp.model.Hour;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public class TilesAdapter extends RecyclerView.Adapter<TilesAdapter.ViewHolder> {
    private List<Hour> hourList;
    private final Context vhContext;

    public TilesAdapter(Context c, List<Hour> hourList) {
        this.hourList = hourList;
        this.vhContext = c;
    }

    public void setData(List<Hour> hourList){
        this.hourList = hourList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTime;
        private TextView tvTemperature;
        private TextView tvWindspeed;
        private ImageView ivCondition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.idTVTime);
            tvTemperature = itemView.findViewById(R.id.idTVTemperature);
            tvWindspeed = itemView.findViewById(R.id.idTVWindSpeed);
            ivCondition = itemView.findViewById(R.id.idIVCondition);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item
        ,parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // write code for displaying api data
        Hour hour = hourList.get(position);
        String conditionIcon = "https:"+hour.getCondition().getIcon();


        holder.tvTime.setText(String.valueOf(hour.getTime()));
        holder.tvTemperature.setText(String.valueOf(hour.getTempC())+" °C");
        Util.loadImage(holder.ivCondition, conditionIcon);
        holder.tvWindspeed.setText(String.valueOf(hour.getWindKph())+"KMPH");
    }

    @Override
    public int getItemCount() {
        return hourList.size();
    }
}
