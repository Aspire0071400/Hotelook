package com.aspire.hotelook.clientActivity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aspire.hotelook.R;
import com.aspire.hotelook.adapter.HotelAdapter;
import com.aspire.hotelook.databinding.ActivityClientHomePageBinding;
import com.aspire.hotelook.model.HotelModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClientHomePage extends AppCompatActivity {
    private ActivityClientHomePageBinding binding;
    private HotelAdapter hotelAdapter;
    private ArrayList<HotelModel> hotelList;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityClientHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String IsVendor = "IsVendor";
        hotelList = new ArrayList<>();
        hotelAdapter = new HotelAdapter(hotelList, getApplicationContext(), this, IsVendor);
        binding.clientHotelRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.clientHotelRecyclerView.setAdapter(hotelAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();

        binding.clientBookingListFab.setOnClickListener(v -> {
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
                Toast.makeText(ClientHomePage.this, errorMessage, Toast.LENGTH_SHORT).show();
                hotelAdapter.notifyDataSetChanged();

            }
        });
    }
}