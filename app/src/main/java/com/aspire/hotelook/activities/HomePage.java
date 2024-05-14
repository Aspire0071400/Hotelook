package com.aspire.hotelook.activities;

import android.Manifest;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aspire.hotelook.R;
import com.aspire.hotelook.adapter.HotelAdapter;
import com.aspire.hotelook.databinding.ActivityHomePageBinding;
import com.aspire.hotelook.dialog.AddHotelDetailsFragment;
import com.aspire.hotelook.model.HotelModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {
    ActivityHomePageBinding binding;
    private HotelAdapter hotelAdapter;
    private ArrayList<HotelModel> hotelList;
    private FirebaseDatabase firebaseDatabase;

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

        String vendor = getIntent().getStringExtra("IsVendor");
        String IsVendor = "true";
        hotelList = new ArrayList<>();
        hotelAdapter = new HotelAdapter(hotelList, getApplicationContext(), this, IsVendor);
        binding.hotelRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.hotelRecyclerView.setAdapter(hotelAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();

        binding.addHotelFab.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted, request it
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        111);
            } else {
                AddHotelDetailsFragment addHotelDetailsFragment = new AddHotelDetailsFragment(hotelAdapter);
                addHotelDetailsFragment.show(getSupportFragmentManager(), "AddHotelDetailsFragment");
            }


        });

        fetchHotelDataFromDatabase();

    }

    private void fetchHotelDataFromDatabase() {
        firebaseDatabase.getReference().child("Hotels").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    hotelList.clear();
                    for (DataSnapshot hotelSnapshot : snapshot.getChildren()) {
                        HotelModel hotelModel = hotelSnapshot.getValue(HotelModel.class);
                        hotelList.add(hotelModel);
                        hotelAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String errorMessage = error.getMessage();
                Toast.makeText(HomePage.this, errorMessage, Toast.LENGTH_SHORT).show();
                hotelAdapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 111) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                AddHotelDetailsFragment addHotelDetailsFragment = new AddHotelDetailsFragment(hotelAdapter);
                addHotelDetailsFragment.show(getSupportFragmentManager(), "AddHotelDetailsFragment");
            }

        }

    }

}