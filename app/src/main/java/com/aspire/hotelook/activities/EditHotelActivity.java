package com.aspire.hotelook.activities;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditHotelActivity extends AppCompatActivity {
    private ActivityEditHotelBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private Uri imageOldUri, imageNewUri;
    String oldImageUrl;
    private HotelAdapter hotelAdapter;
    HotelModel hotelModel;
    private String hotelId;
    private String currentImage;
    //private ArrayList<HotelModel> hotelList;

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

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        getExistingDataFromFirestore(hotelId);

        binding.updateEditHotel.setOnClickListener(v -> {

            String hotelName = binding.editHotelName.getText().toString();
            String hotelDescription = binding.editHotelDescription.getText().toString();


            try {
                if (hotelName.isEmpty() || hotelDescription.isEmpty()) {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (imageNewUri != null) {
                        String imageUrl = imageNewUri.toString();
                        StorageReference storageReference = storage.getReference().child("HotelPics")
                                .child(auth.getCurrentUser().getUid().toString());

                        storageReference.putFile(Uri.parse(imageUrl)).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                storageReference.getDownloadUrl().addOnCompleteListener(uriTask -> {
                                    if (uriTask.isSuccessful()) {

                                        Map<String, Object> updateData = new HashMap<>();
                                        updateData.put("hotelName", hotelName);
                                        updateData.put("hotelDescription", hotelDescription);
                                        updateData.put("hotelImage", imageUrl);

                                        Glide.with(this).load(imageUrl).into(binding.editHotelImage);
                                        String newImageUrl = uriTask.getResult().toString();

                                        firestore.collection("hotels").document(hotelId)
                                                .update(updateData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(EditHotelActivity.this, "Hotel updated successfully", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(e -> {
                                                    Toast.makeText(EditHotelActivity.this, "Error updating hotel: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                });
                            }
                        });

                    }else{

                        Map<String, Object> updateData = new HashMap<>();
                        updateData.put("hotelName", hotelName);
                        updateData.put("hotelDescription", hotelDescription);

                        firestore.collection("Hotels").document(hotelId)
                                .update(updateData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(EditHotelActivity.this, "Hotel updated successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EditHotelActivity.this, "Error updating hotel: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                }
            }catch (Exception e){
                Toast.makeText(this, "Error updating hotel: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

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
                    .child(auth.getCurrentUser().getUid().toString());

            assert uri != null;
            storageReference.putFile(uri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    storageReference.getDownloadUrl().addOnCompleteListener(uriTask -> {
                        if (uriTask.isSuccessful()) {
                            binding.editHotelImage.setImageURI(uri);
                            //Glide.with(this).load(data.getData().toString()).into(hotelImage);
                            imageNewUri = uriTask.getResult();
                        } else {
                            Toast.makeText(this, "Error getting download URL: " + Objects.requireNonNull(uriTask.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(this, "Error uploading image: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();}
    }

    private void getExistingDataFromFirestore(String hotelId) {
        if(hotelId != null){
            firestore.collection("Hotels").document(hotelId).get()
                    .addOnCompleteListener(task -> {
                        hotelModel = task.getResult().toObject(HotelModel.class);
                        if(hotelModel != null){
                            imageOldUri = Uri.parse(currentImage);
                            binding.editHotelName.setText(hotelModel.getHotelName());
                            binding.editHotelDescription.setText(hotelModel.getHotelDescription());
                            Glide.with(EditHotelActivity.this).load(imageOldUri).into(binding.editHotelImage);
                        }else{
                            Toast.makeText(this, "Error getting hotel data", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

}