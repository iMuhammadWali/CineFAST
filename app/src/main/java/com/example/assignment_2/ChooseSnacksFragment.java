package com.example.assignment_2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class ChooseSnacksFragment extends Fragment {
    ListView lv;
    private ArrayList<Snack> snacks;
    private SnackListAdapter adapter;
    private ArrayList<ArrayList<String>> selectedSnacks;
    public ChooseSnacksFragment() {
        // Required empty public constructor
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

        lv = view.findViewById(R.id.lv);
        snacks = new ArrayList<>();
        snacks.add(new Snack(R.drawable.snacks_popcorn, "Popcorn", "Large / Buttered", 8.99f));
        adapter = new SnackListAdapter(requireActivity(), snacks);
        lv.setAdapter(adapter);
    }
}