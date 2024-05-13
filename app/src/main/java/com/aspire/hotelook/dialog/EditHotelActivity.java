package com.aspire.hotelook.dialog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aspire.hotelook.R;
import com.aspire.hotelook.adapter.HotelAdapter;
import com.aspire.hotelook.databinding.ActivityEditHotelBinding;
import com.aspire.hotelook.model.HotelModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditHotelActivity extends AppCompatActivity {
    private ActivityEditHotelBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage storage;
    private Uri imageNewUri = null;
    private String hotelId;
    private String currentImage;
    HotelAdapter hotelAdapter = new HotelAdapter();
    private ArrayList<HotelModel> hotelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityEditHotelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        hotelId = getIntent().getStringExtra("hotelId");
        currentImage = getIntent().getStringExtra("currentImage");
        Glide.with(EditHotelActivity.this).load(currentImage).into(binding.editHotelImage);
        hotelList = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        getExistingDataFromFirestore(hotelId);

        try {
            onStart();
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        binding.updateHotel.setOnClickListener(v -> {
            String hotelName = binding.editHotelName.getText().toString();
            String hotelDescription = binding.editHotelDescription.getText().toString();
            String hotelAddress = binding.editHotelAddress.getText().toString();
            String hotelPrice = binding.editHotelPrice.getText().toString();

//            try {
            if (hotelName.isEmpty() || hotelDescription.isEmpty() || hotelAddress.isEmpty() || hotelPrice.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else if (imageNewUri != null) {

                Map<String, Object> updateData = new HashMap<>();
                updateData.put("hotelName", hotelName);
                updateData.put("hotelDescription", hotelDescription);
                updateData.put("hotelImage", imageNewUri.toString());
                updateData.put("hotelAddress", hotelAddress);
                updateData.put("hotelPrice", hotelPrice);

                //Glide.with(EditHotelActivity.this).load(imageNewUri).into(binding.editHotelImage);

                firebaseDatabase.getReference()
                        .child("Hotels")
                        .child(hotelId).updateChildren(updateData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(EditHotelActivity.this, "Hotel updated successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(e -> {
                            Toast.makeText(EditHotelActivity.this, "Error updating hotel: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });


            } else {

                Map<String, Object> updateData = new HashMap<>();
                updateData.put("hotelName", hotelName);
                updateData.put("hotelDescription", hotelDescription);
                updateData.put("hotelAddress", hotelAddress);
                updateData.put("hotelPrice", hotelPrice);

                firebaseDatabase.getReference()
                        .child("Hotels")
                        .child(hotelId)
                        .updateChildren(updateData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(EditHotelActivity.this, "Hotel updated successfully", Toast.LENGTH_SHORT).show();
                                EditHotelActivity.this.finish();
                            }
                        }).addOnFailureListener(e -> {
                            Toast.makeText(EditHotelActivity.this, "Error fail updating hotel: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });

            }
//            } catch (Exception e) {
//                Toast.makeText(this, "Error e1 updating hotel: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
        });


        binding.editHotelImage.setOnClickListener(v -> {
            openGallery();
        });


    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, 90);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null || data.getData() != null || requestCode == 45 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            StorageReference storageReference = storage.getReference()
                    .child("HotelPics")
                    .child(auth.getCurrentUser().getUid() + System.currentTimeMillis());


            storageReference.putFile(uri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    storageReference.getDownloadUrl().addOnCompleteListener(uriTask -> {
                        if (uriTask.isSuccessful()) {
                            binding.editHotelImage.setImageURI(uri);
                            //Glide.with(this).load(data.getData().toString()).into(hotelImage);
                            imageNewUri = uriTask.getResult();
                            Toast.makeText(this, imageNewUri.toString(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "Error getting download URL: " + Objects.requireNonNull(uriTask.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(this, "Error uploading image: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void getExistingDataFromFirestore(String hotelId) {
        if (hotelId != null) {
            firebaseDatabase.getReference().child("Hotels").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot hotelSnapshot : snapshot.getChildren()) {
                            HotelModel hotelModel = hotelSnapshot.getValue(HotelModel.class);
                            //hotelList.add(hotelModel);
                            if (hotelModel.getHotelId().equals(hotelId)) {
                                binding.editHotelName.setText(hotelModel.getHotelName());
                                binding.editHotelDescription.setText(hotelModel.getHotelDescription());
                                binding.editHotelAddress.setText(hotelModel.getHotelAddress());
                                binding.editHotelPrice.setText(hotelModel.getHotelPrice());
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    String errorMessage = error.getMessage();
                    Toast.makeText(EditHotelActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

}