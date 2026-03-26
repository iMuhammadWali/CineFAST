package com.example.assignment_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SnackListAdapter extends ArrayAdapter<Snack> {
    Context context;
    public SnackListAdapter(@NonNull Context context, @NonNull List<Snack> objects) {
        super(context, 0, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // This function is called getCount() times
        View snackView = convertView;
        if (snackView == null) {
            snackView = LayoutInflater.from(context).inflate(R.layout.single_snack_item_design, parent, false);
        }

        Snack snack = getItem(position);
        if (snack == null) return snackView;

        TextView tvSnackName = snackView.findViewById(R.id.tvSnackName);
        TextView tvSnackDetails = snackView.findViewById(R.id.tvSnackDetails);
        ImageView ivSnackImage = snackView.findViewById(R.id.ivSnackImage);

        tvSnackName.setText(snack.getName());
        tvSnackDetails.setText(snack.getDetails());
        ivSnackImage.setImageResource(snack.getImageSrc());
        return snackView;
    }
}
