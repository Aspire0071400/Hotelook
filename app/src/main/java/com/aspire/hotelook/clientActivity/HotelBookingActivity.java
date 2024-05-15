package com.aspire.hotelook.clientActivity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aspire.hotelook.R;
import com.aspire.hotelook.databinding.ActivityHotelBookingBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class HotelBookingActivity extends AppCompatActivity {
    private ActivityHotelBookingBinding binding;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private String currentUserId;
    private String bookingId, hotelId, hotelName, hotelDescription, hotelAddress, hotelPrice, hotelImage, hotelVendorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHotelBookingBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        hotelVendorId = getIntent().getStringExtra("hotelVendorId");
        hotelId = getIntent().getStringExtra("hotelId");
        hotelName = getIntent().getStringExtra("hotelName");
        hotelDescription = getIntent().getStringExtra("hotelDescription");
        hotelAddress = getIntent().getStringExtra("hotelAddress");
        hotelPrice = getIntent().getStringExtra("hotelPrice");
        hotelImage = getIntent().getStringExtra("currentImage");

        binding.materialToolbarBooking.setTitle(hotelName);
        setSupportActionBar(binding.materialToolbarBooking);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int color = getResources().getColor(R.color.white);
        binding.materialToolbarBooking.getNavigationIcon().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        binding.materialToolbarBooking.setNavigationOnClickListener(v -> finish());

        binding.bookingHotelDescription.setText(hotelDescription);
        binding.bookingHotelAddress.setText(hotelAddress);
        binding.priceBookingBar.setTitle("₹ " + hotelPrice);
        Glide.with(this).load(hotelImage).into(binding.bookingHotelImage);

        currentUserId = auth.getCurrentUser().getUid();
        bookingId = hotelId + currentUserId + System.currentTimeMillis();

        checkHotelBookedExist();

        binding.bookingBtn.setOnClickListener(v -> {

            bookHotel();

        });

    }

    private void bookHotel() {

        Map<String, Object> hotelBookingDetails = new HashMap<>();
        hotelBookingDetails.put("bookingId", bookingId);
        hotelBookingDetails.put("vendorId", hotelVendorId);
        hotelBookingDetails.put("hotelId", hotelId);
        hotelBookingDetails.put("hotelName", hotelName);
        hotelBookingDetails.put("hotelPrice", hotelPrice);
        hotelBookingDetails.put("hotelImage", hotelImage);
        hotelBookingDetails.put("hotelDescription", hotelDescription);
        hotelBookingDetails.put("hotelAddress", hotelAddress);


        firebaseDatabase.getReference().child("Users")
                .child(currentUserId)
                .child("HotelBookings")
                .child(hotelId)
                .setValue(hotelBookingDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Map<String, Object> hotelBookingDetailsForVendor = new HashMap<>();
                        hotelBookingDetailsForVendor.put("clientId", currentUserId);
                        hotelBookingDetailsForVendor.put("bookingId", bookingId);
                        hotelBookingDetailsForVendor.put("bookingStatus", "pending");

                        firebaseDatabase.getReference().child("Hotels")
                                .child(hotelId)
                                .child("HotelBookings")
                                .child(bookingId)
                                .setValue(hotelBookingDetailsForVendor).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Toast.makeText(HotelBookingActivity.this, "Booking Successful", Toast.LENGTH_SHORT).show();
                                        binding.bookingBtn.setEnabled(false);
                                        binding.bookingBtn.setText("BOOKED");
                                        binding.bookingBtn.setTextColor(getColor(R.color.black));
                                        binding.bookingBtn.setBackgroundColor(getColor(R.color.inactivity_grey));

                                    }
                                });
                    }
                });
    }


    private void checkHotelBookedExist() {
        firebaseDatabase.getReference().child("Users")
                .child(currentUserId)
                .child("HotelBookings")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            if (snapshot.hasChild(hotelId)) {
                                Toast.makeText(HotelBookingActivity.this, "Hotel is already booked!", Toast.LENGTH_SHORT).show();
                                binding.bookingBtn.setEnabled(false);
                                binding.bookingBtn.setText("BOOKED");
                                binding.bookingBtn.setTextColor(getColor(R.color.black));
                                binding.bookingBtn.setBackgroundColor(getColor(R.color.inactivity_grey));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(HotelBookingActivity.this, "Failed to check booking status", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void finish() {
        super.finish();
    }
}