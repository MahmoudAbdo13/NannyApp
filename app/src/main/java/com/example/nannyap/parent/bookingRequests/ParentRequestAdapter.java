package com.example.nannyap.parent.bookingRequests;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nannyap.CommonUsed;
import com.example.nannyap.R;
import com.example.nannyap.model.BookingModel;
import com.example.nannyap.model.BooknigParent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ParentRequestAdapter extends RecyclerView.Adapter<ParentRequestAdapter.ParentViewHolder> {

    private List<BooknigParent> dataMoldels = new ArrayList<>();
    private List<BookingModel> bookingModels = new ArrayList<>();

    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ParentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parent_booking_request, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ParentViewHolder holder, int position) {
        BooknigParent model = dataMoldels.get(position);
        holder.nannyName.setText(model.getNannyName());
        holder.startDate.setText(model.getStartDate());
        holder.endDate.setText(model.getEndDate());
        holder.bookingType.setText(model.getType());
        holder.bookingType.setText(model.getType());
        holder.status.setText(model.getStatus());

        if (!model.getStatus().equals(holder.itemView.getContext().getString(R.string.status))){
            holder.status.setTextColor(holder.itemView.getContext().getColor(R.color.red));
        }

        Glide.with(holder.itemView.getContext()).load(model.getImageUrl()).into(holder.nannyImage);
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                SweetAlertDialog dialog = new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE);
                dialog.setTitleText(v.getContext().getString(R.string.confirm));
                dialog.setContentText("هل تريد حذف هذا الطلب حقا؟");
                dialog.setConfirmText(holder.itemView.getContext().getResources().getString(R.string.yes));
                dialog.setConfirmClickListener(sDialog -> {
                    sDialog.dismiss();
                    SweetAlertDialog loader = new SweetAlertDialog(v.getContext(), SweetAlertDialog.PROGRESS_TYPE);
                    loader.setTitle("جاري حذف الطلب...");
                    loader.setCancelable(false);
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();
                    ref.child(CommonUsed.BooknigRequests).child(model.getBookingId()).removeValue().addOnCompleteListener((OnCompleteListener<Void>) task -> {
                        if (task.isSuccessful()) {
                            loader.dismiss();
                            SweetAlertDialog alertDialog = new SweetAlertDialog(v.getContext(), SweetAlertDialog.SUCCESS_TYPE);
                            alertDialog.setTitle("تم حذف الطلب بنجاح");
                            alertDialog.setCancelable(false);
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.setConfirmButton(v.getContext().getString(R.string.ok),ssDialog -> {
                                ssDialog.dismiss();});
                            alertDialog.show();
                        } else {
                            Log.e("e", task.getException().getLocalizedMessage());

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

    public void setAdapterList(List<BooknigParent> models, List<BookingModel> bookings) {
        dataMoldels = models;
        bookingModels = bookings;
        notifyDataSetChanged();
    }

    public class ParentViewHolder extends RecyclerView.ViewHolder {
        private TextView status, nannyName, startDate, endDate, bookingType;
        private ImageView nannyImage;
        private Button deleteBtn;

        public ParentViewHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.item_parent_request_status);
            nannyName = itemView.findViewById(R.id.item_parent_request_name);
            startDate = itemView.findViewById(R.id.item_parent_request_start_date);
            endDate = itemView.findViewById(R.id.item_parent_request_end_date);
            bookingType = itemView.findViewById(R.id.item_parent_request_booking_type);
            nannyImage = itemView.findViewById(R.id.item_parent_request_image);
            deleteBtn = itemView.findViewById(R.id.delete_request_btn);
        }
    }
}
