package com.example.nannyap.nanny.bookingRequest;

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
import com.example.nannyap.databinding.FragmentBookingRequestsBinding;
import com.example.nannyap.model.BookingModel;
import com.example.nannyap.model.BooknigNanny;
import com.example.nannyap.model.ParentModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookingRequestsFragment extends Fragment {

    private FragmentBookingRequestsBinding binding;
    private DatabaseReference reference;
    private List<BooknigNanny> model;
    private List<BookingModel> bookingModels;
    private List<BookingModel> bookings;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBookingRequestsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reference = FirebaseDatabase.getInstance().getReference();
        model = new ArrayList<>();
        bookingModels = new ArrayList<>();
        bookings = new ArrayList<>();
        binding.recyclerBookingRequest.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        getBookingRequest();
    }

    private void getBookingRequest() {
        binding.bookingRequestsProgressbar.setVisibility(View.VISIBLE);
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
            if (FirebaseAuth.getInstance().getUid().equals(bookingModel.getNannyId()) && bookingModel.getStatus().equals(getString(R.string.status))) {
                reference.child(CommonUsed.Parents).child(bookingModel.getParentId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ParentModel parentModel = snapshot.getValue(ParentModel.class);
                        String address = parentModel.getHome()+" - "+parentModel.getStreet()+" - "+ parentModel.getCity();
                        BooknigNanny booknigNanny = new BooknigNanny(bookingModel.getBookingId(), parentModel.getId(), parentModel.getName(),address,
                                parentModel.getImageUrl(), bookingModel.getStartDate(), bookingModel.getEndDate(), bookingModel.getType());
                        model.add(booknigNanny);
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

    private void enableViews(List<BooknigNanny> model, List<BookingModel> bookings) {
        if (bookings.isEmpty()) {
            binding.bookingRequestsProgressbar.setVisibility(View.GONE);
            binding.notFoundBookingRequests.setVisibility(View.VISIBLE);
            binding.recyclerBookingRequest.setVisibility(View.GONE);
        } else {
            binding.bookingRequestsProgressbar.setVisibility(View.GONE);
            binding.notFoundBookingRequests.setVisibility(View.GONE);
            binding.recyclerBookingRequest.setVisibility(View.VISIBLE);
            BookingRequestAdapter adapter = new BookingRequestAdapter();
            adapter.setAdapterList(model, bookings);
            binding.recyclerBookingRequest.setAdapter(adapter);

        }
    }

}