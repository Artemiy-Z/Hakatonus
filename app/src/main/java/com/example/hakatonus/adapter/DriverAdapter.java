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
import com.example.hakatonus.model.Driver;

import java.util.List;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.DriverViewHolder> {


    Context context;
    List<Driver> drivers;

    public DriverAdapter(Context context, List<Driver> drivers) {
        this.context = context;
        this.drivers = drivers;
    }

    @NonNull
    @Override
    public DriverAdapter.DriverViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View driverItems = LayoutInflater.from(context).inflate(R.layout.driver_item,parent,false);
        return new DriverAdapter.DriverViewHolder(driverItems);
    }

    @Override
    public void onBindViewHolder( DriverAdapter.DriverViewHolder holder, int position) {

        int imageId = context.getResources().getIdentifier(drivers.get(position).getImg(),"drawable",context.getPackageName());

        holder.driverImage.setImageResource(imageId);
        holder.txt_FIO.setText(drivers.get(position).getTxt_FIO());
        holder.txt_kyda.setText(drivers.get(position).getTxt_kyda());
        holder.txt_otkyda.setText(drivers.get(position).getTxt_otkyda());

    }

    @Override
    public int getItemCount() {
        return drivers.size();
    }

    public static final class DriverViewHolder extends RecyclerView.ViewHolder {


        ImageView driverImage;
        TextView txt_FIO, txt_kyda, txt_otkyda;

        public DriverViewHolder(@NonNull View itemView) {
            super(itemView);

            driverImage = itemView.findViewById(R.id.driverImage);
            txt_FIO = itemView.findViewById(R.id.txt_FIO);
            txt_kyda = itemView.findViewById(R.id.txt_kyda);
            txt_otkyda = itemView.findViewById(R.id.txt_otkyda);

        }
    }
}
