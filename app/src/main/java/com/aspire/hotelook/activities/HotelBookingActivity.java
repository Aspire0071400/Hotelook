package com.aspire.hotelook.activities;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aspire.hotelook.R;
import com.aspire.hotelook.databinding.ActivityHotelBookingBinding;
import com.bumptech.glide.Glide;

public class HotelBookingActivity extends AppCompatActivity {
    private ActivityHotelBookingBinding binding;

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

        String hotelId = getIntent().getStringExtra("hotelId");
        String hotelName = getIntent().getStringExtra("hotelName");
        String hotelDescription = getIntent().getStringExtra("hotelDescription");
        String hotelAddress = getIntent().getStringExtra("hotelAddress");
        String hotelPrice = getIntent().getStringExtra("hotelPrice");
        String hotelImage = getIntent().getStringExtra("currentImage");

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

        binding.bookingBtn.setOnClickListener(v ->
                Toast.makeText(HotelBookingActivity.this, "Booking system will be added soon", Toast.LENGTH_SHORT).show()
        );

    }

    @Override
    public void finish() {
        super.finish();
    }
}