package com.example.nannyap.nanny.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.nannyap.CommonUsed;
import com.example.nannyap.databinding.FragmentNannyChatHomeBinding;
import com.example.nannyap.model.BookingModel;
import com.example.nannyap.model.MessageModel;
import com.example.nannyap.model.ParentModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NannyChatHomeFragment extends Fragment {

    private FragmentNannyChatHomeBinding binding;
    private DatabaseReference reference;
    private List<MessageModel> model;
    private List<String> keys;
    private List<BookingModel> bookings;
    private static String nannyId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNannyChatHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reference = FirebaseDatabase.getInstance().getReference();
        model = new ArrayList<>();
        keys = new ArrayList<>();
        binding.recyclerNannyChat.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        getParents();
    }

    private void getParents() {
        binding.nannyChatProgressbar.setVisibility(View.VISIBLE);
        reference.child(CommonUsed.Chat).child(CommonUsed.currentOnlineNanny.getNannyId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                keys.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        keys.add(data.getKey());
                    }
                    getNannyMessages(keys);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getNannyMessages(List<String> models) {
        model.clear();
        for (int i = 0; i < models.size(); i++) {
            String parentID = models.get(i);
            reference.child(CommonUsed.Parents).child(parentID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ParentModel parentModel = snapshot.getValue(ParentModel.class);
                    MessageModel message = new MessageModel();

                    message.setUserId(parentModel.getName());
                    message.setUserImage(parentModel.getImageUrl());
                    Query query = reference.child(CommonUsed.Chat).child(CommonUsed.currentOnlineNanny.getNannyId()).child(parentID).orderByKey().limitToLast(1);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot data : snapshot.getChildren()) {
                                MessageModel messageModel = data.getValue(MessageModel.class);
                                message.setMessageTime(messageModel.getMessageTime());
                                message.setMessage(messageModel.getMessage());
                                model.add(message);
                                try {
                                    enableViews(model);
                                } catch (Exception e) {
                                    Log.d("getNannies", "" + e);
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        enableViews(model);
    }

    private void enableViews(List<MessageModel> model) {
        if (model.isEmpty()) {
            binding.nannyChatProgressbar.setVisibility(View.GONE);
            binding.notFoundChat.setVisibility(View.VISIBLE);
            binding.recyclerNannyChat.setVisibility(View.GONE);
        } else {
            binding.nannyChatProgressbar.setVisibility(View.GONE);
            binding.notFoundChat.setVisibility(View.GONE);
            binding.recyclerNannyChat.setVisibility(View.VISIBLE);
            HomeChatAdapter adapter = new HomeChatAdapter(model, keys);
            adapter.notifyDataSetChanged();
            binding.recyclerNannyChat.setAdapter(adapter);

        }
    }
}