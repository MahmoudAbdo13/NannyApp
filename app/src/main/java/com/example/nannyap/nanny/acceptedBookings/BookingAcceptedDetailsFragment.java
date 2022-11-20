package com.example.nannyap.nanny.acceptedBookings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nannyap.EventBus.PassMassageActionClick;
import com.example.nannyap.R;
import com.example.nannyap.databinding.FragmentBookingAcceptedDetailsBinding;
import com.example.nannyap.model.BookingModel;
import com.example.nannyap.model.BooknigNanny;
import com.google.firebase.database.DatabaseReference;

import org.greenrobot.eventbus.EventBus;


public class BookingAcceptedDetailsFragment extends Fragment {

    private FragmentBookingAcceptedDetailsBinding binding;
    private static BooknigNanny modelNanny;
    private static BookingModel model;
    private DatabaseReference reference;

    public static BookingAcceptedDetailsFragment sendData(BooknigNanny nannyModel, BookingModel bookingModel) {
        BookingAcceptedDetailsFragment fragment = new BookingAcceptedDetailsFragment();
        modelNanny = nannyModel;
        model = bookingModel;
        return fragment;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBookingAcceptedDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.parentNameData.setText(modelNanny.getParentName());
        binding.parentStartDateData.setText(model.getStartDate());
        binding.parentEndDateData.setText(model.getEndDate());
        binding.parentStartTimeData.setText(model.getStartTime());
        binding.parentEndTimeData.setText(model.getEndTime());
        binding.specialNeedsData.setText(model.getSpecialNeeds());
        binding.childAgeData.setText(model.getAge());
        binding.bookingTypeData.setText(model.getType());
        binding.parentNoteData.setText(model.getNote());
        binding.parentAddressData.setText(modelNanny.getParentAddress());
        binding.chatBtn.setOnClickListener(v -> EventBus.getDefault().postSticky(new PassMassageActionClick(model.getParentId())));
    }

}