package com.example.assignment_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

//TODO: Add a login loading progress bar
public class LoginActivity extends AppCompatActivity {
    MaterialToolbar toolbar;
    TextView tvRegister;
    AppCompatButton btnLogin;
    FirebaseAuth mAuth;
    EditText etEmail, etPassword;
    TextView tvEmailError, tvPasswordError;
    ProgressBar pbLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        setListeners();

        // Check if user is signed in or not
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        // else user will have to log in.
    }
    private void setListeners(){
        toolbar.setNavigationOnClickListener((v)->{
            Toast.makeText(this, "Okay the back button works", Toast.LENGTH_SHORT).show();
        });

        tvRegister.setOnClickListener((v)->{
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            finish();
        });

        btnLogin.setOnClickListener((v)->{
            btnLogin.setVisibility(View.GONE);
            pbLogin.setVisibility(View.VISIBLE);
            clearErrors();
            boolean hasError = false;
            String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9+_.-]+\\.[A-Za-z]{2,}$";
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            if (!email.matches(emailPattern)){
//                showError(tvEmailError, "Email address must be of the form: name@example.com");
                tvEmailError.setText("Email address must be of the form: name@example.com");
                tvEmailError.setVisibility(View.VISIBLE);
                hasError = true;
            }

            if (email.isEmpty()) {
                tvEmailError.setText("Email field must not be empty");
                tvEmailError.setVisibility(View.VISIBLE);
//                showError(tvEmailError, "Email field must not be empty");
                hasError = true;
            }
            if (password.isEmpty()) {
                tvPasswordError.setText("Password field must not be empty");
                tvPasswordError.setVisibility(View.VISIBLE);
//                showError(tvPasswordError, "Email field must not be empty");
                hasError = true;
            }

            if (hasError) return;

            mAuth.signInWithEmailAndPassword(email, password).
                    addOnCompleteListener(this, (task)->{
                        if (task.isSuccessful()){
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                        }
                        btnLogin.setVisibility(View.VISIBLE);
                        pbLogin.setVisibility(View.GONE);
                    });
        });
    }
    private void init(){
        mAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar);
        tvRegister = findViewById(R.id.tvRegister);
        btnLogin = findViewById(R.id.btnLogin);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        tvEmailError = findViewById(R.id.tvEmailError);
        tvPasswordError = findViewById(R.id.tvPasswordError);
        pbLogin = findViewById(R.id.pbLogin);
    }
    private void clearErrors(){
        tvEmailError.setVisibility(View.GONE);
        tvPasswordError.setVisibility(View.GONE);
    }
    private void showError(TextView tv, String msg) {
        tv.setText(msg);
        tv.setVisibility(View.VISIBLE);
        tv.setAlpha(0f);
        tv.animate()
                .alpha(1f)
                .setDuration(200)
                .start();
    }
    private void hideError(TextView tv) {
        tv.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction(() -> tv.setVisibility(View.GONE))
                .start();
    }
}