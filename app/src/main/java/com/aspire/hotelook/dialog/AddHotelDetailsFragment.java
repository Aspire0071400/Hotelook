package com.aspire.hotelook.dialog;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.aspire.hotelook.R;
import com.aspire.hotelook.adapter.HotelAdapter;
import com.aspire.hotelook.model.HotelModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class AddHotelDetailsFragment extends DialogFragment {
    private EditText addHotelName, addHotelDescription, addHotelAddress, addHotelPrice;
    private CircleImageView hotelImage;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage storage;
    private Uri imageUri;
    private HotelAdapter hotelAdapter;
    HotelModel hotelModel;


    public AddHotelDetailsFragment(HotelAdapter hotelAdapter) {
        this.hotelAdapter = hotelAdapter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_hotel_details, container, false);

        addHotelName = view.findViewById(R.id.add_hotel_name);
        addHotelDescription = view.findViewById(R.id.add_hotel_description);
        hotelImage = view.findViewById(R.id.hotel_image);
        addHotelPrice = view.findViewById(R.id.add_hotel_price);
        addHotelAddress = view.findViewById(R.id.add_hotel_address);
        Button addHotelBtn = view.findViewById(R.id.add_hotel);
        ImageButton backBtn = view.findViewById(R.id.back_btn);
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        hotelImage.setOnClickListener(v -> {
            openGallery();
        });

        backBtn.setOnClickListener(v -> {
            dismiss();
        });

        addHotelBtn.setOnClickListener(v -> {
            String hotelVendorId = auth.getCurrentUser().getUid();
            String hotelId = auth.getCurrentUser().getUid().toString() + new Date().getTime();
            String hotelName = addHotelName.getText().toString();
            String hotelDescription = addHotelDescription.getText().toString();
            String address = addHotelAddress.getText().toString();
            String price = addHotelPrice.getText().toString();


            try {
                if (hotelName.isEmpty() || hotelDescription.isEmpty() || imageUri == null) {
                    Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    String imageUrl = imageUri.toString();
                    hotelModel = new HotelModel(hotelId, hotelName, hotelDescription, imageUrl, address, price, hotelVendorId);

                    firebaseDatabase.getReference()
                            .child("Hotels")
                            .child(hotelId)
                            .setValue(hotelModel)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getContext(), "Hotel added successfully", Toast.LENGTH_SHORT).show();
                                    hotelAdapter.notifyDataSetChanged();
                                    dismiss();
                                }
                            }).addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Error adding hotel", Toast.LENGTH_SHORT).show();
                            });
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "Error adding hotel" + e.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(e.getMessage());
            }

        });
        return view;
    }

    private void openGallery() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, 45);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null || data.getData() != null || requestCode == 45 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            StorageReference storageReference = storage.getReference()
                    .child("HotelPics")
                    .child(auth.getCurrentUser().getUid().toString() + new Date().getTime());

            assert uri != null;
            storageReference.putFile(uri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    storageReference.getDownloadUrl().addOnCompleteListener(uriTask -> {
                        if (uriTask.isSuccessful()) {
                            hotelImage.setImageURI(uri);
                            imageUri = uriTask.getResult();
                        } else {
                            Toast.makeText(getContext(), "Error getting download URL: " + Objects.requireNonNull(uriTask.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Error uploading image: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
    }


}