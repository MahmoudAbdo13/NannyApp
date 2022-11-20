package com.example.nannyap.admin.nannyRequests;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class NannyRquestAdapter extends RecyclerView.Adapter<NannyRquestAdapter.NannyViewHolder> {

    private List<NannyModel> dataMoldels = new ArrayList<>();

    @NonNull
    @Override
    public NannyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NannyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nanny_request, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NannyViewHolder holder, int position) {
        NannyModel nannyModel = dataMoldels.get(position);
        holder.name.setText(nannyModel.getName());
        holder.phone.setText(nannyModel.getMobile());
        holder.email.setText(nannyModel.getEmail());

        Glide.with(holder.itemView).load(nannyModel.getImageUrl()).into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new AdminNannyClick(true,nannyModel));
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
       private TextView name, phone, email;
       private ImageView image;
        public NannyViewHolder(@NonNull View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.item_nanny_request_name);
            phone = itemView.findViewById(R.id.item_nanny_request_phone);
            email = itemView.findViewById(R.id.item_nanny_request_email);
            image = itemView.findViewById(R.id.item_nanny_request_image);
        }
    }
}
