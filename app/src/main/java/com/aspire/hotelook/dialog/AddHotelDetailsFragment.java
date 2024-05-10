package com.aspire.hotelook.dialog;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.aspire.hotelook.R;
import com.aspire.hotelook.activities.HomePage;
import com.aspire.hotelook.adapter.HotelAdapter;
import com.aspire.hotelook.databinding.FragmentAddHotelDetailsBinding;
import com.aspire.hotelook.model.HotelModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class AddHotelDetailsFragment extends DialogFragment {
    private EditText addHotelName, addHotelDescription;
    private CircleImageView hotelImage;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private Uri imageUri;
    private HotelAdapter hotelAdapter;
    HotelModel hotelModel;
    private OnDialogDismissListener dismissListener;


    public AddHotelDetailsFragment(HotelAdapter hotelAdapter, OnDialogDismissListener dismissListener) {
        this.hotelAdapter = hotelAdapter;
        this.dismissListener = dismissListener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_hotel_details, container, false);

        addHotelName = view.findViewById(R.id.add_hotel_name);
        addHotelDescription = view.findViewById(R.id.add_hotel_description);
        hotelImage = view.findViewById(R.id.hotel_image);
        Button addHotelBtn = view.findViewById(R.id.add_hotel);
        ImageButton backBtn = view.findViewById(R.id.back_btn);
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        hotelImage.setOnClickListener(v -> {
            openGallery();
        });

        backBtn.setOnClickListener(v->{
            dismiss();
        });

        addHotelBtn.setOnClickListener(v->{
            String hotelId = auth.getCurrentUser().getUid().toString() + new Date().getTime();
            //String currentUserId = auth.getCurrentUser().getUid();
            String hotelName = addHotelName.getText().toString();
            String hotelDescription = addHotelDescription.getText().toString();
            String imageUrl = imageUri.toString();


            if (hotelName.isEmpty() || hotelDescription.isEmpty() || imageUri == null) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }else{
                if(!imageUrl.isEmpty())
                {
                    hotelModel = new HotelModel(hotelId, hotelName, hotelDescription, imageUrl);
                    firestore.collection("Hotels").document(hotelId).set(hotelModel)
                            .addOnCompleteListener(firestoreTask -> {
                                if (firestoreTask.isSuccessful()) {
                                    Toast.makeText(getContext(), "Hotel added successfully", Toast.LENGTH_SHORT).show();
                                    hotelAdapter.notifyDataSetChanged();
                                    dismiss();
                                } else {
                                    Toast.makeText(getContext(), "Error adding hotel", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

        });

        return view;
    }

    private void openGallery(){

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
                    .child(auth.getCurrentUser().getUid().toString());

            storageReference.putFile(uri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    storageReference.getDownloadUrl().addOnCompleteListener(uriTask -> {
                        if (uriTask.isSuccessful()) {
                            hotelImage.setImageURI(uri);
                            //Glide.with(this).load(data.getData().toString()).into(hotelImage);
                            imageUri = uri;
                        } else {
                            Toast.makeText(getContext(), "Error getting download URL: " + Objects.requireNonNull(uriTask.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Error uploading image: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();}
    }

    public interface OnDialogDismissListener {
        void onDialogDismissed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissListener.onDialogDismissed();
    }


}