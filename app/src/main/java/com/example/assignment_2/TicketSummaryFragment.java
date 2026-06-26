package com.example.assignment_2;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class TicketSummaryFragment extends Fragment {
    private static final String ARG_PARAM1 = "movie";
    private static final String ARG_PARAM2 = "selectedSeats";
    private static final String ARG_PARAM3 = "selectedSnacks";
    private ImageView ivMoviePoster;
    private TextView tvMovieTitle, tvTotalPrice, tvTicketsList, tvSnacksHeading, tvSnacksList;
    private AppCompatButton btnConfirm;
    private Movie movie;
    private ArrayList<String> selectedSeats;
    private ArrayList<SelectedSnack> selectedSnacks;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

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
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();

        tvMovieTitle = view.findViewById(R.id.tvMovieTitle);
        ivMoviePoster = view.findViewById(R.id.ivMoviePoster);
        tvTicketsList = view.findViewById(R.id.tvTicketsList);
        tvSnacksHeading = view.findViewById(R.id.tvSnacksHeading);
        tvSnacksList = view.findViewById(R.id.tvSnacksList);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        btnConfirm = view.findViewById(R.id.btnConfirm);
    }
    private void setupUi(){
        float totalPrice = 0f;
        tvMovieTitle.setText(movie.getTitle());

        int posterId = requireActivity().getResources()
                .getIdentifier(movie.getPosterSrc(), "drawable", requireActivity().getPackageName());

        ivMoviePoster.setImageResource(posterId);
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

        btnConfirm.setOnClickListener(v -> {
            assert mAuth.getCurrentUser() != null;
            String userId = mAuth.getCurrentUser().getUid();
            HashMap<String, Object> data = new HashMap<>();

            data.put("posterSrc", movie.getPosterSrc());
            data.put("title", movie.getTitle());
            data.put("numTickets", selectedSeats.size());
            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.HOUR_OF_DAY, 21);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            long bookingTime = calendar.getTimeInMillis();

            data.put("timestamp", bookingTime);

            mReference.child("bookings")
                    .child(userId)
                    .push()
                    .setValue(data)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(requireContext(), "Booking confirmed.", Toast.LENGTH_SHORT).show();
                        }
                    });


            // Need to parse from selectedSeats.
            // Also, for now, the app is also not storing that this user has already booked specific seat.s

            // This is the structure of a seat from selectedSeats "Row " + (i + 1) + " Seat " + (j + 1);
            ArrayList<HashMap<String, Object>> seats = new ArrayList<>();
            for (String seat: selectedSeats){
                String[] parts = seat.split(" ");
                int row = Integer.parseInt(parts[1]);
                int col = Integer.parseInt(parts[3]);
                HashMap<String, Object> seatData = new HashMap<>();
                seatData.put("row", row);
                seatData.put("col", col);

                seats.add(seatData);
            }
            mReference.child("Seats")
                    .child(movie.getTitle())
                    .child(userId)
                    .setValue(seats)
                    .addOnCompleteListener((t)->{
                        // Do nothing bro.
                    });


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
        });
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setupUi();
    }
}