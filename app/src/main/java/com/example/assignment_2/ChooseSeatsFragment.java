package com.example.assignment_2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChooseSeatsFragment extends Fragment {
    TextView tvMovieTitle;
    AppCompatButton btnBookSeats, btnProceedToSnacks;
    GridLayout glSeating;
    ArrayList<String> selectedSeats;
    ArrayList<String> alreadySelectedSeats;
    Movie movie;
    NavigationManager navigationManager;
    LinearLayout llMovieInformation;
    ImageView ivMoviePoster;
    private static final String ARG_PARAM1 = "movie";

    public static ChooseSeatsFragment newInstance(Movie movie) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, movie);
        ChooseSeatsFragment fragment = new ChooseSeatsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        navigationManager = (NavigationManager) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null){
            movie = (Movie) args.getSerializable(ARG_PARAM1);
        }
        selectedSeats = new ArrayList<>();
        alreadySelectedSeats = new ArrayList<>();
    }

    public ChooseSeatsFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_seats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("Seats").child(movie.getTitle());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot data: snapshot.getChildren()){
                        for (DataSnapshot d : data.getChildren()){
                            Integer row = d.child("row").getValue(Integer.class);
                            Integer col = d.child("col").getValue(Integer.class);
                            String seat = "Row " + row + " Seat " + col;
                            alreadySelectedSeats.add(seat);
                        }
                    }
                }
                createSeatsGrid();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // I don't know when is this function called.
            }
        });
        setupUi(view);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void init(View v){
        llMovieInformation = v.findViewById(R.id.llMovieInformation);
        ivMoviePoster = v.findViewById(R.id.ivMoviePoster);
        tvMovieTitle = v.findViewById(R.id.tvMovieTitle);
        glSeating = v.findViewById(R.id.glSeating);
        btnBookSeats = v.findViewById(R.id.btnBookSeats);
        btnProceedToSnacks = v.findViewById(R.id.btnProceedToSnacks);

        // Set up the instruction buttons.
        v.findViewById(R.id.vBooked).setEnabled(false);
        v.findViewById(R.id.vYours).setSelected(true);
    }
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
    private void setupUi(View view){
        String bannerSrc = movie.getBannerSrc();
        if (bannerSrc.isEmpty()){
            int posterId = requireActivity()
                    .getResources()
                    .getIdentifier(movie.getPosterSrc(), "drawable", requireActivity().getPackageName());

            ivMoviePoster.setImageResource(posterId);

        }
        else {
            int bannerId = requireActivity()
                    .getResources()
                    .getIdentifier(bannerSrc, "drawable", requireActivity().getPackageName());
            ivMoviePoster.setImageResource(bannerId);
        }
        if (selectedSeats.isEmpty()){
            btnBookSeats.setEnabled(false);
            btnProceedToSnacks.setEnabled(false);
        }

        tvMovieTitle.setText(movie.getTitle());

        if (movie.getIsComingSoon()){
            btnBookSeats.setEnabled(false);

            btnBookSeats.setText("Coming Soon");

            btnProceedToSnacks.setEnabled(true);
            btnProceedToSnacks.setText("Watch Trailer");
            btnProceedToSnacks.setBackgroundResource(R.drawable.app_white_button);
            btnProceedToSnacks.setTextColor(getResources().getColor(R.color.black));
            btnProceedToSnacks.setOnClickListener((v)->{
                String link = movie.trailerLink;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
            });
        }
        else {
            btnBookSeats.setOnClickListener((v) -> {
                navigationManager.openTicketSummary(movie, selectedSeats, null);
            });
            btnProceedToSnacks.setOnClickListener((v) -> {
                navigationManager.openSnacks(movie, selectedSeats);
            });
        }
    }
    private void createSeatsGrid(){
        glSeating.removeAllViews();
        int rows = 8;
        int cols = 9;

        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                // Create a seat
                View v = new View(requireActivity());

                // Set the layout
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = dpToPx(22);
                params.height= dpToPx(22);
                params.setMargins(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4));
                v.setLayoutParams(params);

                // Add the seat in the grid.
                glSeating.addView(v);

                // do not show the seat if this place is not a valid seating place
                if (j == 4 || (i == 0 && j == 0) || (i == 0 && j == cols - 1)
                        || (i == rows - 1 && j == 0) || (i == rows - 1 && j == cols - 1)) {
                    continue;
                }

                v.setBackgroundResource(R.drawable.app_choose_seats_seat);
                String seatName = "Row " + (i + 1) + " Seat " + (j + 1);
                v.setTag(seatName);
                if (alreadySelectedSeats.contains(seatName) || movie.getIsComingSoon()){
                    v.setEnabled(false);
                    continue;
                }
                v.setSelected(selectedSeats.contains(seatName));
                v.setOnClickListener(clickedView -> {
                    String seat = (String) clickedView.getTag();
                    if (clickedView.isSelected()) {
                        clickedView.setSelected(false);
                        selectedSeats.remove(seat);
                    } else {
                        clickedView.setSelected(true);
                        selectedSeats.add(seat);
                    }
                    if (selectedSeats.isEmpty()) {
                        btnBookSeats.setEnabled(false);
                        btnProceedToSnacks.setEnabled(false);
                    } else {
                        btnBookSeats.setEnabled(true);
                        btnProceedToSnacks.setEnabled(true);
                    }
                });
            }
        }
    }
}