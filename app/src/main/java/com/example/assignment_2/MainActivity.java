package com.example.assignment_2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationManager {
    private FragmentManager manager;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

        toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open_drawer,
                R.string.close_drawer
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        manager = getSupportFragmentManager();
        if (savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fContainer, new HomeFragment())
                    .commit();
        }

    }
    private void setBackNavigation() {
        toggle.setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void setDrawerNavigation() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        toolbar.setNavigationOnClickListener(v -> {
            drawerLayout.open();
        });
    }
    @Override
    public void makeToolBarVisible() {
        toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void openPreviousFragment() {
        manager.popBackStack();

        if (manager.getBackStackEntryCount() <= 1) {
            getSupportActionBar().setTitle("Home");
            setDrawerNavigation();
        }
    }

    @Override
    public void openChooseSeats(Movie m) {
        getSupportActionBar().setTitle("Choose Seats");
        setBackNavigation();
        manager.beginTransaction()
                .replace(R.id.fContainer, ChooseSeatsFragment.newInstance(m))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void openTrailer(String trailerLink) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailerLink)));
    }

    @Override
    public void openTicketSummary(Movie movie, ArrayList<String> selectedSeats) {
        TicketSummaryFragment fragment = TicketSummaryFragment.newInstance(movie, selectedSeats, null);
            manager.beginTransaction()
                .replace(R.id.fContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void openSnacks(Movie movie, ArrayList<String> selectedSeats) {
        ChooseSnacksFragment fragment = ChooseSnacksFragment.newInstance(movie, selectedSeats);
                manager
                .beginTransaction()
                .replace(R.id.fContainer, fragment)
                .addToBackStack(null)
                .commit();
    }


    private void init(){
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.main);
        navigationView = findViewById(R.id.navigationView);
    }
}