package com.example.assignment_2;

public class MyBooking {
    String id;
    String title;
    String posterSrc;
    int numTickets;
    long timestampMillis;
    String formattedTimestamp;

    public MyBooking(String id, String title, String posterSrc,
                     int numTickets, long timestampMillis, String formattedTimestamp) {
        this.id = id;
        this.title = title;
        this.posterSrc = posterSrc;
        this.numTickets = numTickets;
        this.timestampMillis = timestampMillis;
        this.formattedTimestamp = formattedTimestamp;
    }
    public String getId() {
        return id;
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

    public int getNumTickets() {
        return numTickets;
    }

    public void setNumTickets(int numTickets) {
        this.numTickets = numTickets;
    }

    public long getTimestampMillis() { return timestampMillis; }
    public String getFormattedTimestamp() { return formattedTimestamp; }
}
