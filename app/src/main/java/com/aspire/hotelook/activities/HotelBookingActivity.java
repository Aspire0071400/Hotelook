package com.aspire.hotelook.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aspire.hotelook.R;
import com.aspire.hotelook.databinding.ActivityHotelBookingBinding;

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

//        i.putExtra("hotelId", hotelList.get(position1).getHotelId());
//        i.putExtra("currentImage", hotelList.get(position1).getHotelImage());
//        i.putExtra("hotelName", hotelList.get(position1).getHotelName());
//        i.putExtra("hotelDescription", hotelList.get(position1).getHotelDescription());
//        i.putExtra("hotelAddress", hotelList.get(position1).getHotelAddress());
//        i.putExtra("hotelPrice", hotelList.get(position1).getHotelPrice());

    }
}