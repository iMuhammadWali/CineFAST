package com.example.assignment_2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class NowShowingFragment extends Fragment {
    RecyclerView rv;
    ArrayList<Movie> movies;
    MovieAdapter adapter;
    public NowShowingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_now_showing, container, false);
        init(v);
        return v;
    }
    private void populateMovies(){
        movies.add(new Movie(R.drawable.movie_detective_conan,"Detective Conan: One-Eyed Flashback", "Action / Mystery", "No Link", false));
        movies.add(new Movie(R.drawable.movie_dragon_ball,"Dragon Ball Super: Broly", "Action", "No Link", false));
        movies.add(new Movie(R.drawable.movie_dune,"Dune", "Action", "No Link", false));
        movies.add(new Movie(R.drawable.movie_before_sunset,"Before Sunset", "Romance", "No Link", false));
    }
    private void init(View v){
        rv = v.findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        movies = MyApplication.nowShowingMovies;
        populateMovies();
        adapter = new MovieAdapter(requireActivity(), movies);
        rv.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(adapter);
    }
}