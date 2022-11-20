package com.example.nannyap.admin.nannyRequests;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nannyap.CommonUsed;
import com.example.nannyap.EventBus.PassMassageActionClick;
import com.example.nannyap.R;
import com.example.nannyap.databinding.FragmentNannyDetailsBinding;
import com.example.nannyap.model.NannyModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class NannyDetailsFragment extends Fragment {

    private FragmentNannyDetailsBinding binding;
    private static NannyModel model;

      public NannyDetailsFragment() {
        // Required empty public constructor
    }

    public static NannyDetailsFragment sendData(NannyModel nannyModel) {
        NannyDetailsFragment fragment = new NannyDetailsFragment();
        model = nannyModel;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNannyDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.nannyDetailsNameData.setText(model.getName());
        binding.nannyDetailsEmailData.setText(model.getEmail());
        binding.nannyDetailsCityData.setText(model.getCity());
        binding.nannyDetailsPhoneData.setText(model.getMobile());
        binding.nannyDetailsStreetData.setText(model.getStreet());
        binding.nannyDetailsHomeNumberData.setText(model.getHome());

        Glide.with(view).load(model.getImageUrl()).into(binding.profileUserImage);

        binding.acceptAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.confirm))
                        .setContentText("هل تريد قبول طلب هذة المربية؟")
                        .setConfirmText(getString(R.string.yes))
                        .setConfirmClickListener(sDialog -> {
                            sDialog.dismiss();
                            acceptNanny();
                        }).setCancelText(getString(R.string.no))
                        .setCancelClickListener(sweetAlertDialog -> {
                            sweetAlertDialog.dismiss();
                        }).show();
            }
        });
        binding.cancelAccountBtn.setOnClickListener( view1 -> {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.confirm))
                    .setContentText("هل تريد رفض طلب هذة المربية؟")
                    .setCancelButtonBackgroundColor(getResources().getColor(R.color.red))
                    .setConfirmText(getString(R.string.yes))
                    .setConfirmClickListener(sDialog -> {
                        sDialog.dismiss();
                        deleteRequest("تم رفض طلب المربية بنجاح");
                    }).setCancelText(getString(R.string.no))
                    .setCancelClickListener(sweetAlertDialog -> {
                        sweetAlertDialog.dismiss();
                    }).show();

        });
    }

    private void acceptNanny() {
        SweetAlertDialog loadingBar = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        loadingBar.setTitleText(getString(R.string.confirm));
        loadingBar.setContentText(getString(R.string.wait));
        loadingBar.setCancelable(false);
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child(CommonUsed.Nannies).child(model.getNannyId()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    loadingBar.dismiss();
                    deleteRequest("تم قبول طلب المربية بنجاح");
                }
            }
        });
    }

    private void deleteRequest(String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child(CommonUsed.NannyRequests).child(model.getNannyId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                dialog.setTitle(message);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setConfirmButton(getString(R.string.ok), sweetAlertDialog ->{
                    EventBus.getDefault().postSticky(new PassMassageActionClick("DisplayNannyRequest"));
                    dialog.dismiss();
                });
                dialog.show();
                //Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }

        });
    }

}