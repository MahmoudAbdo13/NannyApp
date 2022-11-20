package com.example.nannyap.parent.chat;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.nannyap.CommonUsed;
import com.example.nannyap.R;
import com.example.nannyap.databinding.FragmentChatBinding;
import com.example.nannyap.databinding.FragmentNannyChatBinding;
import com.example.nannyap.model.MessageModel;
import com.example.nannyap.nanny.chat.ChatAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ChatFragment extends Fragment {

    private FragmentChatBinding binding;
    private DatabaseReference reference;
    private List<MessageModel> messageList;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reference = FirebaseDatabase.getInstance().getReference();
        messageList = new ArrayList<>();
        binding.recyclerParentChat.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.chatSendBtn.setOnClickListener(view1 -> validate());

        getMessages();

    }

    private void validate() {
        String message = binding.parentChatMessageData.getText().toString();

        if (message.isEmpty()) {
            Toast.makeText(getContext(), "لا يمكنك ارسال رسالة فارغة", Toast.LENGTH_SHORT).show();
        } else {
            String time = getTime();
            MessageModel model = new MessageModel(message, CommonUsed.currentOnlineParent.getImageUrl(), CommonUsed.currentOnlineParent.getId(), time);
            sendMessage(model);
        }
    }

    private String getTime() {
        return new SimpleDateFormat("hh:mm a").format(Calendar.getInstance().getTime());
    }

    private void sendMessage(MessageModel messageModel) {

        final String id = new SimpleDateFormat("dd,MM,yyyyHH:mm:ss",Locale.US).format(Calendar.getInstance().getTime());

        reference.child(CommonUsed.Chat).child(messageModel.getUserId()).child(CommonUsed.NannyID).child(id)
                .setValue(messageModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        reference.child(CommonUsed.Chat).child(CommonUsed.NannyID).child(messageModel.getUserId())
                                .child(id).setValue(messageModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        binding.parentChatMessageData.setText("");
                                    }
                                });
                    }
                });
    }

    private void getMessages() {

        reference.child(CommonUsed.Chat).child(CommonUsed.currentOnlineParent.getId()).child(CommonUsed.NannyID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    messageList.clear();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        MessageModel messageModel = data.getValue(MessageModel.class);
                        messageList.add(messageModel);
                    }
                    ChatAdapter adapter = new ChatAdapter(messageList, CommonUsed.currentOnlineParent.getId());
                    binding.recyclerParentChat.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    binding.recyclerParentChat.smoothScrollToPosition(binding.recyclerParentChat.getAdapter().getItemCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}