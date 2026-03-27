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
import android.widget.Toast;

import java.util.ArrayList;

// If enough time, TODO: Use a ListView here for showing selected seats;
public class TicketSummaryFragment extends Fragment {
    private static final String ARG_PARAM1 = "movie";
    private static final String ARG_PARAM2 = "selectedSeats";
    private static final String ARG_PARAM3 = "selectedSnacks";
    TextView tvMovieTitle;
    ImageView ivMoviePoster;
    TextView tvTicketsList;
    private Movie movie;
    private ArrayList<String> selectedSeats;
    private ArrayList<SelectedSnack> selectedSnacks;

    public TicketSummaryFragment() {
        // Required empty public constructor
    }
    public static TicketSummaryFragment newInstance(Movie movie, ArrayList<String> seats, ArrayList<SelectedSnack> snacks) {
        TicketSummaryFragment fragment = new TicketSummaryFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, movie);
        args.putStringArrayList(ARG_PARAM2, seats);
        args.putSerializable(ARG_PARAM3, snacks);
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
                    (ArrayList<SelectedSnack>) args.getSerializable(ARG_PARAM3);
        }
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
        tvTicketsList = view.findViewById(R.id.tvTicketsList);
    }
    private void setupUi(){
        tvMovieTitle.setText(movie.getTitle());
        ivMoviePoster.setImageResource(movie.getPosterSrc());
        if (selectedSeats != null){
            StringBuilder htmlText = new StringBuilder(); // Accumulate HTML here
            int pricePerSeat = 16;
            for (String seat : selectedSeats) {
                htmlText.append(seat).append(" &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ").append("<b>").append(pricePerSeat).append(" USD </b><br/>");        }

            tvTicketsList.setText(android.text.Html.fromHtml(htmlText.toString(), android.text.Html.FROM_HTML_MODE_LEGACY));
        };

        //TODO: Add Snacks here by using that 2D ArrayList
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setupUi();
    }
}