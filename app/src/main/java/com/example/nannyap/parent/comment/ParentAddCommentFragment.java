package com.example.nannyap.parent.comment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.nannyap.CommonUsed;
import com.example.nannyap.EventBus.PassMassageActionClick;
import com.example.nannyap.R;
import com.example.nannyap.databinding.FragmentParentAddCommentBinding;
import com.example.nannyap.model.CommentModel;
import com.example.nannyap.model.RatingModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ParentAddCommentFragment extends Fragment {

    private FragmentParentAddCommentBinding binding;
    private float rate = 0;

    public ParentAddCommentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentParentAddCommentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.sendCommentBtn.setOnClickListener(view1 -> validateData());
        binding.nannyName.setText(CommonUsed.NannyName);
        Glide.with(getContext()).load(CommonUsed.NannyImageUrl).into(binding.nannyImage);
        binding.nannyRate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rate = v;
            }
        });

    }

    private void validateData() {
        String comment;
        comment = binding.commentData.getText().toString();
        if (rate == 0) {
            Toast.makeText(getContext(), "من فضلك اضف تقييم", Toast.LENGTH_SHORT).show();
        } else if (comment.isEmpty()) {
            binding.commentData.setError("من فضلك اضف تعليق");
            binding.commentData.setFocusable(true);
        } else {
            saveEvaluation(comment, String.valueOf(rate));
        }
    }

    private void saveEvaluation(String comment, String rate) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(Calendar.getInstance().getTime());

        String id = reference.push().getKey();
        RatingModel ratingModel = new RatingModel(id, CommonUsed.currentOnlineParent.getId(), CommonUsed.NannyID, rate);
        CommentModel commentModel = new CommentModel(id, CommonUsed.currentOnlineParent.getId(), CommonUsed.NannyID, comment, date);

        SweetAlertDialog loader = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        loader.setTitle("جاري التقييم...");
        loader.setCancelable(false);
        loader.setCanceledOnTouchOutside(false);
        loader.show();
        reference.child(CommonUsed.Comment).child(id).setValue(commentModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                reference.child(CommonUsed.Rating).child(id).setValue(ratingModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loader.dismiss();
                        SweetAlertDialog alertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                        alertDialog.setTitle("تم تقييم المربية وإضافة التعليق بنجاح");
                        alertDialog.setCancelable(false);
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setConfirmButton(getContext().getString(R.string.ok), ssDialog -> {
                            EventBus.getDefault().postSticky(new PassMassageActionClick("openAcceptedRequestsFragment"));
                            ssDialog.dismiss();
                        });
                        alertDialog.show();
                    }
                });
            }
        });
    }
}