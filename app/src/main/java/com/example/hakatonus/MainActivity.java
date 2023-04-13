package com.example.hakatonus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hakatonus.adapter.TravelAdapter_driver;
import com.example.hakatonus.model.TravelDriver;
import com.example.hakatonus.model.TravelPassenger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MapView map;
    private IMapController controller;
    private Marker startPoint = null;
    private Marker endPoint = null;
    private View top_container;
    private View buttons;
    private Polyline route;
    private RoadManager roadManager;
    private LinearLayout mBottomSheetLayout;
    RecyclerView driverRecycler;
    static TravelAdapter_driver travelAdapterDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue(getString(R.string.useragent));
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(getApplicationContext());

        map = findViewById(R.id.mapView);
        map.setMultiTouchControls(true);
        map.setBuiltInZoomControls(false);

        map.setTileSource(TileSourceFactory.MAPNIK);

        controller = map.getController();
        controller.setCenter(new GeoPoint(54.81640514945013f, 56.02013157238105));
        controller.setZoom(11.869000316368705);

        buttons = findViewById(R.id.buttons);
        top_container = findViewById(R.id.top_container);

        roadManager = new OSRMRoadManager(getApplicationContext(), getString(R.string.useragent));

        findViewById(R.id.setA).setOnClickListener(this::onClickA);
        findViewById(R.id.setB).setOnClickListener(this::onClickB);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("travels/drivers");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<String> keys = new ArrayList<>();

                for(DataSnapshot s : snapshot.getChildren()) {
                    keys.add(s.getKey());
                }

                load(keys, 0, new ArrayList<>());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        findViewById(R.id.startTravel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int role = getSharedPreferences("signin", MODE_PRIVATE).getInt("role", 0);
                if (role == 0) {
                    AlphaAnimation show = new AlphaAnimation(0, 1);
                    show.setDuration(300);
                    show.setFillAfter(true);

                    findViewById(R.id.driver_create).startAnimation(show);
                    findViewById(R.id.driver_create).setVisibility(View.VISIBLE);
                    findViewById(R.id.driver_create).setEnabled(true);

                    EditText model = findViewById(R.id.driver_create).findViewById(R.id.model);
                    EditText capacity = findViewById(R.id.driver_create).findViewById(R.id.capacity);
                    EditText time = findViewById(R.id.driver_create).findViewById(R.id.time);
                    EditText date = findViewById(R.id.driver_create).findViewById(R.id.date);

                    model.setEnabled(true);
                    capacity.setEnabled(true);
                    time.setEnabled(true);
                    date.setEnabled(true);

                    findViewById(R.id.driver_create).findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlphaAnimation hide = new AlphaAnimation(1, 0);
                            hide.setDuration(300);
                            hide.setFillAfter(true);

                            findViewById(R.id.driver_create).startAnimation(hide);
                            findViewById(R.id.driver_create).setVisibility(View.INVISIBLE);

                            model.setText("");
                            capacity.setText("");
                            time.setText("");
                            date.setText("");
                        }
                    });

                    findViewById(R.id.driver_create).findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("travels/drivers");

                            HashMap<String, Object> travel = new HashMap<>();
                            travel.put("model", model.getText().toString());
                            travel.put("capacity", capacity.getText().toString());
                            travel.put("time", time.getText().toString());
                            travel.put("date", date.getText().toString());
                            travel.put("start", String.valueOf(startPoint.getPosition().getLatitude()) + ";" + String.valueOf(startPoint.getPosition().getLongitude()));
                            travel.put("end", String.valueOf(endPoint.getPosition().getLatitude()) + ";" + String.valueOf(endPoint.getPosition().getLongitude()));

                            ArrayList<String> members = new ArrayList<String>();
                            members.add(FirebaseAuth.getInstance().getCurrentUser().getUid());

                            travel.put("members", members);

                            DatabaseReference tr = ref.push();

                            tr.setValue(travel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Отправлено", Toast.LENGTH_SHORT).show();
                                        AlphaAnimation hide = new AlphaAnimation(1, 0);
                                        hide.setDuration(300);
                                        hide.setFillAfter(true);

                                        findViewById(R.id.driver_create).startAnimation(hide);
                                        findViewById(R.id.driver_create).setVisibility(View.GONE);
                                        findViewById(R.id.driver_create).setEnabled(false);

                                        ((ViewGroup) findViewById(R.id.driver_create).getParent()).removeView(findViewById(R.id.driver_create));

                                        model.setEnabled(false);
                                        capacity.setEnabled(false);
                                        time.setEnabled(false);
                                        date.setEnabled(false);
                                    } else {
                                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });

        mBottomSheetLayout = findViewById(R.id.bottom_sheet_layout);
    }

    public void load(ArrayList<String> keys, int index, ArrayList<TravelDriver> driverList) {
        if(index == keys.size()) {
            setDriverRecycler(driverList);
        }
        TravelDriver t = new TravelDriver();
        FirebaseDatabase.getInstance().getReference("travels/drivers").child(keys.get(index)).child("model").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                t.model = snapshot.getValue(String.class);

                FirebaseDatabase.getInstance().getReference("travels/drivers").child(keys.get(index)).child("capacity").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        t.capacity = snapshot.getValue(String.class);

                        FirebaseDatabase.getInstance().getReference("travels/drivers").child(keys.get(index)).child("time").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                t.time = snapshot.getValue(String.class);
                                FirebaseDatabase.getInstance().getReference("travels/drivers").child(keys.get(index)).child("date").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        t.date = snapshot.getValue(String.class);
                                        driverList.add(t);
                                        load(keys, index+1, driverList);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void Route() {
        if (startPoint != null && endPoint != null) {
            map.getOverlays().remove(route);

            ArrayList<GeoPoint> points = new ArrayList<GeoPoint>();
            points.add(startPoint.getPosition());
            points.add(endPoint.getPosition());

            Road road = roadManager.getRoad(points);

            route = RoadManager.buildRoadOverlay(road);

            route.getOutlinePaint().setColor(getColor(R.color.bg1));
            route.getOutlinePaint().setStrokeWidth(10f);
            route.getOutlinePaint().setStrokeCap(Paint.Cap.ROUND);

            map.getOverlays().add(route);
            map.invalidate();


        }
    }

    private void setDriverRecycler(List<TravelDriver> driverList) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        driverRecycler = findViewById(R.id.driverRecycler);
        driverRecycler.setLayoutManager(layoutManager);

        travelAdapterDriver = new TravelAdapter_driver(this, driverList);
        driverRecycler.setAdapter(travelAdapterDriver);

    }

    public void onClickA(View view) {

        Log.e("map params", String.valueOf(map.getMapCenter().getLatitude()) +
                String.valueOf(map.getMapCenter().getLongitude())
                + "; " + String.valueOf(map.getZoomLevelDouble()));

        if (startPoint != null) {
            controller.animateTo(startPoint.getPosition());
            return;
        }

        findViewById(R.id.setA).setOnClickListener(null);
        findViewById(R.id.setB).setOnClickListener(null);

        TranslateAnimation hideBs = new TranslateAnimation(0,
                buttons.getMeasuredWidth() + 20 * getResources().getDisplayMetrics().density, 0, 0);
        hideBs.setDuration(300);
        hideBs.setFillAfter(true);
        buttons.startAnimation(hideBs);
        buttons.setVisibility(View.GONE);

        TranslateAnimation showTop = new TranslateAnimation(0, 0, -top_container.getMeasuredHeight(), 0);
        showTop.setDuration(300);
        showTop.setFillAfter(true);
        top_container.startAnimation(showTop);
        top_container.setVisibility(View.VISIBLE);

        Marker temp = new Marker(map);
        temp.setPosition((GeoPoint) map.getMapCenter());
        temp.setIcon(getDrawable(R.drawable.marker_a_icon));

        map.getOverlays().add(temp);
        map.invalidate();

        temp.setDraggable(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        findViewById(R.id.no).setOnClickListener(null);
                        findViewById(R.id.yes).setOnClickListener(null);

                        TranslateAnimation showBs = new TranslateAnimation(buttons.getMeasuredWidth() + 20 * getResources().getDisplayMetrics().density,
                                0, 0, 0);
                        showBs.setDuration(300);
                        showBs.setFillAfter(true);
                        buttons.startAnimation(showBs);
                        buttons.setVisibility(View.VISIBLE);

                        TranslateAnimation hideTop = new TranslateAnimation(0, 0, 0, -top_container.getMeasuredHeight());
                        hideTop.setDuration(300);
                        hideTop.setFillAfter(true);
                        top_container.startAnimation(hideTop);
                        top_container.setVisibility(View.GONE);

                        map.getOverlays().remove(temp);
                        map.invalidate();

                        findViewById(R.id.setA).setOnClickListener(MainActivity.this::onClickA);
                        findViewById(R.id.setB).setOnClickListener(MainActivity.this::onClickB);
                    }
                });

                findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        findViewById(R.id.no).setOnClickListener(null);
                        findViewById(R.id.yes).setOnClickListener(null);

                        TranslateAnimation showBs = new TranslateAnimation(buttons.getMeasuredWidth() + 20 * getResources().getDisplayMetrics().density,
                                0, 0, 0);
                        showBs.setDuration(300);
                        showBs.setFillAfter(true);
                        buttons.startAnimation(showBs);
                        buttons.setVisibility(View.VISIBLE);

                        TranslateAnimation hideTop = new TranslateAnimation(0, 0, 0, -top_container.getMeasuredHeight());
                        hideTop.setDuration(300);
                        hideTop.setFillAfter(true);
                        top_container.startAnimation(hideTop);
                        top_container.setVisibility(View.GONE);

                        startPoint = temp;

                        if(endPoint != null)
                            findViewById(R.id.startTravel).setVisibility(View.VISIBLE);
                        new updateRoute().execute();

                        startPoint.setOnMarkerDragListener(new Marker.OnMarkerDragListener() {
                            @Override
                            public void onMarkerDrag(Marker marker) {

                            }

                            @Override
                            public void onMarkerDragEnd(Marker marker) {
                                new updateRoute().execute();
                            }

                            @Override
                            public void onMarkerDragStart(Marker marker) {

                            }
                        });

                        findViewById(R.id.setA).setOnClickListener(MainActivity.this::onClickA);
                        findViewById(R.id.setB).setOnClickListener(MainActivity.this::onClickB);
                    }
                });
            }
        }, 100);
    }

    public void onClickB(View view) {
        if (endPoint != null) {
            controller.animateTo(endPoint.getPosition());
            return;
        }

        findViewById(R.id.setA).setOnClickListener(null);
        findViewById(R.id.setB).setOnClickListener(null);

        TranslateAnimation hideBs = new TranslateAnimation(0,
                buttons.getMeasuredWidth() + 20 * getResources().getDisplayMetrics().density, 0, 0);
        hideBs.setDuration(300);
        hideBs.setFillAfter(true);
        buttons.startAnimation(hideBs);
        buttons.setVisibility(View.GONE);

        TranslateAnimation showTop = new TranslateAnimation(0, 0, -top_container.getMeasuredHeight(), 0);
        showTop.setDuration(300);
        showTop.setFillAfter(true);
        top_container.startAnimation(showTop);
        top_container.setVisibility(View.VISIBLE);

        Marker temp = new Marker(map);
        temp.setPosition((GeoPoint) map.getMapCenter());
        temp.setIcon(getDrawable(R.drawable.marker_b_icon));

        map.getOverlays().add(temp);
        map.invalidate();

        temp.setDraggable(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        findViewById(R.id.no).setOnClickListener(null);
                        findViewById(R.id.yes).setOnClickListener(null);

                        TranslateAnimation showBs = new TranslateAnimation(buttons.getMeasuredWidth() + 20 * getResources().getDisplayMetrics().density,
                                0, 0, 0);
                        showBs.setDuration(300);
                        showBs.setFillAfter(true);
                        buttons.startAnimation(showBs);
                        buttons.setVisibility(View.VISIBLE);

                        TranslateAnimation hideTop = new TranslateAnimation(0, 0, 0, -top_container.getMeasuredHeight());
                        hideTop.setDuration(300);
                        hideTop.setFillAfter(true);
                        top_container.startAnimation(hideTop);
                        top_container.setVisibility(View.GONE);

                        map.getOverlays().remove(temp);
                        map.invalidate();

                        findViewById(R.id.setA).setOnClickListener(MainActivity.this::onClickA);
                        findViewById(R.id.setB).setOnClickListener(MainActivity.this::onClickB);
                    }
                });

                findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        findViewById(R.id.no).setOnClickListener(null);
                        findViewById(R.id.yes).setOnClickListener(null);

                        TranslateAnimation showBs = new TranslateAnimation(buttons.getMeasuredWidth() + 20 * getResources().getDisplayMetrics().density,
                                0, 0, 0);
                        showBs.setDuration(300);
                        showBs.setFillAfter(true);
                        buttons.startAnimation(showBs);
                        buttons.setVisibility(View.VISIBLE);

                        TranslateAnimation hideTop = new TranslateAnimation(0, 0, 0, -top_container.getMeasuredHeight());
                        hideTop.setDuration(300);
                        hideTop.setFillAfter(true);
                        top_container.startAnimation(hideTop);
                        top_container.setVisibility(View.GONE);

                        endPoint = temp;
                        new updateRoute().execute();

                        if(startPoint != null)
                            findViewById(R.id.startTravel).setVisibility(View.VISIBLE);

                        endPoint.setOnMarkerDragListener(new Marker.OnMarkerDragListener() {
                            @Override
                            public void onMarkerDrag(Marker marker) {

                            }

                            @Override
                            public void onMarkerDragEnd(Marker marker) {
                                new updateRoute().execute();
                            }

                            @Override
                            public void onMarkerDragStart(Marker marker) {

                            }
                        });
                        findViewById(R.id.setA).setOnClickListener(MainActivity.this::onClickA);
                        findViewById(R.id.setB).setOnClickListener(MainActivity.this::onClickB);
                    }
                });
            }
        }, 100);
    }


    public class updateRoute extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            Route();
            return "done";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        map.onResume();
    }

    @Override
    public void onBackPressed() {
        MyDialogFragment d = new MyDialogFragment();
        d.show(getSupportFragmentManager(), "exit");
    }

    public void okClicked() {
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку OK!",
                Toast.LENGTH_LONG).show();
    }

    public void cancelClicked() {
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку отмены!",
                Toast.LENGTH_LONG).show();
    }
}

