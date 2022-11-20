package com.example.nannyap.admin.comments;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nannyap.CommonUsed;
import com.example.nannyap.R;
import com.example.nannyap.model.Comment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> dataMoldels = new ArrayList<>();

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment model = dataMoldels.get(position);
        holder.parentName.setText(model.getName());
        holder.comment.setText(model.getComment());
        holder.date.setText(model.getDate());
        holder.rate.setRating(Float.valueOf(model.getRate()));

        Glide.with(holder.itemView).load(model.getImageUrl()).into(holder.parentImage);
        holder.deleteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                SweetAlertDialog dialog = new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE);
                dialog.setTitleText(v.getContext().getString(R.string.confirm));
                dialog.setContentText("هل تريد حذف هذا التعليق حقا؟");
                dialog.setConfirmText(holder.itemView.getContext().getResources().getString(R.string.yes));
                dialog.setConfirmClickListener(sDialog -> {
                    sDialog.dismiss();
                    SweetAlertDialog loader = new SweetAlertDialog(v.getContext(), SweetAlertDialog.PROGRESS_TYPE);
                    loader.setTitle("جاري الحذف...");
                    loader.setCancelable(false);
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();
                    ref.child(CommonUsed.Comment).child(model.getId()).removeValue().addOnCompleteListener((OnCompleteListener<Void>) task -> {
                        if (task.isSuccessful()) {
                            ref.child(CommonUsed.Rating).child(model.getId()).removeValue().addOnCompleteListener((OnCompleteListener<Void>) task1 -> {
                                if (task1.isSuccessful()) {
                                    loader.dismiss();
                                    SweetAlertDialog alertDialog = new SweetAlertDialog(v.getContext(), SweetAlertDialog.SUCCESS_TYPE);
                                    alertDialog.setTitle("تم حذف التعليق بنجاح");
                                    alertDialog.setCancelable(false);
                                    alertDialog.setCanceledOnTouchOutside(false);
                                    alertDialog.setConfirmButton(v.getContext().getString(R.string.ok),ssDialog -> {
                                        ssDialog.dismiss();});
                                    alertDialog.show();
                                } else {
                                    Log.e("comment", task1.getException().getLocalizedMessage());

                                }
                            });
                        } else {
                            Log.e("comment", task.getException().getLocalizedMessage());

                        }
                    });
                });
                dialog.setCancelText(v.getContext().getString(R.string.no));
                dialog.setCancelClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismiss();
                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataMoldels.size();
    }

    public void setAdapterList(List<Comment> models){
        dataMoldels = models;
        notifyDataSetChanged();
    }
    public class CommentViewHolder extends RecyclerView.ViewHolder{
       private TextView parentName, comment, date;
       private RatingBar rate;
       private ImageView parentImage;
       private Button deleteComment;
        public CommentViewHolder(@NonNull View itemView){
            super(itemView);

            parentName = itemView.findViewById(R.id.item_comment_name);
            comment = itemView.findViewById(R.id.item_comment_comment);
            date = itemView.findViewById(R.id.item_comment_date);
            rate = itemView.findViewById(R.id.item_comment_rate);
            parentImage = itemView.findViewById(R.id.item_comment_image);
            deleteComment = itemView.findViewById(R.id.delete_comment_btn);
        }
    }
}
