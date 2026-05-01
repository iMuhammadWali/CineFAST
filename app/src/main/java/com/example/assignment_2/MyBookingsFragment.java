package com.example.assignment_2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
                for (DataSnapshot bookingSnap : snapshot.getChildren()){
                    Long numTickets = bookingSnap.child("numTickets").getValue(Long.class);
                    Long timestamp = bookingSnap.child("timestamp").getValue(Long.class);

                    String id = bookingSnap.getKey();
                    String title = bookingSnap.child("title").getValue(String.class);
                    String posterSrc = bookingSnap.child("posterSrc").getValue(String.class);

                    long ts = (timestamp != null) ? timestamp : 0;

                    String formatted = String.valueOf(
                            android.text.format.DateFormat.format(
                                    "dd/MM/yyyy hh:mm a",
                                    new java.util.Date(ts)
                            )
                    );

                    MyBooking booking = new MyBooking(
                            id,
                            title,
                            posterSrc,
                            numTickets != null ? numTickets.intValue() : 0,
                            ts,
                            formatted
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
        showConfirmDialog(
                requireContext(),
                "Cancel Booking",
                "Are you sure you want to cancel this booking?",
                "Cancel",
                0,
                () -> {
                    assert mAuth.getCurrentUser() != null;
                    String userId = mAuth.getCurrentUser().getUid();

                    String bookingId = booking.getId();

                    mDatabase.getReference("bookings")
                            .child(userId)
                            .child(bookingId)
                            .removeValue()
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(requireContext(), "Booking Cancelled Successfully", Toast.LENGTH_SHORT).show();
                            });
                }
        );
    }

    public static void showConfirmDialog(
            Context context,
            String title,
            String message,
            String confirmText,
            int iconRes,
            Runnable onConfirm) {

        View view = LayoutInflater.from(context).inflate(R.layout.app_dialog_design, null);

        ImageView ivIcon = view.findViewById(R.id.ivDialogIcon);
        TextView tvTitle = view.findViewById(R.id.tvDialogTitle);
        TextView tvMessage = view.findViewById(R.id.tvDialogMessage);
        TextView tvCancel = view.findViewById(R.id.tvDialogCancel);
        TextView tvConfirm = view.findViewById(R.id.tvDialogConfirm);

        tvTitle.setText(title);
        tvMessage.setText(message);
        tvConfirm.setText(confirmText);

        if (iconRes != 0) {
            ivIcon.setVisibility(View.VISIBLE);
            ivIcon.setImageResource(iconRes);
        }

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .create();

//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tvCancel.setOnClickListener(v -> dialog.dismiss());
        tvConfirm.setOnClickListener(v -> {
            dialog.dismiss();
            onConfirm.run();
        });

        dialog.show();

    }

}