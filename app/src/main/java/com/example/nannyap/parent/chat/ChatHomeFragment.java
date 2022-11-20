package com.example.nannyap.parent.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nannyap.CommonUsed;
import com.example.nannyap.R;
import com.example.nannyap.databinding.FragmentChatHomeBinding;
import com.example.nannyap.databinding.FragmentNannyChatHomeBinding;
import com.example.nannyap.model.BookingModel;
import com.example.nannyap.model.MessageModel;
import com.example.nannyap.model.NannyModel;
import com.example.nannyap.model.ParentModel;
import com.example.nannyap.parent.chat.HomeChatAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ChatHomeFragment extends Fragment {

    private FragmentChatHomeBinding binding;
    private DatabaseReference reference;
    private List<MessageModel> model;
    private List<String> keys;
    private List<BookingModel> bookings;
    private static String nannyId;

    public ChatHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reference = FirebaseDatabase.getInstance().getReference();
        model = new ArrayList<>();
        keys = new ArrayList<>();
        binding.recyclerParentChatHome.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        getNannies();
    }

    private void getNannies() {
        binding.parentChatProgressbar.setVisibility(View.VISIBLE);
        reference.child(CommonUsed.Chat).child(CommonUsed.currentOnlineParent.getId()).addValueEventListener(new ValueEventListener() {
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
            String nannyID = models.get(i);
            reference.child(CommonUsed.Nannies).child(nannyID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    NannyModel nannyModel = snapshot.getValue(NannyModel.class);
                    MessageModel message = new MessageModel();

                    message.setUserId(nannyModel.getName());
                    message.setUserImage(nannyModel.getImageUrl());
                    Query query = reference.child(CommonUsed.Chat).child(CommonUsed.currentOnlineParent.getId()).child(nannyID).orderByKey().limitToLast(1);
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
                                    Log.d("getNannyMessages", "" + e);
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
        Collections.reverse(keys);
        Collections.reverse(model);
        if (model.isEmpty()) {
            binding.parentChatProgressbar.setVisibility(View.GONE);
            binding.notFoundChatHome.setVisibility(View.VISIBLE);
            binding.recyclerParentChatHome.setVisibility(View.GONE);
        } else {
            binding.parentChatProgressbar.setVisibility(View.GONE);
            binding.notFoundChatHome.setVisibility(View.GONE);
            binding.recyclerParentChatHome.setVisibility(View.VISIBLE);
            HomeChatAdapter adapter = new HomeChatAdapter(model, keys);
            adapter.notifyDataSetChanged();
            binding.recyclerParentChatHome.setAdapter(adapter);

        }
    }
}