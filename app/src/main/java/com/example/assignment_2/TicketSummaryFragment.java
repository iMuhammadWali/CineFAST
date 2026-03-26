package com.example.assignment_2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


// If enough time, TODO: Use a ListView here for showing selected seats;
public class TicketSummaryFragment extends Fragment {
    private static final String ARG_PARAM1 = "movie";
    private static final String ARG_PARAM2 = "selectedSeats";
    private static final String ARG_PARAM3 = "selectedSnacks";
    TextView tvMovieTitle;
    ImageView ivMoviePoster;
    private Movie movie;
    private ArrayList<String> selectedSeats;
    private ArrayList<ArrayList<String>> selectedSnacks;

    public TicketSummaryFragment() {
        // Required empty public constructor
    }
    public static TicketSummaryFragment newInstance(Movie movie, ArrayList<String> seats, ArrayList<ArrayList<String>> snacks) {
        TicketSummaryFragment fragment = new TicketSummaryFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, movie);
        args.putStringArrayList(ARG_PARAM2, seats);
        args.putSerializable(ARG_PARAM2, snacks);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null){
            movie = (Movie) args.getSerializable(ARG_PARAM1);
            selectedSeats = args.getStringArrayList(ARG_PARAM2);
            selectedSnacks =
                    (ArrayList<ArrayList<String>>) getArguments().getSerializable(ARG_PARAM3);        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ticket_summary, container, false);
    }
    private void init(View view){
        tvMovieTitle = view.findViewById(R.id.tvMovieTitle);
        ivMoviePoster = view.findViewById(R.id.ivMoviePoster);
    }
    private void setupUi(){
        tvMovieTitle.setText(movie.getTitle());
        ivMoviePoster.setImageResource(movie.getPosterSrc());
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setupUi();

    }
}