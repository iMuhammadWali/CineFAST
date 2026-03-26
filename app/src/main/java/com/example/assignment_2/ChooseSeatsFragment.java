package com.example.assignment_2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
// TODO: Understand the lifecycle of Fragment
public class ChooseSeatsFragment extends Fragment {
    public ChooseSeatsFragment() {
//        Toast.makeText(requireContext(), "Choose Seat", Toast.LENGTH_SHORT).show();
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_seats, container, false);
    }
}