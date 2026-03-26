package com.example.assignment_2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ComingSoonFragment extends Fragment {
    RecyclerView rv;
    ArrayList<Movie> movies;
    MovieAdapter adapter;
    public ComingSoonFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_coming_soon, container, false);
        init(v);
        return v;
    }
    private void populateMovies(){
        movies.add(new Movie(R.drawable.movie_detective_conan_coming_soon,"Detective Conan: Fallen Angel of the Highway", "Action / Mystery", "https://youtu.be/Lrc1GAMTFnc?si=vbFl-fYnJLcH2ZP5", true));
        movies.add(new Movie(R.drawable.movie_slime_coming_soon,"That Time I Got Reincarnated as a Slime the Movie: Tears of the Azure Sea", "Action / Comedy", "https://youtu.be/RBJL4B0abpI?si=LeAzTpuuJ5oJv45y", true));
        movies.add(new Movie(R.drawable.movie_sound_euphonium_coming_soon,"Sound! Euphonium The Final Movie Part 1", "Action", "https://youtu.be/cC3aGvKZ7U4?si=g3_gZvcjpP1HdnxH", true));
    }
    private void init(View v){
        rv = v.findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        movies = new ArrayList<>();
        populateMovies();
        adapter = new MovieAdapter(requireActivity(), movies);
        rv.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(adapter);
    }
}