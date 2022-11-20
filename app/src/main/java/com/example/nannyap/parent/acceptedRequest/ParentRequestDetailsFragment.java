package com.example.nannyap.parent.acceptedRequest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.nannyap.CommonUsed;
import com.example.nannyap.EventBus.PassMassageActionClick;
import com.example.nannyap.databinding.FragmentParentRequestDetailsBinding;
import com.example.nannyap.model.BookingModel;
import com.example.nannyap.model.BooknigParent;

import org.greenrobot.eventbus.EventBus;


public class ParentRequestDetailsFragment extends Fragment {

    private FragmentParentRequestDetailsBinding binding;
    private static BooknigParent bookingParent;
    private static BookingModel model;

    public static ParentRequestDetailsFragment sendData(BooknigParent booknigParent, BookingModel bookingModel) {
        ParentRequestDetailsFragment fragment = new ParentRequestDetailsFragment();
        bookingParent = booknigParent;
        model = bookingModel;
        return fragment;
    }

    public ParentRequestDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentParentRequestDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.nannyNameData.setText(bookingParent.getNannyName());
        binding.parentStartDateData.setText(model.getStartDate());
        binding.parentEndDateData.setText(model.getEndDate());
        binding.parentStartTimeData.setText(model.getStartTime());
        binding.parentEndTimeData.setText(model.getEndTime());
        System.out.println("Needs"+model.getSpecialNeeds());
        binding.specialNeedsData.setText(model.getSpecialNeeds());
        binding.childAgeData.setText(model.getAge());
        binding.bookingTypeData.setText(model.getType());
        binding.parentNoteData.setText(model.getNote());
        Glide.with(getContext()).load(bookingParent.getImageUrl()).into(binding.profileUserImage);

        CommonUsed.NannyID= bookingParent.getNannyId();
        CommonUsed.NannyName= bookingParent.getNannyName();
        CommonUsed.NannyImageUrl= bookingParent.getImageUrl();
        binding.addCommentBtn.setOnClickListener(v -> EventBus.getDefault().postSticky(new PassMassageActionClick("openAddCommentFragment")));
    }
}