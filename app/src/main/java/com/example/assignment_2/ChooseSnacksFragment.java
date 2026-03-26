package com.example.assignment_2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;

public class ChooseSnacksFragment extends Fragment {
    ListView lv;
    private ArrayList<Snack> snacks;
    private SnackListAdapter adapter;
    private ArrayList<String> selectedSeats;
    private ArrayList<ArrayList<String>> selectedSnacks;
    private AppCompatButton btnConfirm;
    private static final String ARG_PARAM1 = "movie";
    private static final String ARG_PARAM2 = "selectedSeats";
    public static ChooseSnacksFragment newInstance(Movie movie, ArrayList<String> seats){
        ChooseSnacksFragment fragment = new ChooseSnacksFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1,  movie);
        args.putStringArrayList(ARG_PARAM2, seats);
        fragment.setArguments(args);
        return fragment;
    }
    public ChooseSnacksFragment() {
        // Required empty public constructor
    }
    private void populateSnacks(){
        snacks.add(new Snack(R.drawable.snacks_popcorn, "Popcorn", "Large / Buttered", 8.99f));
        snacks.add(new Snack(R.drawable.snacks_nachos, "Nachos", "With Cheese Dip", 7.99f));
        snacks.add(new Snack(R.drawable.snacks_soft_drink, "Soft Drink", "Large / Any Flavor", 5.99f));
        snacks.add(new Snack(R.drawable.snacks_lays, "Lays", "Family Pack", 1.99f));
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null){
            selectedSeats = args.getStringArrayList(ARG_PARAM1);
        }
        snacks = new ArrayList<>();
        populateSnacks();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_snacks, container, false);
    }
    private void init(View view){
        lv = view.findViewById(R.id.lv);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        adapter = new SnackListAdapter(requireActivity(), snacks);
        lv.setAdapter(adapter);
    }
    private void setupUi(){
        // TODO: Send selected Seats and Snacks to TicketSummaryFragment
        btnConfirm.setOnClickListener((v)->{
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fContainer, new TicketSummaryFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setupUi();;
    }
}