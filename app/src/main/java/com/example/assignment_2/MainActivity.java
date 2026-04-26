package com.example.assignment_2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        setupListeners();
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open_drawer,
                R.string.close_drawer
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        View headerView = navigationView.getHeaderView(0);
        TextView tvUsername = headerView.findViewById(R.id.tvUsername);

        TextView tvEmail = headerView.findViewById(R.id.tvUserEmail);
        tvEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference()
                .child("users").child(FirebaseAuth.getInstance().getUid());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = snapshot.child("username").getValue(String.class);
                tvUsername.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                For now, I have no clue what is onCancelled.
            }
        });


        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        if (savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fContainer, new HomeFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.navHome);
            getSupportActionBar().setTitle("Home");
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selected = null;
                int id = menuItem.getItemId();
                if (id == R.id.navHome){
                    selected = new HomeFragment();
                    setDrawerNavigation();
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    getSupportActionBar().setTitle("Home");
                }
                else if (id == R.id.navMyBookings){
                    selected = new MyBookingsFragment();
                    setDrawerNavigation();
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    getSupportActionBar().setTitle("My Bookings");
                }
                else if (id == R.id.navLogout){
                    FirebaseAuth.getInstance().signOut();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    return true;
                }
                // I am not adding to back stack here as I dont think it is viable.
                if (selected != null){
                    manager.beginTransaction()
                            .replace(R.id.fContainer, selected)
                            .commit();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
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
    }
    @Override
    public void openChooseSeats(Movie m) {
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
    public void openTicketSummary(Movie movie, ArrayList<String> selectedSeats, ArrayList<SelectedSnack> selectedSnacks) {
        TicketSummaryFragment fragment = TicketSummaryFragment.newInstance(movie, selectedSeats, selectedSnacks);
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
        manager = getSupportFragmentManager();
    }
    private void setupListeners(){
        manager.addOnBackStackChangedListener(() -> {
            Fragment current = getSupportFragmentManager()
                    .findFragmentById(R.id.fContainer);

            String title = null;
            if (current instanceof HomeFragment) {
                title = "Home";
                setDrawerNavigation();
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
            else if (current instanceof ChooseSnacksFragment) {
                title = "Choose Snacks";
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
            else if (current instanceof ChooseSeatsFragment) {
                title = "Choose Seats";
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
            else if (current instanceof TicketSummaryFragment){
                title = "Ticket Summary";
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
            else if (current instanceof MyBookingsFragment){
                title = "My Bookings";
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

            if(getSupportActionBar()!=null)
            {
                getSupportActionBar().setTitle(title);
            }
        });
    }
}