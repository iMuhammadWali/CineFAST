package com.example.assignment_2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class NowShowingFragment extends Fragment {
    RecyclerView rv;
    ArrayList<Movie> movies;
    MovieListAdapter adapter;
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
        try {
            java.io.InputStream is = requireActivity().getAssets().open("movies.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String moviesJson = new String(buffer, StandardCharsets.UTF_8);

            JSONObject moviesJSON = new JSONObject(moviesJson);
            JSONArray nowShowingMovies = moviesJSON.getJSONArray("nowShowingMovies");


            for (int i = 0; i < nowShowingMovies.length(); i++){
                JSONObject obj = nowShowingMovies.getJSONObject(i);
                String title = obj.getString("title");
                String genre = obj.getString("genre");
                String trailer = obj.getString("trailerLink");
                String bannerSrc = obj.getString("bannerSrc");
                String posterName = obj.getString("posterSrc");


                Movie movie = new Movie(
                        posterName,
                        title,
                        genre,
                        trailer,
                        false,
                        bannerSrc
                );

                movies.add(movie);
            }

        } catch (Exception e) {
            Toast.makeText(requireActivity(),
                    e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }
    private void init(View v){
        rv = v.findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        movies = new ArrayList<>();
        populateMovies();
        adapter = new MovieListAdapter(
                (NavigationManager) requireActivity(),
                movies
        );
        rv.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(adapter);
    }
}