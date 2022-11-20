package com.example.nannyap.parent.comment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nannyap.CommonUsed;
import com.example.nannyap.databinding.FragmentEvaluationBinding;
import com.example.nannyap.databinding.FragmentParentNannyCommentsBinding;
import com.example.nannyap.model.BookingModel;
import com.example.nannyap.model.Comment;
import com.example.nannyap.model.CommentModel;
import com.example.nannyap.model.ParentModel;
import com.example.nannyap.nanny.evaluation.EvaluationAdapter;
import com.example.nannyap.nanny.evaluation.EvaluationFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ParentNannyCommentsFragment extends Fragment {

    private FragmentParentNannyCommentsBinding binding;
    private DatabaseReference reference;
    private List<CommentModel> commentModels;
    private List<Comment> model;
    private List<BookingModel> bookings;
    private static String nannyId;

    public static EvaluationFragment sendData(String id) {
        EvaluationFragment fragment = new EvaluationFragment();
        //nannyId = id;
        return fragment;
    }
    public ParentNannyCommentsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentParentNannyCommentsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reference = FirebaseDatabase.getInstance().getReference();
        model = new ArrayList<>();
        commentModels = new ArrayList<>();
        binding.recyclerNannyEvaluation.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        getNannyComments();
    }



    private void getNannyComments() {
        binding.nannyEvaluationProgressbar.setVisibility(View.VISIBLE);
        reference.child(CommonUsed.Comment).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentModels.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        CommentModel commentModel = data.getValue(CommentModel.class);
                        commentModels.add(commentModel);
                    }
                    getNannyRatings(commentModels);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getNannyRatings(List<CommentModel> models) {
        model.clear();
        for (int i = 0; i < models.size(); i++) {
            CommentModel commentModel = models.get(i);
            if (CommonUsed.NannyID.equals(commentModel.getNannyId())) {
                reference.child(CommonUsed.Parents).child(commentModel.getParentId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ParentModel parentModel = snapshot.getValue(ParentModel.class);
                        Comment comment = new Comment();
                        comment.setId(commentModel.getCommentId());
                        comment.setDate(commentModel.getDate());
                        comment.setComment(commentModel.getComment());
                        comment.setName(parentModel.getName());
                        comment.setImageUrl(parentModel.getImageUrl());
                        reference.child(CommonUsed.Rating).child(commentModel.getCommentId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                comment.setRate(snapshot.child("rate").getValue().toString());
                                model.add(comment);
                                Log.e("rate", model.get(0).getId());
                                try {
                                    enableViews(model);
                                } catch (Exception e) {
                                    Log.d("getNannyRatings", "" + e);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
        enableViews(model);
    }


    private void enableViews(List<Comment> model) {
        Collections.reverse(model);
        if (model.isEmpty()) {
            binding.nannyEvaluationProgressbar.setVisibility(View.GONE);
            binding.notFoundEvaluation.setVisibility(View.VISIBLE);
            binding.recyclerNannyEvaluation.setVisibility(View.GONE);
        } else {
            binding.nannyEvaluationProgressbar.setVisibility(View.GONE);
            binding.notFoundEvaluation.setVisibility(View.GONE);
            binding.recyclerNannyEvaluation.setVisibility(View.VISIBLE);
            EvaluationAdapter adapter = new EvaluationAdapter();
            adapter.setAdapterList(model);
            binding.recyclerNannyEvaluation.setAdapter(adapter);

        }
    }

}