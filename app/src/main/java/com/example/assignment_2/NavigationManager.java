package com.example.assignment_2;

import java.util.ArrayList;

public interface NavigationManager {
    public void makeToolBarVisible();
    public void openPreviousFragment();
    public void openChooseSeats(Movie m);
    public void openTrailer(String trailer);
    public void openTicketSummary(Movie m, ArrayList<String> seats);
    public void openSnacks(Movie m, ArrayList<String> seats);
}
