package com.example.assignment_2;

import android.graphics.drawable.Drawable;

public class Movie {
    int posterSrc;
    String title;
    String genre;
    String trailerLink;
    String date;
    boolean isComingSoon;
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

    public boolean getIsComingSoon(){
        return isComingSoon;
    }
    public Movie(int posterSrc, String title, String genre, String trailerLink, boolean isComingSoon) {
        this.posterSrc = posterSrc;
        this.title = title;
        this.genre = genre;
        this.trailerLink = trailerLink;
        this.isComingSoon = isComingSoon;
    }
}
