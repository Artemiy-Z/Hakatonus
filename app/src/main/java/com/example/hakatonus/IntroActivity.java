package com.example.hakatonus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;

import com.google.firebase.FirebaseApp;

import java.util.ArrayList;

public class IntroActivity extends FragmentActivity {

    private AppCompatButton prevB;
    private AppCompatButton nextB;
    private AppCompatButton btn_passenger;
    private AppCompatButton btn_driver;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        ListPagerAdapter adapter = new ListPagerAdapter(this);
        adapter.addFragment(new IntroFragment(getLayoutInflater().inflate(R.layout.intro_1, null)));
        adapter.addFragment(new IntroFragment(getLayoutInflater().inflate(R.layout.intro_2, null)));

        ViewPager2 vp = findViewById(R.id.viewpager);
        vp.setAdapter(adapter);


        prevB = findViewById(R.id.prevB);
        nextB = findViewById(R.id.nextB);
        btn_driver = findViewById(R.id.btn_driver);
        btn_passenger = findViewById(R.id.btn_passenger);

        vp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position == 0) {
                    prevB.setVisibility(View.INVISIBLE);
                    btn_passenger.setVisibility(View.INVISIBLE);
                    nextB.setVisibility(View.VISIBLE);
                    btn_driver.setVisibility(View.VISIBLE);
                } else if (position == 1) {
                    nextB.setVisibility(View.INVISIBLE);
                    btn_driver.setVisibility(View.INVISIBLE);
                    prevB.setVisibility(View.VISIBLE);
                    btn_passenger.setVisibility(View.VISIBLE);

                }
            }

        });

        prevB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vp.getCurrentItem() != 0)
                    vp.setCurrentItem(vp.getCurrentItem() - 1);
            }

        });

        nextB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vp.getCurrentItem() != 1)
                    vp.setCurrentItem(vp.getCurrentItem() + 1);
            }
        });

        btn_passenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences("signin",MODE_PRIVATE).edit().putInt("role",1).apply();
                startActivity(new Intent(IntroActivity.this,RegisterToPassenger.class));
                finish();
            }
        });

        btn_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences("signin",MODE_PRIVATE).edit().putInt("role",0).apply();
                startActivity(new Intent(IntroActivity.this,RegisterToDriver.class));
                finish();
            }
        });


    }

    public static class IntroFragment extends Fragment {
        private View view;

        public IntroFragment(View layout) {
            this.view = layout;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return view;
        }
    }

    private static class ListPagerAdapter extends FragmentStateAdapter {

        private ArrayList<Fragment> list;

        public ListPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);

            list = new ArrayList<>();
        }

        public void addFragment(Fragment f) {
            list.add(f);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return list.get(position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}