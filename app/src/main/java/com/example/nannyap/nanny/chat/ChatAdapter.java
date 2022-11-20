package com.example.nannyap.nanny.chat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nannyap.CommonUsed;
import com.example.nannyap.R;
import com.example.nannyap.model.MessageModel;

import java.util.List;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.messageViewHolder> {

    List<MessageModel> arrayList;
    String currentUserId;
    Fragment fragment;

    public ChatAdapter(List<MessageModel> arrayList, String currentUserId) {
        this.arrayList = arrayList;
        this.currentUserId = currentUserId;

    }

    @NonNull
    @Override
    public messageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new messageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull messageViewHolder holder, int position) {
        String userId = arrayList.get(position).getUserId();
        if (currentUserId.equals(userId)) {
            holder.receverLayout.setVisibility(View.GONE);
            holder.senderLayout.setVisibility(View.VISIBLE);
            holder.senderMessage.setText(arrayList.get(position).getMessage());
            holder.senderTime.setText(arrayList.get(position).getMessageTime());
            Glide.with(holder.itemView.getContext()).load(arrayList.get(position).getUserImage()).into(holder.senderImage);
        } else {
            holder.receverLayout.setVisibility(View.VISIBLE);
            holder.senderLayout.setVisibility(View.GONE);
            holder.receverMessage.setText(arrayList.get(position).getMessage());
            holder.receverTime.setText(arrayList.get(position).getMessageTime());
            Glide.with(holder.itemView.getContext()).load(arrayList.get(position).getUserImage()).into(holder.receverImage);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class messageViewHolder extends RecyclerView.ViewHolder {
        TextView senderMessage, senderTime, receverMessage, receverTime;
        ImageView senderImage, receverImage;
        RelativeLayout senderLayout, receverLayout;

        public messageViewHolder(@NonNull View itemView) {
            super(itemView);
            receverMessage = (TextView) itemView.findViewById(R.id.receiver_message);
            receverImage = (ImageView) itemView.findViewById(R.id.receiver_image);
            receverTime = (TextView) itemView.findViewById(R.id.receiver_time);
            receverLayout = (RelativeLayout) itemView.findViewById(R.id.receiver_layout);

            senderMessage = (TextView) itemView.findViewById(R.id.sender_message);
            senderImage = (ImageView) itemView.findViewById(R.id.sender_image);
            senderTime = (TextView) itemView.findViewById(R.id.sender_time);
            senderLayout = (RelativeLayout) itemView.findViewById(R.id.sender_layout);
        }
    }
}
