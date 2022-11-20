package com.example.nannyap.admin.acceptedNannies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.nannyap.CommonUsed;
import com.example.nannyap.EventBus.PassMassageActionClick;
import com.example.nannyap.R;
import com.example.nannyap.databinding.FragmentAcceptedNannyDetailsBinding;
import com.example.nannyap.model.NannyModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AcceptedNannyDetailsFragment extends Fragment {

    private FragmentAcceptedNannyDetailsBinding binding;
    private static NannyModel model;

    public static AcceptedNannyDetailsFragment sendData(NannyModel nannyModel) {
        AcceptedNannyDetailsFragment fragment = new AcceptedNannyDetailsFragment();
        model = nannyModel;
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAcceptedNannyDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.acceptedNannyNameData.setText(model.getName());
        binding.acceptedNannyDetailsEmailData.setText(model.getEmail());
        binding.acceptedNannyDetailsCityData.setText(model.getCity());
        binding.acceptedNannyDetailsPhoneData.setText(model.getMobile());
        binding.acceptedNannyDetailsStreetData.setText(model.getStreet());
        binding.acceptedNannyDetailsHomeNumberData.setText(model.getHome());
        System.out.println(model.getNannyId());

        Glide.with(view).load(model.getImageUrl()).into(binding.profileUserImage);


        binding.blockBtn.setOnClickListener(view1 -> {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.confirm))
                    .setContentText("هل تريد حظر هذة المربية؟")
                    .setCancelButtonBackgroundColor(getResources().getColor(R.color.red))
                    .setConfirmText(getString(R.string.yes))
                    .setConfirmClickListener(sDialog -> {
                        sDialog.dismiss();
                        blockNanny("تم حظر المربية بنجاح");
                    }).setCancelText(getString(R.string.no))
                    .setCancelClickListener(sweetAlertDialog -> {
                        sweetAlertDialog.dismiss();
                    }).show();
        });

        binding.commentsBtn.setOnClickListener(view1 -> {

            EventBus.getDefault().postSticky(new PassMassageActionClick(model.getNannyId()));
            CommonUsed.NannyID = model.getNannyId();
            System.out.println("Accepted Nanny Adapter "+CommonUsed.NannyID);

        });
    }


    private void blockNanny(String message) {
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
        reference2.child(CommonUsed.Nannies).child(model.getNannyId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                dialog.setTitle(message);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setConfirmButton(getString(R.string.ok), sweetAlertDialog -> {
                    EventBus.getDefault().postSticky(new PassMassageActionClick("DisplayAcceptedNanny"));
                    dialog.dismiss();
                });
                dialog.show();
                //Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}