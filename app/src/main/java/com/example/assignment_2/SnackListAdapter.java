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
import androidx.appcompat.widget.AppCompatButton;

import java.util.List;
import java.util.Map;

public class SnackListAdapter extends ArrayAdapter<Snack> {

    private final Context context;
    private final SnackOnClickListener listener;
    private final Map<String, Integer> snackQtyMap;
    public SnackListAdapter(@NonNull Context context,
                            @NonNull SnackOnClickListener listener,
                            @NonNull List<Snack> objects,
                            @NonNull Map<String, Integer> snackQtyMap){
        super(context, 0, objects);
        this.context = context;
        this.listener = listener;
        this.snackQtyMap = snackQtyMap;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View snackView = convertView;
        if (snackView == null) {
            snackView = LayoutInflater.from(context).inflate(R.layout.single_snack_item_design, parent, false);
        }

        Snack snack = getItem(position);
        if (snack == null) return snackView;

        ImageView ivSnackImage = snackView.findViewById(R.id.ivSnackImage);
        TextView tvSnackName = snackView.findViewById(R.id.tvSnackName);
        TextView tvSnackDetails = snackView.findViewById(R.id.tvSnackDetails);
        TextView tvQuantity = snackView.findViewById(R.id.tvQuantity);
        AppCompatButton btnIncrease = snackView.findViewById(R.id.btnIncrease);
        AppCompatButton btnDecrease = snackView.findViewById(R.id.btnDecrease);

        ivSnackImage.setImageResource(snack.getImageSrc());
        tvSnackName.setText(snack.getName());
        tvSnackDetails.setText(snack.getDetails());
        Integer q = snackQtyMap.get(snack.getName());
        tvQuantity.setText(String.valueOf(q == null ? 0 : q));

        btnIncrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(tvQuantity.getText().toString());
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
            listener.onIncreaseClickListener(snack, quantity);
        });

        btnDecrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(tvQuantity.getText().toString());
            if (quantity == 0) return;
            quantity--;
            tvQuantity.setText(String.valueOf(quantity));
            listener.onDecreaseClickListener(snack, quantity);
        });

        return snackView;
    }

    public interface SnackOnClickListener {
        void onIncreaseClickListener(Snack s, Integer q);
        void onDecreaseClickListener(Snack s, Integer q);
    }
}
