package com.example.assignment_2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


// Notes:-
// FragmentViewAdapter creates the right fragment for each position
// ViewPager2 displays whatever fragment adapter gives it
// TabLayoutModerator tells ViewPager2 the current position

public class HomeFragment extends Fragment {
    TabLayout tl;
    ViewPager2 vp2;
    ViewPagerAdapter adapter;
    TabLayoutMediator mediator;
    ImageView ivMenu;

    public HomeFragment() {
        // Required empty public constructor
    }
    //TODO: Understand Layout Inflation [Done]
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setupUi();
    }
    private void setupUi() {
        ivMenu.setOnClickListener(v -> {
            final String[] options = {"View Last Booking"};

            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle("Menu");

            builder.setItems(options, (dialog, which) -> {
                if (which == 0) {
                    SharedPreferences prefs =
                            requireActivity().getSharedPreferences("bookingPrefs", Context.MODE_PRIVATE);

                    String movie = prefs.getString("movieTitle", null);
                    int seats = prefs.getInt("seatCount", -1);
                    float total = prefs.getFloat("totalPrice", -1f);

                    AlertDialog.Builder infoBuilder = new AlertDialog.Builder(requireActivity());

                    if (movie == null || seats < 0 || total < 0f) {
                        infoBuilder.setTitle("Last Booking");
                        infoBuilder.setMessage("No previous booking found.");
                    } else {
                        String message = "Movie: " + movie + "\n" +
                                "Seats: " + seats + "\n" +
                                "Total Price: $" + total;
                        infoBuilder.setTitle("Last Booking");
                        infoBuilder.setMessage(message);
                    }

                    infoBuilder.setPositiveButton("OK", (d, w) -> d.dismiss());
                    infoBuilder.show();
                }
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            builder.show();
        });
    }
    private void init(View v){
        tl = v.findViewById(R.id.tl);
        vp2 = v.findViewById(R.id.vp2);
        ivMenu = v.findViewById(R.id.ivMenu);
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