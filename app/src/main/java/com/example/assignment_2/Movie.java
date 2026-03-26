package com.example.assignment_2;

import android.graphics.drawable.Drawable;

public class Movie {
    int posterSrc;
    String title;
    String genre;
    String trailerLink;

    public int getPosterSrc(){
        return posterSrc;
    }
    public String getTitle() {
        return title;
    }

    public void setName(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTrailerLink() {
        return trailerLink;
    }

    public void setTrailerLink(String trailerLink) {
        this.trailerLink = trailerLink;
    }

    public Movie(int posterSrc, String title, String genre, String trailerLink) {
        this.posterSrc = posterSrc;
        this.title = title;
        this.genre = genre;
        this.trailerLink = trailerLink;
    }
}
