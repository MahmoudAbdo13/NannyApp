package com.example.nannyap.nanny;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nannyap.CommonUsed;
import com.example.nannyap.R;
import com.example.nannyap.databinding.FragmentNannyProfileBinding;
import com.example.nannyap.model.NannyModel;
import com.example.nannyap.model.RatingModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class NannyProfileFragment extends Fragment {

    private FragmentNannyProfileBinding binding;
    private boolean specialNeeds;
    private final Calendar myCalendar = Calendar.getInstance();
    private float rate = 0;
    private int counter = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNannyProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.languageData.setText(CommonUsed.currentOnlineNanny.getLanguage());
        binding.nannyAgeData.setText(CommonUsed.currentOnlineNanny.getAge());
        binding.nannyNationalityData.setText(CommonUsed.currentOnlineNanny.getNationality());
        binding.bookingTimeData.setText(CommonUsed.currentOnlineNanny.getBookingTime());
        binding.childrenAgeData.setText(CommonUsed.currentOnlineNanny.getChildrenAge());
        binding.nannyBioData.setText(CommonUsed.currentOnlineNanny.getBio());
        binding.statusCheckBox.setChecked(CommonUsed.currentOnlineNanny.isAvailable());
        Log.e("id", CommonUsed.currentOnlineNanny.getNannyId());

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("نعم");
        arrayList.add("لا");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.specialNeedsData.setAdapter(arrayAdapter);
        binding.specialNeedsData.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    specialNeeds = true;
                } else {
                    specialNeeds = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });

        binding.saveNannyDataBtn.setOnClickListener(view1 -> {
            saveNannyData();
        });
    }


    private void saveNannyData() {
        String language, age, nationality, bookingTime, childAge, bio;

        language = binding.languageData.getText().toString();
        age = binding.nannyAgeData.getText().toString();
        nationality = binding.nannyNationalityData.getText().toString();
        bookingTime = binding.bookingTimeData.getText().toString();
        childAge = binding.childrenAgeData.getText().toString();
        bio = binding.nannyBioData.getText().toString();


        if (language.isEmpty()) {
            binding.languageData.setError(getString(R.string.write_your_lagnuage));
            binding.languageData.setFocusable(true);
        } else if (age.isEmpty()) {
            binding.nannyAgeData.setError(getString(R.string.write_your_age));
            binding.nannyAgeData.setFocusable(true);
        } else if (nationality.isEmpty()) {
            binding.nannyNationalityData.setError(getString(R.string.write_your_nationality));
            binding.nannyNationalityData.setFocusable(true);
        } else if (bookingTime.isEmpty()) {
            binding.bookingTimeData.setError(getString(R.string.write_your_time));
            binding.bookingTimeData.setFocusable(true);
        } else if (childAge.isEmpty()) {
            binding.childrenAgeData.setError(getString(R.string.write_your_child_age));
            binding.childrenAgeData.setFocusable(true);
        } else if (bio.isEmpty()) {
            binding.nannyBioData.setError(getString(R.string.write_your_bio));
            binding.nannyBioData.setFocusable(true);
        } else {
            boolean status;
            if (binding.statusCheckBox.isChecked()) {
                status = true;
            } else {
                status = false;
            }
            final SweetAlertDialog loadingBar = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            loadingBar.setTitleText(getString(R.string.wait));
            loadingBar.setContentText(getString(R.string.update));
            loadingBar.setCancelable(false);
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            NannyModel model = CommonUsed.currentOnlineNanny;
            NannyModel nannyModel = new NannyModel(model.getNannyId(), model.getName(), model.getEmail(), model.getCity(), model.getStreet()
                    , model.getHome(), bookingTime, language, nationality, bio, model.getImageUrl(), age, childAge, model.getMobile()
                    , specialNeeds, status, model.getRate());
            updateNannyProfile(nannyModel, loadingBar);
        }
    }

    private void updateNannyProfile(NannyModel nannyModel, SweetAlertDialog loadingBar) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child(CommonUsed.Rating).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    RatingModel ratingModel = dataSnapshot.getValue(RatingModel.class);
                    if (CommonUsed.currentOnlineNanny.getNannyId().equals(ratingModel.getNannyId())) {
                        counter+=1;
                        rate += Float.valueOf(ratingModel.getRate());
                    }
                }
                try {
                    calculateRate(counter,rate);
                } catch (Exception e) {
                    Log.d("getNannies", "" + e);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child(CommonUsed.Nannies).child(nannyModel.getNannyId()).setValue(nannyModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingBar.dismiss();
                SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                dialog.setTitle("تم تحديث البيانات بنجاح");
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setConfirmButton(getString(R.string.ok), sweetAlertDialog -> {
                    dialog.dismiss();
                });
                dialog.show();
            }
        });
    }

    private void calculateRate(int number, float totalRate) {
        float rate = totalRate / number;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child(CommonUsed.Nannies).child(CommonUsed.currentOnlineNanny.getNannyId()).child("rate").setValue(rate);
    }
}