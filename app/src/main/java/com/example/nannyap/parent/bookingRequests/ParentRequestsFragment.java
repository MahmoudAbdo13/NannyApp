package com.example.nannyap.parent.bookingRequests;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.nannyap.CommonUsed;
import com.example.nannyap.R;
import com.example.nannyap.databinding.FragmentParentRequestsBinding;
import com.example.nannyap.model.BookingModel;
import com.example.nannyap.model.BooknigNanny;
import com.example.nannyap.model.BooknigParent;
import com.example.nannyap.model.NannyModel;
import com.example.nannyap.model.ParentModel;
import com.example.nannyap.nanny.bookingRequest.BookingRequestAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ParentRequestsFragment extends Fragment {

    private FragmentParentRequestsBinding binding;
    private DatabaseReference reference;
    private List<BooknigParent> model;
    private List<BookingModel> bookingModels;
    private List<BookingModel> bookings;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentParentRequestsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reference = FirebaseDatabase.getInstance().getReference();
        model = new ArrayList<>();
        bookingModels = new ArrayList<>();
        bookings = new ArrayList<>();
        binding.recyclerParentBooking.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        getBookingRequest();
    }

    private void getBookingRequest() {
        binding.parentBookingProgressbar.setVisibility(View.VISIBLE);
        reference.child(CommonUsed.BooknigRequests).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookingModels.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        BookingModel bookingModel = data.getValue(BookingModel.class);
                        bookingModels.add(bookingModel);
                    }
                    myBookingRequests(bookingModels);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void myBookingRequests(List<BookingModel> models) {
        model.clear();
        bookings.clear();
        for (int i = 0; i < models.size(); i++) {
            BookingModel bookingModel = models.get(i);
            if (FirebaseAuth.getInstance().getUid().equals(bookingModel.getParentId())) {
                //
                reference.child(CommonUsed.Nannies).child(bookingModel.getNannyId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        NannyModel nannyModel = snapshot.getValue(NannyModel.class);
                        BooknigParent booknigParent = new BooknigParent(bookingModel.getBookingId(), nannyModel.getNannyId(), nannyModel.getName(),
                                bookingModel.getStatus(),nannyModel.getImageUrl(), bookingModel.getStartDate(), bookingModel.getEndDate(), bookingModel.getType());
                        model.add(booknigParent);
                        bookings.add(bookingModel);
                        try {
                            enableViews(model, bookings);
                        } catch (Exception e) {
                            Log.d("getNannies", "" + e);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
        enableViews(model,bookings);
    }

    private void enableViews(List<BooknigParent> model, List<BookingModel> bookings) {
        if (bookings.isEmpty()) {
            binding.parentBookingProgressbar.setVisibility(View.GONE);
            binding.notFoundParentBooking.setVisibility(View.VISIBLE);
            binding.recyclerParentBooking.setVisibility(View.GONE);
        } else {
            binding.parentBookingProgressbar.setVisibility(View.GONE);
            binding.notFoundParentBooking.setVisibility(View.GONE);
            binding.recyclerParentBooking.setVisibility(View.VISIBLE);
            ParentRequestAdapter adapter = new ParentRequestAdapter();
            adapter.setAdapterList(model, bookings);
            binding.recyclerParentBooking.setAdapter(adapter);

        }
    }

}