package com.example.nannyap.parent.nannies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.nannyap.CommonUsed;
import com.example.nannyap.EventBus.PassMassageActionClick;
import com.example.nannyap.R;
import com.example.nannyap.databinding.FragmentParentNannyDetailsBinding;
import com.example.nannyap.model.BookingModel;
import com.example.nannyap.model.NannyModel;

import org.greenrobot.eventbus.EventBus;

public class ParentNannyDetailsFragment extends Fragment {

    private FragmentParentNannyDetailsBinding binding;
    private static NannyModel model;

    public static ParentNannyDetailsFragment sendData(NannyModel nannyModel) {
        ParentNannyDetailsFragment fragment = new ParentNannyDetailsFragment();
        model = nannyModel;
        return fragment;
    }

    public ParentNannyDetailsFragment() {
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
        binding = FragmentParentNannyDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.parentNannyNameData.setText(model.getName());
        binding.nannyAgeData.setText(model.getAge());
        binding.childrenAgeData.setText(model.getChildrenAge());
        binding.nannyNationalityData.setText(model.getNationality());
        binding.bookingTimeData.setText(model.getBookingTime());
        binding.bioData.setText(model.getBio());

        if (model.isSpecialNeedsChildren()) {
            binding.specialNeedsData.setText(getString(R.string.yes));
        } else {
            binding.specialNeedsData.setText(getString(R.string.no));
        }

        Glide.with(view).load(model.getImageUrl()).into(binding.profileNannyImage);

        CommonUsed.NannyID = model.getNannyId();

        binding.bookBtn.setOnClickListener(v -> EventBus.getDefault().postSticky(new PassMassageActionClick("openBookingFragment")));
        binding.showCommentsBtn.setOnClickListener(v -> EventBus.getDefault().postSticky(new PassMassageActionClick("openCommentFragment")));
    }

    private void openFragment(Fragment someFragment) {

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_content_parent, someFragment); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.setReorderingAllowed(true);
        transaction.commit();
    }
}