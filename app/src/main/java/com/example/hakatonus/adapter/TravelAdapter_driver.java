package com.example.hakatonus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hakatonus.R;
import com.example.hakatonus.model.TravelDriver;
import com.example.hakatonus.model.TravelPassenger;

import java.util.List;

public class TravelAdapter_driver extends RecyclerView.Adapter<TravelAdapter_driver.DriverViewHolder> {


    Context context;
    List<TravelDriver> drivers;

    public TravelAdapter_driver(Context context, List<TravelDriver> drivers) {
        this.context = context;
        this.drivers = drivers;
    }

    @NonNull
    @Override
    public TravelAdapter_driver.DriverViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View driverItems = LayoutInflater.from(context).inflate(R.layout.driver_item,parent,false);
        return new TravelAdapter_driver.DriverViewHolder(driverItems);
    }

    @Override
    public void onBindViewHolder(TravelAdapter_driver.DriverViewHolder holder, int position) {

        holder.model.setText(drivers.get(position).model);
        holder.capacity.setText(drivers.get(position).capacity);
        holder.time.setText(drivers.get(position).time);
        holder.date.setText(drivers.get(position).date);

    }

    @Override
    public int getItemCount() {
        return drivers.size();
    }

    public static final class DriverViewHolder extends RecyclerView.ViewHolder {
        TextView model, capacity, time, date;

        public DriverViewHolder(@NonNull View itemView) {
            super(itemView);

            model = itemView.findViewById(R.id.model);
            capacity = itemView.findViewById(R.id.capacity);
            time = itemView.findViewById(R.id.time);
            date = itemView.findViewById(R.id.date);

        }
    }
}
