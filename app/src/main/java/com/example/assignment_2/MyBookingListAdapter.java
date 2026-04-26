package com.example.assignment_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyBookingListAdapter extends RecyclerView.Adapter<MyBookingListAdapter.MyBookingViewHolder> {
    private Context context;
    private ArrayList<MyBooking> myBookings;
    public MyBookingListAdapter(Context context, ArrayList<MyBooking> myBookings){
        this.context = context;
        this.myBookings = myBookings;
    }

    @NonNull
    @Override
    public MyBookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyBookingViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.single_my_booking_item_design, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyBookingViewHolder holder, int position) {
        MyBooking booking = myBookings.get(position);
        int posterId = context
                .getResources()
                .getIdentifier(booking.getPosterSrc(), "drawable", context.getPackageName());

        holder.ivPoster.setImageResource(posterId);
        holder.tvMovieTitle.setText(booking.getTitle());
        holder.tvNumTickets.setText(booking.getNumTickets() + "Tickets");
        holder.tvTimestamp.setText(booking.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return myBookings.size();
    }

    public class MyBookingViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPoster, ivDelete;
        TextView tvMovieTitle, tvTimestamp, tvNumTickets;

        public MyBookingViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivMoviePoster);
            ivDelete = itemView.findViewById(R.id.ivDeleteTicket);

            tvMovieTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvNumTickets = itemView.findViewById(R.id.tvNumTickets);
        }
    }
}
