package com.example.nannyap.admin.nannyRequests;

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
import com.example.nannyap.databinding.FragmentRequestsNannyBinding;
import com.example.nannyap.model.NannyModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NannyRequestsFragment extends Fragment {

    private FragmentRequestsNannyBinding binding;
    private DatabaseReference reference;
    private List<NannyModel> model;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRequestsNannyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reference = FirebaseDatabase.getInstance().getReference().child(CommonUsed.NannyRequests);
        model = new ArrayList<>();
        binding.recyclerNannyRequest.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        getNannies();
    }

    private void getNannies() {
        binding.nannyProgressbarManager.setVisibility(View.VISIBLE);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                model.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        NannyModel nannyModel = data.getValue(NannyModel.class);
                        model.add(nannyModel);
                        try {
                            enableViews(model);
                        } catch (Exception e) {
                            Log.d("getNannies", "" + e);
                        }
                    }
                } else {
                    enableViews(model);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enableViews(List<NannyModel> model) {
        if (model.isEmpty()) {
            binding.nannyProgressbarManager.setVisibility(View.GONE);
            binding.notFoundNanniesManager.setVisibility(View.VISIBLE);
            binding.recyclerNannyRequest.setVisibility(View.GONE);
        } else {
            binding.nannyProgressbarManager.setVisibility(View.GONE);
            binding.notFoundNanniesManager.setVisibility(View.GONE);
            binding.recyclerNannyRequest.setVisibility(View.VISIBLE);
            NannyRquestAdapter adapter = new NannyRquestAdapter();
            adapter.setAdapterList(model);
            binding.recyclerNannyRequest.setAdapter(adapter);

        }
    }
}