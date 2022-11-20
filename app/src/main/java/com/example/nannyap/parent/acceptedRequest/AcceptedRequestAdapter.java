package com.example.nannyap.parent.acceptedRequest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nannyap.CommonUsed;
import com.example.nannyap.EventBus.NannyBookingRequestClick;
import com.example.nannyap.EventBus.ParentBookingRequestClick;
import com.example.nannyap.EventBus.PassMassageActionClick;
import com.example.nannyap.R;
import com.example.nannyap.model.BookingModel;
import com.example.nannyap.model.BooknigNanny;
import com.example.nannyap.model.BooknigParent;
import com.example.nannyap.model.CommentModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class AcceptedRequestAdapter extends RecyclerView.Adapter<AcceptedRequestAdapter.CommentViewHolder> {

    private List<BooknigParent> dataMoldels = new ArrayList<>();
    private List<BookingModel> bookingModels = new ArrayList<>();

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_accepted_request, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        BooknigParent model = dataMoldels.get(position);
        holder.nannyName.setText(model.getNannyName());
        holder.startDate.setText(model.getStartDate());
        holder.endDate.setText(model.getEndDate());
        holder.bookingType.setText(model.getType());

        Glide.with(holder.itemView.getContext()).load(model.getImageUrl()).into(holder.nannyImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new ParentBookingRequestClick(true,model, bookingModels.get(position)));
            }
        });

        holder.openChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUsed.NannyID = model.getNannyId();
                EventBus.getDefault().postSticky(new PassMassageActionClick("openChatWithThisNanny"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataMoldels.size();
    }

    public void setAdapterList(List<BooknigParent> models, List<BookingModel> bookings){
        dataMoldels = models;
        bookingModels = bookings;
        notifyDataSetChanged();
    }
    public class CommentViewHolder extends RecyclerView.ViewHolder{
       private TextView nannyName, startDate, endDate, bookingType;
       private ImageView nannyImage;
       private Button openChat;

        public CommentViewHolder(@NonNull View itemView){
            super(itemView);
            nannyName = itemView.findViewById(R.id.item_accepted_request_name);
            startDate = itemView.findViewById(R.id.item_accepted_request_start_date);
            endDate = itemView.findViewById(R.id.item_accepted_request_end_date);
            bookingType = itemView.findViewById(R.id.item_accepted_request_booking_type);
            nannyImage = itemView.findViewById(R.id.item_accepted_request_image);
            openChat = itemView.findViewById(R.id.chat_item_accepted_request_btn);
        }
    }
}
