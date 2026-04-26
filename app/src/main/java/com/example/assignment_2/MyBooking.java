package com.example.assignment_2;

public class MyBooking {
    String title;
    String posterSrc;
    String numTickets;
    String timestamp;

    public MyBooking(String title, String posterSrc, String numTickets, String timestamp) {
        this.title = title;
        this.posterSrc = posterSrc;
        this.numTickets = numTickets;
        this.timestamp= timestamp;
    }

    public String getPosterSrc() {
        return posterSrc;
    }

    public void setPosterSrc(String posterSrc) {
        this.posterSrc = posterSrc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNumTickets() {
        return numTickets;
    }

    public void setNumTickets(String numTickets) {
        this.numTickets = numTickets;
    }

    public String getTimestamp() {
        return timestamp;
    }

}
