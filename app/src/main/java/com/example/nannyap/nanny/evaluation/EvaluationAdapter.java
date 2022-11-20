package com.example.nannyap.nanny.evaluation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nannyap.R;
import com.example.nannyap.model.Comment;

import java.util.ArrayList;
import java.util.List;

public class EvaluationAdapter extends RecyclerView.Adapter<EvaluationAdapter.CommentViewHolder> {

    private List<Comment> dataMoldels = new ArrayList<>();

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evaluation, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment model = dataMoldels.get(position);
        holder.parentName.setText(model.getName());
        holder.comment.setText(model.getComment());
        holder.date.setText(model.getDate());
        holder.rate.setRating(Float.valueOf(model.getRate()));

        Glide.with(holder.itemView).load(model.getImageUrl()).into(holder.parentImage);

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
        public CommentViewHolder(@NonNull View itemView){
            super(itemView);
            parentName = itemView.findViewById(R.id.item_evaluation_name);
            comment = itemView.findViewById(R.id.item_evaluation_comment);
            date = itemView.findViewById(R.id.item_evaluation_date);
            rate = itemView.findViewById(R.id.item_evaluation_rate);
            parentImage = itemView.findViewById(R.id.item_evaluation_image);

        }
    }
}
