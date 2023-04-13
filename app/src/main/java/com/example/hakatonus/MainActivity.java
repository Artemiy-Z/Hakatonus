package com.example.hakatonus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MapView map;
    private IMapController controller;
    private Marker startPoint = null;
    private Marker endPoint = null;
    private View top_container;
    private View buttons;
    private Polyline route;
    private RoadManager roadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue(getString(R.string.useragent));
        setContentView(R.layout.activity_main);

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
    }

    public void onClickA(View view) {

        Log.e("map params", String.valueOf(map.getMapCenter().getLatitude()) +
                String.valueOf(map.getMapCenter().getLongitude())
                + "; " + String.valueOf(map.getZoomLevelDouble()));

        if(startPoint != null) {
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
        if(endPoint != null) {
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

    public void Route() {
        if(startPoint != null && endPoint != null) {
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

