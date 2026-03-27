package com.example.assignment_2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

// If enough time, TODO: Use a ListView here for showing selected seats;
// PS: I used a scrollView

// TODO: Calculate TotalPrice and update it on the screen. [Done]
public class TicketSummaryFragment extends Fragment {
    private static final String ARG_PARAM1 = "movie";
    private static final String ARG_PARAM2 = "selectedSeats";
    private static final String ARG_PARAM3 = "selectedSnacks";
    ImageView ivMoviePoster;
    TextView tvMovieTitle, tvTotalPrice, tvTicketsList, tvSnacksHeading, tvSnacksList;
    AppCompatButton btnBack, btnConfirm;
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
        tvSnacksHeading = view.findViewById(R.id.tvSnacksHeading);
        tvSnacksList = view.findViewById(R.id.tvSnacksList);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        btnBack = view.findViewById(R.id.btnBack);
        btnConfirm = view.findViewById(R.id.btnConfirm);
    }
    private void setupUi(){
        float totalPrice = 0f;
        tvMovieTitle.setText(movie.getTitle());
        ivMoviePoster.setImageResource(movie.getPosterSrc());
        if (selectedSeats != null){
            StringBuilder htmlText = new StringBuilder(); // Accumulate HTML here
            int pricePerSeat = 16;
            for (String seat : selectedSeats) {
                totalPrice += movie.getTicketPrice();
                htmlText.append(seat).append(" &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ").append("<b>").append(pricePerSeat).append(" USD </b><br/>");
            }

            tvTicketsList.setText(android.text.Html.fromHtml(htmlText.toString(), android.text.Html.FROM_HTML_MODE_LEGACY));
        }

        if (selectedSnacks != null && !selectedSnacks.isEmpty()) {
            tvSnacksHeading.setVisibility(View.VISIBLE);
            tvSnacksList.setVisibility(View.VISIBLE);
            StringBuilder htmlText = new StringBuilder();
            for (SelectedSnack snack : selectedSnacks) {
                totalPrice += snack.getTotalPrice();
                htmlText.append("X").append(snack.getQuantity()).append(" ").append(snack.getName()).append(" &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ").append(snack.getTotalPrice()).append(" USD").append("</b><br/>");
            }
            tvSnacksList.setText(android.text.Html.fromHtml(htmlText.toString(), android.text.Html.FROM_HTML_MODE_LEGACY));
        }
        tvTotalPrice.setText(totalPrice + " USD");



        btnBack.setOnClickListener((v)->{
            requireActivity()
                    .getSupportFragmentManager()
                    .popBackStack();
        });

        float finalTotalPrice = totalPrice;
        btnConfirm.setOnClickListener(v -> {
            // Store the ticket using SharedPreferences
            SharedPreferences prefs = requireActivity().getSharedPreferences("bookingPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("movieTitle", movie.getTitle());
            editor.putInt("seatCount", selectedSeats.size());
            editor.putFloat("totalPrice", finalTotalPrice);
            editor.apply();

            String seatsText = (selectedSeats != null && !selectedSeats.isEmpty())
                    ? String.join(", ", selectedSeats)
                    : "None";

            String snacksText = "None";
            if (selectedSnacks != null && !selectedSnacks.isEmpty()) {
                ArrayList<String> snackLines = new ArrayList<>();
                for (SelectedSnack snack : selectedSnacks) {
                    snackLines.add(snack.getName() + " x" + snack.getQuantity());
                }
                snacksText = String.join(", ", snackLines);
            }

            String subject = "Movie Ticket Confirmation: " + tvMovieTitle.getText().toString();
            String body = "Thank you for your order!\n\n" +
                    "Movie: " + tvMovieTitle.getText().toString() + "\n" +
                    "Date: 13/4/2025\n" +
                    "Time: 22:15\n" +
                    "Seats: " + seatsText + "\n" +
                    "Snacks: " + snacksText + "\n" +
                    "Total Price: " + tvTotalPrice.getText().toString() + "\n\n" +
                    "Enjoy your movie!";

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(android.net.Uri.parse("mailto:"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, body);

            try {
                startActivity(Intent.createChooser(emailIntent, "Send Confirmation Email..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(requireContext(), "No email apps installed.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setupUi();
    }
}