package com.example.assignment_2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

// To pass data to choose seats fragment, I will have to load that fragment here in this activity.
public class MainActivity extends AppCompatActivity implements MovieListAdapter.OnHomeMovieClickListener {
    private FragmentManager manager;
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

        // Load the HomeFragment() just once.
        if (savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fContainer, new HomeFragment())
                    .commit();
        }
        manager = getSupportFragmentManager();
    }

    @Override
    public void onHomeBookSeatsClick(Movie m) {
        manager.beginTransaction()
                .replace(R.id.fContainer, ChooseSeatsFragment.newInstance(m))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onHomeTrailerClick(Movie m) {
        String link = m.trailerLink;
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
    }
}