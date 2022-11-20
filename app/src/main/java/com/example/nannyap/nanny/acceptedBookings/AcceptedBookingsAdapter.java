package com.example.nannyap.nanny.acceptedBookings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nannyap.EventBus.NannyBookingRequestClick;
import com.example.nannyap.EventBus.PassMassageActionClick;
import com.example.nannyap.R;
import com.example.nannyap.model.BookingModel;
import com.example.nannyap.model.BooknigNanny;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class AcceptedBookingsAdapter extends RecyclerView.Adapter<AcceptedBookingsAdapter.CommentViewHolder> {

    private List<BooknigNanny> dataMoldels = new ArrayList<>();
    private List<BookingModel> bookingModels = new ArrayList<>();

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_accepted_booking, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        BooknigNanny model = dataMoldels.get(position);
        holder.parentName.setText(model.getParentName());
        holder.startDate.setText(model.getStartDate());
        holder.endDate.setText(model.getEndDate());
        holder.bookingType.setText(model.getType());

        Glide.with(holder.itemView.getContext()).load(model.getImageUrl()).into(holder.parentImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new NannyBookingRequestClick(false,model, bookingModels.get(position)));
            }
        });

        holder.openChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new PassMassageActionClick(model.getParentId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataMoldels.size();
    }

    public void setAdapterList(List<BooknigNanny> models, List<BookingModel> bookings){
        dataMoldels = models;
        bookingModels = bookings;
        notifyDataSetChanged();
    }
    public class CommentViewHolder extends RecyclerView.ViewHolder{
       private TextView parentName, startDate, endDate, bookingType;
       private ImageView parentImage;
       private Button openChat;

        public CommentViewHolder(@NonNull View itemView){
            super(itemView);
            parentName = itemView.findViewById(R.id.item_accepted_booking_name);
            startDate = itemView.findViewById(R.id.item_accepted_booking_start_date);
            endDate = itemView.findViewById(R.id.item_accepted_booking_end_date);
            bookingType = itemView.findViewById(R.id.item_accepted_booking_type);
            parentImage = itemView.findViewById(R.id.item_accepted_booking_image);
            openChat = itemView.findViewById(R.id.chat_item_accepted_booking_btn);
        }
    }
}
