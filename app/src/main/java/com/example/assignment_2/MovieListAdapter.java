package com.example.assignment_2;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {
    OnHomeMovieClickListener context;
    ArrayList<Movie> movies;
    public MovieListAdapter(Activity context, ArrayList<Movie> movies){
        this.context = (OnHomeMovieClickListener) context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
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
            context.onHomeBookSeatsClick(m);
        });
        holder.btnTrailer.setOnClickListener((v)->{
            context.onHomeTrailerClick(m);
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

        }
    }

    public interface OnHomeMovieClickListener {
        void onHomeBookSeatsClick(Movie m);
        void onHomeTrailerClick(Movie m);
    }
}
