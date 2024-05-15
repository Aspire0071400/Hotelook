package com.aspire.hotelook.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aspire.hotelook.R;
import com.aspire.hotelook.model.BookingRecordModel;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BookingRecordAdapter extends RecyclerView.Adapter<BookingRecordAdapter.ViewHolder> {

    Context context;
    private FirebaseDatabase firebaseDatabase;
    private Activity activity;
    ArrayList<BookingRecordModel> bookingRecordList;

    public BookingRecordAdapter(Context context, ArrayList<BookingRecordModel> bookingRecordModels) {
        this.context = context;
        this.bookingRecordList = bookingRecordModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.booking_record_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return bookingRecordList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
