package com.aspire.hotelook.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aspire.hotelook.R;
import com.aspire.hotelook.dialog.AddHotelDetailsFragment;
import com.aspire.hotelook.model.HotelModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.ViewHolder>{
    ArrayList<HotelModel> hotelList;
    Context context;
    private FirebaseFirestore firestore;

    public HotelAdapter(ArrayList<HotelModel> hotelList, Context context) {
        this.hotelList = hotelList;
        this.context = context;
    }

    public void setHotelList(ArrayList<HotelModel> hotelList) {
        this.hotelList = hotelList;
        notifyDataSetChanged(); // Notify RecyclerView that data has changed
    }



    @NonNull
    @Override
    public HotelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hotel_view_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelAdapter.ViewHolder holder, int position) {

        HotelModel hotelModel = hotelList.get(position);
        holder.hotelName.setText(hotelModel.getHotelName());
        holder.hotelDescription.setText(hotelModel.getHotelDescription());
        Glide.with(context).load(hotelModel.getHotelImage().toString()).into(holder.hotelImage);

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the position of the clicked item
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    deleteHotelFromFirestore(hotelModel.getHotelId());
                    String hotel = hotelList.get(position).getHotelName();
                    hotelList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, hotel+" deleted from Firestore", Toast.LENGTH_SHORT).show();

                }
            }
        });

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() { return hotelList.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView hotelImage;
        TextView hotelName,hotelDescription;
        ImageButton deleteButton,editButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            hotelImage = itemView.findViewById(R.id.hotel_image);
            hotelName = itemView.findViewById(R.id.hotel_name);
            hotelDescription = itemView.findViewById(R.id.hotel_description);
            deleteButton = itemView.findViewById(R.id.delete_hotel);
            editButton = itemView.findViewById(R.id.edit_hotel);

        }
    }

    private void deleteHotelFromFirestore(String hotelId) {
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("Hotels").document(hotelId).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Hotel deleted from Firestore", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(context, "Failed to delete hotel from Firestore", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
