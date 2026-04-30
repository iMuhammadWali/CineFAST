package com.example.assignment_2;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChooseSnacksFragment extends Fragment implements SnackListAdapter.SnackOnClickListener {
    ListView lv;
    private ArrayList<Snack> snacks;
    private Movie movie;
    private ArrayList<String> selectedSeats;
    private Map<String, Integer> snackQtyMap;
    private AppCompatButton btnConfirm;
    NavigationManager navigationManager;

    private static final String ARG_PARAM1 = "movie";
    private static final String ARG_PARAM2 = "selectedSeats";
    public static ChooseSnacksFragment newInstance(Movie movie, ArrayList<String> selectedSeats){
        ChooseSnacksFragment fragment = new ChooseSnacksFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1,  movie);
        args.putStringArrayList(ARG_PARAM2, selectedSeats);
        fragment.setArguments(args);
        return fragment;
    }

    public ChooseSnacksFragment() {
        // Required empty public constructor
    }

    private void populateSnacks() {
        DatabaseManager dbManager = new DatabaseManager(requireContext());
        snacks.clear();
        snacks.addAll(dbManager.getAllSnacks());
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
            selectedSeats = args.getStringArrayList(ARG_PARAM2);
        }
        else {
            // This line will never run though, because this fragment is displayed without arguments.
            movie = null;
            selectedSeats = new ArrayList<>();
        }
        snacks = new ArrayList<>();
        snackQtyMap = new HashMap<>();
        populateSnacks();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_snacks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setupUi();;
    }

    private void init(View view){
        lv = view.findViewById(R.id.lv);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        SnackListAdapter adapter = new SnackListAdapter(requireActivity(), this, snacks, snackQtyMap);
        lv.setAdapter(adapter);
    }

    private void setupUi(){
        btnConfirm.setOnClickListener((v)->{
            // Build selectedSnacks from the map.
            ArrayList<SelectedSnack> selectedSnacks = new ArrayList<>();
            for (Snack snack : snacks){
                Integer quantity = snackQtyMap.get(snack.getName());
                if (quantity != null && quantity > 0){
                    SelectedSnack selectedSnack = new SelectedSnack(snack.getName(), snack.getPrice(), quantity);
                    selectedSnacks.add(selectedSnack);
                }
            }

            navigationManager.openTicketSummary(movie, selectedSeats, selectedSnacks);
        });
    }

    @Override
    public void onIncreaseClickListener(Snack snack, Integer quantity) {
        snackQtyMap.put(snack.getName(), quantity);
    }

    @Override
    public void onDecreaseClickListener(Snack snack, Integer quantity) {
        if (quantity == 0){
            snackQtyMap.remove(snack.getName());
        }
        else{
            snackQtyMap.put(snack.getName(), quantity);
        }
    }
}