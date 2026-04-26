package com.example.assignment_2;

import android.content.Context;
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
    NavigationManager navigationManager;
    ArrayList<Movie> movies;

    public MovieListAdapter(NavigationManager navigationManager, ArrayList<Movie> movies){
        this.navigationManager = navigationManager;
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        This parent is the Recycler View and its context is the main activity.
        View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.single_movie_item_design, parent, false);
        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie m = movies.get(position);
        holder.tvMovieTitle.setText(m.getTitle());
        holder.tVMovieGenre.setText(m.getGenre());

//        itemView's context is also MainActivity as we are the ones that set it using from()
        Context context = holder.itemView.getContext();
        int posterId = context.getResources()
                .getIdentifier(m.getPosterSrc(), "drawable", context.getPackageName());

        holder.ivMoviePoster.setImageResource(posterId);

        holder.btnBookSeats.setOnClickListener(v -> {
            navigationManager.openChooseSeats(m);
        });

        holder.btnTrailer.setOnClickListener(v -> {
            navigationManager.openTrailer(m.getTrailerLink());
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
}
