package com.example.assignment_2;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.List;

public class SnacksListAdapter extends ArrayAdapter<Snack> {
    Context context;
    public SnacksListAdapter(@NonNull Context context, @NonNull List<Snack> objects) {
        super(context, R.layout.single_snack_item_design, objects);
    }
}
