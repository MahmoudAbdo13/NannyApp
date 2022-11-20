package com.example.nannyap.admin.acceptedNannies;

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
import com.example.nannyap.databinding.FragmentAcceptedNannyBinding;
import com.example.nannyap.model.NannyModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AcceptedNannyFragment extends Fragment {

    private FragmentAcceptedNannyBinding binding;
    private DatabaseReference reference;
    private List<NannyModel> model;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAcceptedNannyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reference = FirebaseDatabase.getInstance().getReference().child(CommonUsed.Nannies);
        model = new ArrayList<>();
        binding.recyclerAcceptedNanny.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        getAcceptedNannies();
    }

    private void getAcceptedNannies() {
        binding.nannyProgressbarAdmin.setVisibility(View.VISIBLE);
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
                            Log.d("getAcceptedNannies", "" + e);
                        }
                    }
                } else {
                    enableViews(model);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void enableViews(List<NannyModel> model) {
        if (model.isEmpty()) {
            binding.nannyProgressbarAdmin.setVisibility(View.GONE);
            binding.notFoundAcceptedAdmin.setVisibility(View.VISIBLE);
            binding.recyclerAcceptedNanny.setVisibility(View.GONE);
        } else {
            binding.nannyProgressbarAdmin.setVisibility(View.GONE);
            binding.notFoundAcceptedAdmin.setVisibility(View.GONE);
            binding.recyclerAcceptedNanny.setVisibility(View.VISIBLE);
            AccpetedNannyAdapter adapter = new AccpetedNannyAdapter();
            adapter.setAdapterList(model);
            binding.recyclerAcceptedNanny.setAdapter(adapter);
        }
    }

}