package com.example.nannyap.parent.nannies;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.nannyap.CommonUsed;
import com.example.nannyap.admin.acceptedNannies.AccpetedNannyAdapter;
import com.example.nannyap.databinding.FragmentParentNanniesBinding;
import com.example.nannyap.model.NannyModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ParentNanniesFragment extends Fragment {

    private FragmentParentNanniesBinding binding;
    private DatabaseReference reference;
    private List<NannyModel> model;
    private boolean status = true;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentParentNanniesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("الاسم");
        arrayList.add("المدينة");
        arrayList.add("متاحة");
        arrayList.add("مشغولة");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.userSearchView.setAdapter(arrayAdapter);
        binding.userSearchView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String opprtType = adapterView.getItemAtPosition(i).toString();
                if (i==0){
                    status = true;
                    getAcceptedNannies("name");
                }else if (i==1){
                    status = true;
                    getAcceptedNannies("city");
                }else if (i==2){
                    status = false;
                    getAcceptedNannies("available");
                }else  if (i==3){
                    status = true;
                    getAcceptedNannies("available");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child(CommonUsed.Nannies);
        model = new ArrayList<>();
        binding.recyclerAcceptedNanny.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

    }

    private void getAcceptedNannies(String sortChild) {
        binding.nannyProgressbarAdmin.setVisibility(View.VISIBLE);
        reference.orderByChild(sortChild).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                model.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        NannyModel nannyModel = data.getValue(NannyModel.class);
                        model.add(nannyModel);
                    }
                    try {
                        if (status) {
                            enableViews(model);
                        }else {
                            Collections.reverse(model);
                            enableViews(model);
                        }
                    } catch (Exception e) {
                        Log.d("getAcceptedNannies", "" + e);
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
            ParentNannyAdapter adapter = new ParentNannyAdapter();
            adapter.setAdapterList(model);
            binding.recyclerAcceptedNanny.setAdapter(adapter);
        }
    }


}