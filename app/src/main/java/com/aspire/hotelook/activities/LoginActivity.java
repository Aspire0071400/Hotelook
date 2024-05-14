package com.aspire.hotelook.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aspire.hotelook.R;
import com.aspire.hotelook.clientActivity.ClientHomePage;
import com.aspire.hotelook.databinding.ActivityLoginBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        FirebaseApp.initializeApp(this);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        binding.loginBtn.setOnClickListener(v -> {
            String email = binding.loginEmail.getText().toString();
            String password = binding.loginPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter email and password",
                        Toast.LENGTH_SHORT).show();
            } else {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this,
                        task -> {
                            if (task.isSuccessful()) {
                                firebaseDatabase.getReference("Users").child(auth.getCurrentUser().getUid().toString()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            userType = snapshot.child("vendor").getValue(String.class);

                                            if (userType == "true") {
                                                Intent intent = new Intent(LoginActivity.this, HomePage.class);
                                                intent.putExtra("IsVendor", "true");
                                                startActivity(intent);
                                                finish();
                                            } else if (userType == "false") {
                                                Intent intent = new Intent(LoginActivity.this, ClientHomePage.class);
                                                intent.putExtra("IsVendor", "false");
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                            } else {
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }

        });

        binding.newUserSignup.setOnClickListener(v -> {

            startActivity(new Intent(this, SignUpActivity.class));
            finish();

        });

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            firebaseDatabase.getReference("Users").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        userType = (String) snapshot.child("vendor").getValue();

                        if (Objects.equals(userType, "true")) {
                            Intent intent = new Intent(LoginActivity.this, HomePage.class);
                            intent.putExtra("IsVendor", "true");
                            startActivity(intent);
                            finish();
                        } else if (Objects.equals(userType, "false")) {
                            Intent intent = new Intent(LoginActivity.this, ClientHomePage.class);
                            intent.putExtra("IsVendor", "false");
                            startActivity(intent);
                            finish();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
}