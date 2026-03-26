package com.example.assignment_2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    Activity context;
    ArrayList<Movie> movies;
    public MovieAdapter(Activity context, ArrayList<Movie> movies){
        this.context = (Activity) context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                    .inflate(R.layout.single_movie_item_design, parent, false);
        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie m = movies.get(position);
        holder.tvMovieTitle.setText(m.getTitle());
        holder.tVMovieGenre.setText(m.getGenre());
        holder.ivMoviePoster.setImageResource(m.getPosterSrc());

        holder.btnBookSeats.setOnClickListener(v -> {
            Toast.makeText(context, "Book Seats Button is Clicked", Toast.LENGTH_SHORT).show();
            AppCompatActivity activity = (AppCompatActivity) context;
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fContainer, new ChooseSeatsFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView tvMovieTitle, tVMovieGenre;
        AppCompatButton btnBookSeats, btnTrailer;
        ImageView ivMoviePoster;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMoviePoster = itemView.findViewById(R.id.ivMoviePoster);
            tvMovieTitle = itemView.findViewById(R.id.tvMovieTitle);
            tVMovieGenre = itemView.findViewById(R.id.tvMovieGenre);
            btnBookSeats = itemView.findViewById(R.id.btnBookSeats);
            btnTrailer = itemView.findViewById(R.id.btnTrailer);

            btnBookSeats.setOnClickListener((v)->{
                context.startActivity(new Intent(context, SplashActivity.class));
                context.finish();
            });
        }
    }
}
