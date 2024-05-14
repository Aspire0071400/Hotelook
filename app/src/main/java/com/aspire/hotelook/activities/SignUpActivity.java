package com.aspire.hotelook.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aspire.hotelook.R;
import com.aspire.hotelook.clientActivity.ClientHomePage;
import com.aspire.hotelook.databinding.ActivitySignUpBinding;
import com.aspire.hotelook.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private final ArrayList<String> gender_list = new ArrayList<>();
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private String uid;
    private String IsVendor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        gender_list.add("Gender");
        gender_list.add("Male");
        gender_list.add("Female");
        gender_list.add("Other");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gender_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.signupGenderSpinner.setAdapter(adapter);

        binding.oldUserToLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });

        binding.signupBtn.setOnClickListener(v -> {

            int currentGender = binding.signupGenderSpinner.getSelectedItemPosition();
            String name = binding.signupName.getText().toString();
            String email = binding.signupEmail.getText().toString().trim();
            String phoneNumber = binding.signupPhoneNumber.getText().toString().trim();
            String gender = gender_list.get(currentGender);
            String address = binding.signupAddress.getText().toString();
            String password = binding.signupPassword.getText().toString().trim();
            String confirmPassword = binding.signupPasswordConfirm.getText().toString().trim();


            if (name.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || gender.equals("Gender") || address.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email format. Please enter a valid email address.", Toast.LENGTH_LONG).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 8) {
                Toast.makeText(this, "Password must be 8 or more characters long", Toast.LENGTH_SHORT).show();
            } else if (binding.radioGroup.getCheckedRadioButtonId() != R.id.vendor || binding.radioGroup.getCheckedRadioButtonId() != R.id.customer) {
                Toast.makeText(this, "Please select a role", Toast.LENGTH_SHORT).show();
            } else if (binding.radioGroup.getCheckedRadioButtonId() == R.id.vendor) {
                IsVendor = "true";
            } else if (binding.radioGroup.getCheckedRadioButtonId() == R.id.customer) {
                IsVendor = "false";
            } else {

                binding.inactivityView.setVisibility(VISIBLE);
                binding.progressBar.setVisibility(VISIBLE);

                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isComplete()) {
                                    uid = task.getResult().getUser().getUid();

                                    UserModel users = new UserModel(uid, name, email, phoneNumber, gender, address, IsVendor);

                                    firebaseDatabase.getReference("Users").child(uid).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            binding.inactivityView.setVisibility(GONE);
                                            binding.progressBar.setVisibility(GONE);
                                            if (IsVendor == "true") {
                                                Intent intent = new Intent(SignUpActivity.this, HomePage.class);
                                                intent.putExtra("IsVendor", "true");
                                                startActivity(intent);
                                                finish();
                                            } else if (IsVendor == "false") {
                                                Intent intent = new Intent(SignUpActivity.this, ClientHomePage.class);
                                                intent.putExtra("IsVendor", "false");
                                                startActivity(intent);
                                                finish();
                                            }

                                        }
                                    }).addOnFailureListener(e -> {
                                        binding.inactivityView.setVisibility(GONE);
                                        binding.progressBar.setVisibility(GONE);
                                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });

                                } else {
                                    binding.inactivityView.setVisibility(GONE);
                                    binding.progressBar.setVisibility(GONE);
                                    Exception e = task.getException();
                                    if (e instanceof FirebaseAuthException) {
                                        String errorCode = ((FirebaseAuthException) e).getErrorCode();
                                        Toast.makeText(SignUpActivity.this, errorCode, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });

    }
}