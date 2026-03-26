package com.example.assignment_2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

//TODO: Understand the lifecycle of the Fragment (and hopefully, of an Activity again)


// Notes:-
// FragmentViewAdapter creates the right fragment for each position
// ViewPager2 displays whatever fragment adapter gives it
// TabLayoutModerator tells ViewPager2 the current position

public class HomeFragment extends Fragment {
    TabLayout tl;
    ViewPager2 vp2;
    ViewPagerAdapter adapter;
    TabLayoutMediator mediator;
    public HomeFragment() {
        // Required empty public constructor
    }
    //TODO: Understand Layout Inflation [Done]
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        init(v);
        return v;
    }
    private void init(View v){
        tl = v.findViewById(R.id.tl);
        vp2 = v.findViewById(R.id.vp2);
        adapter = new ViewPagerAdapter(requireActivity());
        vp2.setAdapter(adapter);

        mediator = new TabLayoutMediator(
                tl,
                vp2,
                (tab, pos)->{
                    switch (pos){
                        case 0:
                            tab.setText("Now Showing");
                            break;
                        case 1:
                            tab.setText("Coming Soon");
                    }
                }
        );
        mediator.attach();
    }
}