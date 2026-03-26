package com.example.assignment_2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

// TODO: Understand the lifecycle of Fragment
// TODO: Make the screen work for isComingSoonMovies
public class ChooseSeatsFragment extends Fragment {
    // Hooks and Attributes
    TextView tvMovieTitle;
    AppCompatButton btnBack, btnBookSeats, btnProceedToSnacks;
    GridLayout glSeating;
    ArrayList<String> selectedSeats;
    Movie movie;
    private static final String ARG_PARAM1 = "movie";

    // Methods
    public static ChooseSeatsFragment newInstance(Movie movie) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, movie);
        ChooseSeatsFragment fragment = new ChooseSeatsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null){
            movie = (Movie) args.getSerializable(ARG_PARAM1);
        }
        selectedSeats = new ArrayList<>();
    }

    public ChooseSeatsFragment() {
        // Required empty public constructor
    }
    private void init(View v){
        tvMovieTitle = v.findViewById(R.id.tvMovieTitle);
        btnBack = v.findViewById(R.id.btnBack);
        glSeating = v.findViewById(R.id.glSeating);
        btnBookSeats = v.findViewById(R.id.btnBookSeats);
        btnProceedToSnacks = v.findViewById(R.id.btnProceedToSnacks);
        v.findViewById(R.id.vBooked).setEnabled(false);
        v.findViewById(R.id.vYours).setSelected(true);

    }
    private void setupUi(View view){
        tvMovieTitle.setText(movie.getTitle());
        btnBack.setOnClickListener((v)-> requireActivity()
                .getSupportFragmentManager()
                .popBackStack());

        btnProceedToSnacks.setOnClickListener((v)->{
            ChooseSnacksFragment fragment = ChooseSnacksFragment.newInstance(movie, selectedSeats);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fContainer, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        btnBookSeats.setOnClickListener((v)->{
            TicketSummaryFragment fragment = TicketSummaryFragment.newInstance(movie, selectedSeats, null);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fContainer, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        createSeatsGrid();
    }
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
    private void createSeatsGrid(){
        int rows = 8;
        int cols = 9;

        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                View v = new View(requireActivity());
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = dpToPx(22);
                params.height= dpToPx(22);
                params.setMargins(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4));
                v.setLayoutParams(params);
                glSeating.addView(v);

                if (j == 4 || (i == 0 && j == 0) || (i == 0 && j == cols - 1)
                        || (i == rows - 1 && j == 0) || (i == rows - 1 && j == cols - 1)) { // This is the aisle (the middle gap)
                    // do not show the seat if this place is not a valid seating place
                    continue;
                }

                if (i == 2){
                    v.setEnabled(false);
                }
                v.setBackgroundResource(R.drawable.app_choose_seats_seat);

                v.setOnClickListener(clickedView -> {
                    String seat = (String) clickedView.getTag();
                    if (clickedView.isSelected()) {
                        clickedView.setSelected(false);
                        selectedSeats.remove(seat);
                    } else {
                        clickedView.setSelected(true);
                        selectedSeats.add(seat);
                    }
                });
            }
        }
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
        setupUi(view);
    }
}