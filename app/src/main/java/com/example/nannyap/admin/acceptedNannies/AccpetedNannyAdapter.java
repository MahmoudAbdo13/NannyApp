package com.example.nannyap.admin.acceptedNannies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nannyap.EventBus.AdminNannyClick;
import com.example.nannyap.R;
import com.example.nannyap.model.NannyModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class AccpetedNannyAdapter extends RecyclerView.Adapter<AccpetedNannyAdapter.NannyViewHolder> {

    private List<NannyModel> dataMoldels = new ArrayList<>();

    @NonNull
    @Override
    public NannyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NannyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nanny_accepted, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NannyViewHolder holder, int position) {
        NannyModel nannyModel = dataMoldels.get(position);
        holder.name.setText(nannyModel.getName());
        holder.rate.setRating(nannyModel.getRate());

        Glide.with(holder.itemView).load(nannyModel.getImageUrl()).into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new AdminNannyClick(false,nannyModel));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataMoldels.size();
    }

    public void setAdapterList(List<NannyModel> models){
        dataMoldels = models;
        notifyDataSetChanged();
    }
    public class NannyViewHolder extends RecyclerView.ViewHolder{
       private TextView name, price;
       private ImageView image;
        private RatingBar rate;
        public NannyViewHolder(@NonNull View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.item_accepted_nanny_name);
            rate = itemView.findViewById(R.id.item_accepted_nanny_rate);
            image = itemView.findViewById(R.id.item_accepted_nanny_image);
        }
    }
}
