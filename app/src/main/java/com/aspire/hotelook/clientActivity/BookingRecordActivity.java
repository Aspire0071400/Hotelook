package com.aspire.hotelook.clientActivity;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aspire.hotelook.R;
import com.aspire.hotelook.adapter.BookingRecordAdapter;
import com.aspire.hotelook.databinding.ActivityBookingRecordBinding;
import com.aspire.hotelook.model.BookingRecordModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookingRecordActivity extends AppCompatActivity {
    private ActivityBookingRecordBinding binding;
    private BookingRecordAdapter bookingRecordAdapter;
    private ArrayList<BookingRecordModel> hotelBookingList;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityBookingRecordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        bookingRecordAdapter = new BookingRecordAdapter(this, hotelBookingList);

        binding.ToolbarRecords.setTitle("Booking Records");
        setSupportActionBar(binding.ToolbarRecords);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int color = getResources().getColor(R.color.white);
        binding.ToolbarRecords.getNavigationIcon().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        binding.ToolbarRecords.setNavigationOnClickListener(v -> onSupportNavigateUp());

        fetchHotelBookingRecords();

    }

    private void fetchHotelBookingRecords() {

        firebaseDatabase.getReference().child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("HotelBooking")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}