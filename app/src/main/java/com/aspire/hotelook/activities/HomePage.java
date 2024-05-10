package com.aspire.hotelook.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aspire.hotelook.dialog.AddHotelDetailsFragment;
import com.aspire.hotelook.R;
import com.aspire.hotelook.adapter.HotelAdapter;
import com.aspire.hotelook.databinding.ActivityHomePageBinding;
import com.aspire.hotelook.model.HotelModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity implements AddHotelDetailsFragment.OnDialogDismissListener{
    ActivityHomePageBinding binding;
    private RecyclerView hotelRecyclerView;
    private HotelAdapter hotelAdapter;
    private ArrayList<HotelModel> hotelList;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FragmentManager fragmentManager = getSupportFragmentManager();

        hotelRecyclerView = findViewById(R.id.hotel_recycler_view);
        hotelList = new ArrayList<>();
        hotelAdapter = new HotelAdapter(hotelList,getApplicationContext());
        hotelRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        hotelRecyclerView.setAdapter(hotelAdapter);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        binding.addHotelFab.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted, request it
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        111);
            } else {
                AddHotelDetailsFragment addHotelDetailsFragment = new AddHotelDetailsFragment(hotelAdapter,this::onDialogDismissed);
                addHotelDetailsFragment.show(getSupportFragmentManager(), "AddHotelDetailsFragment");
            }



        });

        fetchHotelDataFromFirestore();

    }

    private void fetchHotelDataFromFirestore() {
        firestore.collection("Hotels").get().addOnSuccessListener(queryDocumentSnapshots -> {
            hotelList.addAll(queryDocumentSnapshots.toObjects(HotelModel.class));
            hotelAdapter.setHotelList(hotelList);
            hotelAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 111) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                AddHotelDetailsFragment addHotelDetailsFragment = new AddHotelDetailsFragment(hotelAdapter,this::onDialogDismissed);
                addHotelDetailsFragment.show(getSupportFragmentManager(), "AddHotelDetailsFragment");
            }

        }

    }

    @Override
    public void onDialogDismissed() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

}