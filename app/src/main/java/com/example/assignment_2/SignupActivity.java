package com.example.assignment_2;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    TextView tvLogin, tvNameError, tvEmailError, tvPasswordError, tvConfirmPasswordError;
    AppCompatButton btnSignup;

    EditText etName, etEmail, etPassword, etConfirmPassword;

    ProgressBar pbSignup;
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
        mDatabase = FirebaseDatabase.getInstance();

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
        pbSignup = findViewById(R.id.pbSignup);
    }

    private void setListeners() {
        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        btnSignup.setOnClickListener(v -> {
            boolean hasError = false;
            clearErrors();
            pbSignup.setVisibility(View.VISIBLE);
            btnSignup.setVisibility(View.GONE);
            String name = etName.getText().toString().trim();
            String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9+_.-]+\\.[A-Za-z]{2,}$";
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            if (!email.matches(emailPattern)){
                tvEmailError.setText("Email address must be of the form: name@example.com");
                tvEmailError.setVisibility(View.VISIBLE);
                hasError = true;
            }
            String confirmPassword = etConfirmPassword.getText().toString().trim();

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
                            HashMap<String, Object> data = new HashMap<>();
                            data.put("username", etName.getText().toString().trim());
                            DatabaseReference ref = mDatabase.getReference();
                            ref.child("users").child(mAuth.getUid()).setValue(data);

                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        } else {
                            showErrorDialog(String.valueOf(task.getException()));
                            pbSignup.setVisibility(View.GONE);
                            btnSignup.setVisibility(View.VISIBLE);
                        }
                        pbSignup.setVisibility(View.GONE);
                        btnSignup.setVisibility(View.VISIBLE);
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