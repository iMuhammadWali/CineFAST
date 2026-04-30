package com.example.assignment_2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MyBookingsFragment extends Fragment implements MyBookingListAdapter.OnDeleteClickListener {

    private ArrayList<MyBooking> myBookings;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    MyBookingListAdapter adapter;
    ProgressBar progressBar;
    public MyBookingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_bookings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        loadBookings();
    }
    private void init(View view){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        myBookings = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new MyBookingListAdapter(requireContext(), myBookings, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        progressBar = view.findViewById(R.id.progressBar);
    }
    private void loadBookings(){
        if (mAuth.getCurrentUser() == null)
            Toast.makeText(requireContext(), "User is not logged in?", Toast.LENGTH_SHORT).show();;
        String userId = mAuth.getCurrentUser().getUid();
        mReference = mDatabase.getReference("bookings").child(userId);

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myBookings.clear();
                for (DataSnapshot bookingSnap : snapshot.getChildren()){Long numTickets = bookingSnap.child("numTickets").getValue(Long.class);
                    Long timestamp = bookingSnap.child("timestamp").getValue(Long.class);
                    String id = bookingSnap.getKey();
                    String title = bookingSnap.child("title").getValue(String.class);
                    String posterSrc = bookingSnap.child("posterSrc").getValue(String.class);

                    MyBooking booking = new MyBooking(
                            id,
                            title,
                            posterSrc,
                            String.valueOf(numTickets),
                            String.valueOf(android.text.format.DateFormat.format(
                                    "dd/MM/yyyy hh:mm a",
                                    new java.util.Date(timestamp)
                            ))
                    );
                    myBookings.add(booking);
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void ondeleteClickListener(MyBooking booking) {
        String userId = mAuth.getCurrentUser().getUid();

        String bookingId = booking.getId();

        mDatabase.getReference("bookings")
                .child(userId)
                .child(bookingId)
                .removeValue()
                .addOnSuccessListener(unused -> {
                    // Nothing to add here nor in the onFailure listerner as well
                });
    }
}