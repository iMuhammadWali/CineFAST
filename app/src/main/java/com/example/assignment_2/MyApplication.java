package com.example.assignment_2;

import android.app.Application;

import java.util.ArrayList;

public class MyApplication extends Application {
    public static ArrayList<Movie> nowShowingMovies;
    public static ArrayList<Movie> comingSoonMovies;

    @Override
    public void onCreate() {
        super.onCreate();
        nowShowingMovies = new ArrayList<>();
        comingSoonMovies = new ArrayList<>();
    }
}
