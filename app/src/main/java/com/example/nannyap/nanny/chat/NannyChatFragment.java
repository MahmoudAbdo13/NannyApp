package com.example.nannyap.nanny.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.nannyap.CommonUsed;
import com.example.nannyap.databinding.FragmentNannyChatBinding;
import com.example.nannyap.model.MessageModel;
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

public class NannyChatFragment extends Fragment {

    private FragmentNannyChatBinding binding;
    private DatabaseReference reference;
    private List<MessageModel> messageList;

    public NannyChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNannyChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reference = FirebaseDatabase.getInstance().getReference();
        messageList = new ArrayList<>();
        binding.recyclerNannyChat.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.chatSendBtn.setOnClickListener(view1 -> validate());
        getMessages();
    }

    private void validate() {
        String message = binding.chatMessageData.getText().toString();

        if (message.isEmpty()) {
            Toast.makeText(getContext(), "لا يمكنك ارسال رسالة فارغة", Toast.LENGTH_SHORT).show();
        } else {
            String time = getTime();
            MessageModel model = new MessageModel(message, CommonUsed.currentOnlineNanny.getImageUrl(), CommonUsed.currentOnlineNanny.getNannyId(), time);
            sendMessage(model);
        }
    }

    private String getTime() {
        return new SimpleDateFormat("hh:mm a").format(Calendar.getInstance().getTime());
    }

    private void sendMessage(MessageModel messageModel) {
        final String id = new SimpleDateFormat("dd,MM,yyyyHH:mm:ss",Locale.US).format(Calendar.getInstance().getTime());

        reference.child(CommonUsed.Chat).child(messageModel.getUserId()).child(CommonUsed.ParentID).child(id)
                .setValue(messageModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    reference.child(CommonUsed.Chat).child(CommonUsed.ParentID).child(messageModel.getUserId()).child(id)
                            .setValue(messageModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        binding.chatMessageData.setText("");
                                    }
                                }
                            });
                }
            }
        });
    }

    private void getMessages() {

        reference.child(CommonUsed.Chat).child(CommonUsed.currentOnlineNanny.getNannyId()).child(CommonUsed.ParentID).orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    messageList.clear();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        MessageModel messageModel = data.getValue(MessageModel.class);
                        messageList.add(messageModel);

                    }
                    ChatAdapter adapter = new ChatAdapter(messageList, CommonUsed.currentOnlineNanny.getNannyId());
                    binding.recyclerNannyChat.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    binding.recyclerNannyChat.smoothScrollToPosition(binding.recyclerNannyChat.getAdapter().getItemCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}