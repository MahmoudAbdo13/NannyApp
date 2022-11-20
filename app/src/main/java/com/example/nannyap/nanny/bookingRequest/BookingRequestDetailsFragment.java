package com.example.nannyap.nanny.bookingRequest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nannyap.CommonUsed;
import com.example.nannyap.EventBus.PassMassageActionClick;
import com.example.nannyap.R;
import com.example.nannyap.databinding.FragmentBookingRequestDetailsBinding;
import com.example.nannyap.model.BookingModel;
import com.example.nannyap.model.BooknigNanny;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BookingRequestDetailsFragment extends Fragment {

    private FragmentBookingRequestDetailsBinding binding;
    private static BooknigNanny modelNanny;
    private static BookingModel model;
    private DatabaseReference reference;

    public static BookingRequestDetailsFragment sendData(BooknigNanny nannyModel, BookingModel bookingModel) {
        BookingRequestDetailsFragment fragment = new BookingRequestDetailsFragment();
        modelNanny = nannyModel;
        model = bookingModel;
        return fragment;
    }

    public BookingRequestDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookingRequestDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reference = FirebaseDatabase.getInstance().getReference();

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

        binding.acceptBookingBtn.setOnClickListener(v -> acceptBooking());
        binding.cancelBookingBtn.setOnClickListener(v -> cancelBooking("تم رفض الحجز بنجاح"));
    }

    private void acceptBooking() {
        SweetAlertDialog loadingBar = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        loadingBar.setTitleText(getString(R.string.confirming));
        loadingBar.setContentText(getString(R.string.wait));
        loadingBar.setCancelable(false);
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        model.setStatus("تم القبول");
        reference.child(CommonUsed.Booknigs).child(model.getBookingId()).setValue(model)
                .addOnCompleteListener(task -> reference.child(CommonUsed.BooknigRequests)
                        .child(model.getBookingId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                loadingBar.dismiss();
                                SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                                dialog.setTitle("تم قبول الحجز بنجاح \nالان يمكنك رؤيته في الحجوزات المقبولة");
                                dialog.setCancelable(false);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setConfirmButton(getString(R.string.ok), sweetAlertDialog -> {
                                    EventBus.getDefault().postSticky(new PassMassageActionClick("DisplayBookingRequest"));
                                    dialog.dismiss();
                                });
                                dialog.show();
                            }
                        }));
    }

    private void cancelBooking(String message) {
        SweetAlertDialog loadingBar = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        loadingBar.setTitleText(getString(R.string.confirming));
        loadingBar.setContentText(getString(R.string.wait));
        loadingBar.setCancelable(false);
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        reference.child(CommonUsed.BooknigRequests).child(model.getBookingId()).child(CommonUsed.Status).setValue("تم الرفض").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingBar.dismiss();
                SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                dialog.setTitle(message);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setConfirmButton(getString(R.string.ok), sweetAlertDialog -> {
                    EventBus.getDefault().postSticky(new PassMassageActionClick("DisplayBookingRequest"));
                    dialog.dismiss();
                });
                dialog.show();
            }
        });
    }
}