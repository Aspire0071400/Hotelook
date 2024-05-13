package com.aspire.hotelook.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aspire.hotelook.R;
import com.aspire.hotelook.activities.HotelBookingActivity;
import com.aspire.hotelook.dialog.EditHotelActivity;
import com.aspire.hotelook.model.HotelModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.ViewHolder> {
    ArrayList<HotelModel> hotelList;
    Context context;
    private FirebaseDatabase firebaseDatabase;

    private Activity activity;

    public HotelAdapter(ArrayList<HotelModel> hotelList, Context context, Activity activity) {
        this.hotelList = hotelList;
        this.context = context;
        this.activity = activity;
    }

    public HotelAdapter() {
    }

    @NonNull
    @Override
    public HotelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hotel_view_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelAdapter.ViewHolder holder, int position) {

        HotelModel hotelModel = hotelList.get(position);
        holder.hotelName.setText(hotelModel.getHotelName());
        holder.hotelDescription.setText(hotelModel.getHotelDescription());
        holder.hotelAddress.setText(hotelModel.getHotelAddress());
        holder.hotelPrice.setText(hotelModel.getHotelPrice());
        Glide.with(context).load(hotelModel.getHotelImage()).into(holder.hotelImage);

        holder.itemView.setOnClickListener(v -> {
            int position1 = holder.getAdapterPosition();
            if (activity != null) {
                Intent i = new Intent(context, HotelBookingActivity.class);
                i.putExtra("hotelId", hotelList.get(position1).getHotelId());
                i.putExtra("currentImage", hotelList.get(position1).getHotelImage());
                i.putExtra("hotelName", hotelList.get(position1).getHotelName());
                i.putExtra("hotelDescription", hotelList.get(position1).getHotelDescription());
                i.putExtra("hotelAddress", hotelList.get(position1).getHotelAddress());
                i.putExtra("hotelPrice", hotelList.get(position1).getHotelPrice());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            } else {
                Toast.makeText(context, "Unable to start activity", Toast.LENGTH_SHORT).show();
            }
        });

        holder.deleteButton.setOnClickListener(v -> {

            deleteHotelFromFirestore(hotelModel.getHotelId());
            HotelAdapter.this.notifyDataSetChanged();

//            int position12 = holder.getAdapterPosition();
//            if (position12 != RecyclerView.NO_POSITION) {
//                String hotel = hotelList.get(position12).getHotelName();
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("Delete Hotel");
//                builder.setMessage("Are you sure you want to delete " + hotel + " ?");
//                builder.setPositiveButton("Yes", (dialogInterface, i) -> {
//                    deleteHotelFromFirestore(hotelModel.getHotelId());
////                           hotelList.remove(position);
////                           notifyItemRemoved(position);
//                    Toast.makeText(context, hotel + " deleted from Database", Toast.LENGTH_SHORT).show();
//                });
//                builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            }
        });

        holder.editButton.setOnClickListener(v -> {
            int position1 = holder.getAdapterPosition();
            if (activity != null) {
                Intent i = new Intent(context, EditHotelActivity.class);
                i.putExtra("hotelId", hotelList.get(position1).getHotelId());
                i.putExtra("currentImage", hotelList.get(position1).getHotelImage());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            } else {
                Toast.makeText(context, "Unable to start activity", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView hotelImage;
        TextView hotelName, hotelDescription, hotelPrice, hotelAddress;
        ImageButton deleteButton, editButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            hotelImage = itemView.findViewById(R.id.hotel_image);
            hotelName = itemView.findViewById(R.id.hotel_name);
            hotelDescription = itemView.findViewById(R.id.hotel_description);
            deleteButton = itemView.findViewById(R.id.delete_hotel);
            editButton = itemView.findViewById(R.id.edit_hotel);
            hotelPrice = itemView.findViewById(R.id.hotel_price);
            hotelAddress = itemView.findViewById(R.id.hotel_address);

        }
    }

    private void deleteHotelFromFirestore(String hotelId) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference()
                .child("Hotels").
                child(hotelId).
                removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Hotel deleted from Database", Toast.LENGTH_SHORT).show();
                        HotelAdapter.this.notifyDataSetChanged();
                    }
                }).addOnFailureListener(e -> Toast.makeText(context, "Failed to delete hotel from Database", Toast.LENGTH_SHORT).show());
    }

}
