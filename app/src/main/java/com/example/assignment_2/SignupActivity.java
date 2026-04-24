package com.example.assignment_2;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    TextView tvLogin, tvNameError, tvEmailError, tvPasswordError, tvConfirmPasswordError;
    AppCompatButton btnSignup;

    EditText etName, etEmail, etPassword, etConfirmPassword;

//    TODO: for now, it shows the error sent back by firebase, I need to see how to make it consistent.
//    TODO: Make a custom view for the dialog.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            );
            return insets;
        });

        init();
        setListeners();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();

        tvLogin = findViewById(R.id.tvLogin);

        tvNameError = findViewById(R.id.tvNameError);
        tvEmailError = findViewById(R.id.tvEmailError);
        tvPasswordError = findViewById(R.id.tvPasswordError);
        tvConfirmPasswordError = findViewById(R.id.tvConfirmPasswordError);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnSignup = findViewById(R.id.btnSignup);
    }

    private void setListeners() {

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        btnSignup.setOnClickListener(v -> {

            clearErrors();

            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            boolean hasError = false;

            if (name.isEmpty()) {
                tvNameError.setText("Name field must not be empty");
                tvNameError.setVisibility(View.VISIBLE);
                hasError = true;
            }

            if (email.isEmpty()) {
                tvEmailError.setText("Email field must not be empty");
                tvEmailError.setVisibility(View.VISIBLE);
                hasError = true;
            }

            if (password.isEmpty()) {
                tvPasswordError.setText("Password field must not be empty");
                tvPasswordError.setVisibility(View.VISIBLE);
                hasError = true;
            }

            if (confirmPassword.isEmpty()) {
                tvConfirmPasswordError.setText("Confirm password field must not be empty");
                tvConfirmPasswordError.setVisibility(View.VISIBLE);
                hasError = true;
            }

            if (hasError) return;

            if (!password.equals(confirmPassword)) {
                tvConfirmPasswordError.setText("Passwords do not match");
                tvConfirmPasswordError.setVisibility(View.VISIBLE);
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        } else {
                            showErrorDialog(String.valueOf(task.getException()));
                        }
                    });
        });
    }

    private void clearErrors() {
        tvNameError.setVisibility(View.GONE);
        tvEmailError.setVisibility(View.GONE);
        tvPasswordError.setVisibility(View.GONE);
        tvConfirmPasswordError.setVisibility(View.GONE);
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("TRY AGAIN", (d, w) -> {})
                .show();
    }
}