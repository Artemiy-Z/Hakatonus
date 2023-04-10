package com.example.hakatonus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import java.util.ArrayList;

public class IntroActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        ListPagerAdapter adapter = new ListPagerAdapter(this);
        adapter.addFragment(new IntroFragment(getLayoutInflater().inflate(R.layout.intro_1,null)));
        adapter.addFragment(new IntroFragment(getLayoutInflater().inflate(R.layout.intro_2,null)));

        ViewPager2 vp = findViewById(R.id.viewpager);
        vp.setAdapter(adapter);

        AlphaAnimation hideA = new AlphaAnimation(1.0f, 0.0f);
        hideA.setDuration(150);
        hideA.setFillAfter(true);
        AlphaAnimation showA = new AlphaAnimation(0.0f, 1.0f);
        showA.setDuration(150);
        showA.setFillAfter(true);

        AlphaAnimation hideB = new AlphaAnimation(1.0f, 0.0f);
        hideB.setDuration(150);
        hideB.setFillAfter(true);
        AlphaAnimation showB = new AlphaAnimation(0.0f, 1.0f);
        showB.setDuration(150);
        showB.setFillAfter(true);

        AlphaAnimation hideC = new AlphaAnimation(1.0f, 0.0f);
        hideC.setDuration(150);
        hideC.setFillAfter(true);
        AlphaAnimation showC = new AlphaAnimation(0.0f, 1.0f);
        showC.setDuration(150);
        showC.setFillAfter(true);

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