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
import com.example.nannyap.EventBus.PassMassageActionClick;
import com.example.nannyap.R;
import com.example.nannyap.model.MessageModel;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class HomeChatAdapter extends RecyclerView.Adapter<HomeChatAdapter.messageViewHolder> {

    List<MessageModel> arrayList;
    List<String> userIds;
    Fragment fragment;

    public HomeChatAdapter(List<MessageModel> arrayList, List<String> userIds) {
        this.arrayList = arrayList;
        this.userIds = userIds;

    }

    @NonNull
    @Override
    public messageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new messageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_home, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull messageViewHolder holder, int position) {
            holder.name.setText(arrayList.get(position).getUserId());
            holder.message.setText(arrayList.get(position).getMessage());
            holder.time.setText(arrayList.get(position).getMessageTime());
            Glide.with(holder.itemView.getContext()).load(arrayList.get(position).getUserImage()).into(holder.image);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().postSticky(new PassMassageActionClick(userIds.get(position)));
                }
            });
         }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class messageViewHolder extends RecyclerView.ViewHolder {
        TextView name, message, time;
        ImageView image;

        public messageViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_chat_name);
            image =  itemView.findViewById(R.id.item_chat_image);
            time = itemView.findViewById(R.id.item_chat_massage_time);
            message = itemView.findViewById(R.id.item_chat_message);
        }
    }
}
