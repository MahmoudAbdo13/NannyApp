package com.example.nannyap.nanny.acceptedBookings;

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
import com.example.nannyap.databinding.FragmentBookingAcceptedBinding;
import com.example.nannyap.model.BookingModel;
import com.example.nannyap.model.BooknigNanny;
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

public class BookingAcceptedFragment extends Fragment {

    private FragmentBookingAcceptedBinding binding;
    private DatabaseReference reference;
    private List<BooknigNanny> model;
    private List<BookingModel> bookingModels;
    private List<BookingModel> bookings;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBookingAcceptedBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reference = FirebaseDatabase.getInstance().getReference();
        model = new ArrayList<>();
        bookingModels = new ArrayList<>();
        bookings = new ArrayList<>();
        binding.recyclerAcceptedBooking.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        getAccepredBookings();
    }

    private void getAccepredBookings() {
        binding.acceptedBookingProgressbar.setVisibility(View.VISIBLE);
        reference.child(CommonUsed.Booknigs).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookingModels.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        BookingModel bookingModel = data.getValue(BookingModel.class);
                        Log.e("booking id outer", bookingModel.getNannyId());
                        bookingModels.add(bookingModel);
                    }
                    myAccepredBookings(bookingModels);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void myAccepredBookings(List<BookingModel> models) {

        model.clear();
        bookings.clear();
        for (int i = 0; i < models.size(); i++) {
            BookingModel bookingModel = models.get(i);
            Log.e("bookingNannyRequests id", bookingModel.getNannyId());
            Log.e("bookingNannyRequests CommonUsed", CommonUsed.currentOnlineNanny.getNannyId());
            if (FirebaseAuth.getInstance().getUid().equals(bookingModel.getNannyId())) {
                Log.e("booking id", bookingModel.getNannyId());
                binding.acceptedBookingProgressbar.setVisibility(View.VISIBLE);
                binding.notFoundAcceptedBooking.setVisibility(View.GONE);
                reference.child(CommonUsed.Parents).child(bookingModel.getParentId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ParentModel parentModel = snapshot.getValue(ParentModel.class);
                        String address = parentModel.getHome() + " - " + parentModel.getStreet() + " - " + parentModel.getCity();
                        BooknigNanny booknigNanny = new BooknigNanny(bookingModel.getBookingId(), parentModel.getId(), parentModel.getName(), address,
                                parentModel.getImageUrl(), bookingModel.getStartDate(), bookingModel.getEndDate(), bookingModel.getType());
                        model.add(booknigNanny);
                        bookings.add(bookingModel);
                        Log.e("booking", "hdffd");
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
        enableViews(model, bookings);
    }

    private void enableViews(List<BooknigNanny> model, List<BookingModel> bookings) {
        if (bookings.isEmpty()) {
            binding.acceptedBookingProgressbar.setVisibility(View.GONE);
            binding.notFoundAcceptedBooking.setVisibility(View.VISIBLE);
            binding.recyclerAcceptedBooking.setVisibility(View.GONE);
        } else {
            binding.acceptedBookingProgressbar.setVisibility(View.GONE);
            binding.notFoundAcceptedBooking.setVisibility(View.GONE);
            binding.recyclerAcceptedBooking.setVisibility(View.VISIBLE);
            AcceptedBookingsAdapter adapter = new AcceptedBookingsAdapter();
            adapter.setAdapterList(model, bookings);
            binding.recyclerAcceptedBooking.setAdapter(adapter);

        }
    }

}