package com.example.hakatonus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.hakatonus.adapter.DriverAdapter;
import com.example.hakatonus.model.Driver;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    private LinearLayout mBottomSheetLayout;
    private BottomSheetBehavior sheetBehavior;
    private ImageView header_Arrow_Image;
    RecyclerView driverRecycler;
    static DriverAdapter driverAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        List<Driver> driverList = new ArrayList<>();
        driverList.add(new Driver(1,"test","Зубенко Михаил Петрович","Питер","Магадан"));
        driverList.add(new Driver(2,"test2","Иванов Иван Иванович", "Казань", "Уфа"));
       setDriverRecycler(driverList);



        mBottomSheetLayout = findViewById(R.id.bottom_sheet_layout);
        sheetBehavior = BottomSheetBehavior.from(mBottomSheetLayout);
        header_Arrow_Image = findViewById(R.id.bottom_sheet_arrow);

        header_Arrow_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            }
        });
    }
    private  void setDriverRecycler(List<Driver> driverList){

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        driverRecycler = findViewById(R.id.driverRecycler);
        driverRecycler.setLayoutManager(layoutManager);

        driverAdapter = new DriverAdapter(this, driverList);
        driverRecycler.setAdapter(driverAdapter);

    }
}