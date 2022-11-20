package com.example.nannyap.nanny;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.nannyap.CheckLanguage;
import com.example.nannyap.CommonUsed;
import com.example.nannyap.EventBus.NannyBookingRequestClick;
import com.example.nannyap.EventBus.PassMassageActionClick;
import com.example.nannyap.R;
import com.example.nannyap.databinding.ActivityNannyBinding;
import com.example.nannyap.nanny.acceptedBookings.BookingAcceptedDetailsFragment;
import com.example.nannyap.nanny.acceptedBookings.BookingAcceptedFragment;
import com.example.nannyap.nanny.bookingRequest.BookingRequestDetailsFragment;
import com.example.nannyap.nanny.bookingRequest.BookingRequestsFragment;
import com.example.nannyap.nanny.chat.NannyChatFragment;
import com.example.nannyap.nanny.chat.NannyChatHomeFragment;
import com.example.nannyap.nanny.evaluation.EvaluationFragment;
import com.example.nannyap.outerPages.LoginActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class NannyActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityNannyBinding binding;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNannyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        CheckLanguage checkLanguage = new CheckLanguage(this);
        checkLanguage.UpdateLanguage();

        setSupportActionBar(binding.appBarNanny.toolbar);

        View headerView = binding.navView.getHeaderView(0);
        TextView nannyName = headerView.findViewById(R.id.nannyName);
        TextView nannyEmail = headerView.findViewById(R.id.nannyEmail);
        ImageView nannyImage = headerView.findViewById(R.id.nannyImage);

        nannyName.setText(CommonUsed.currentOnlineNanny.getName());
        nannyEmail.setText(CommonUsed.currentOnlineNanny.getEmail());
        Glide.with(NannyActivity.this).load(CommonUsed.currentOnlineNanny.getImageUrl()).into(nannyImage);

        toolbar = binding.appBarNanny.toolbar;
        setSupportActionBar(toolbar);

        drawer = binding.drawerLayout;
        navigationView = binding.navView;

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        Fragment selectedScreen = new NannyProfileFragment();
        showFragment(selectedScreen);
        toolbar.setTitle(R.string.menu_profile);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_nanny_profile) {
            Fragment selectedScreen = new NannyProfileFragment();
            showFragment(selectedScreen);
            toolbar.setTitle(R.string.menu_profile);
        } else if (id == R.id.nav_nanny_booking_requests) {
            Fragment selectedScreen = new BookingRequestsFragment();
            showFragment(selectedScreen);
            toolbar.setTitle(R.string.menu_bookings);
        } else if (id == R.id.nav_nanny_booking_accepted) {
            Fragment selectedScreen = new BookingAcceptedFragment();
            showFragment(selectedScreen);
            toolbar.setTitle(R.string.menu_accepted_booking);
        } else if (id == R.id.nav_nanny_chat) {
            Fragment selectedScreen = new NannyChatHomeFragment();
            showFragment(selectedScreen);
            toolbar.setTitle(R.string.menu_chat);
        } else if (id == R.id.nav_nanny_evaluation) {
            Fragment selectedScreen = new EvaluationFragment();
            showFragment(selectedScreen);
            toolbar.setTitle(R.string.menu_evaluation);
        } else if (id == R.id.nav_logout) {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.confirm))
                    .setContentText(getString(R.string.do_you_want_logout))
                    .setConfirmText(getString(R.string.yes))
                    .setConfirmClickListener(sDialog -> {
                        sDialog.dismiss();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(this, LoginActivity.class));
                        Log.e("signOut", "id: " + FirebaseAuth.getInstance().getUid());
                        finish();
                    }).setCancelText(getString(R.string.no))
                    .setCancelClickListener(sweetAlertDialog -> {
                        sweetAlertDialog.dismiss();
                    }).show();
        }

        DrawerLayout drawer = binding.drawerLayout;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_content_nanny, fragment);
        transaction.addToBackStack(null);
        transaction.setReorderingAllowed(true);
        transaction.commit();
    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onBookingRequestSelected(NannyBookingRequestClick event) {
        if (event.isSuccess()) {
            toolbar.setTitle(getString(R.string.booking_details));
            Fragment selectedScreen = BookingRequestDetailsFragment.sendData(event.getBooknigNanny(), event.getBookingModel());
            showFragment(selectedScreen);
        } else {
            toolbar.setTitle(getString(R.string.booking_details));
            Fragment selectedScreen = BookingAcceptedDetailsFragment.sendData(event.getBooknigNanny(), event.getBookingModel());
            showFragment(selectedScreen);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessagePassed(PassMassageActionClick event) {
        if (event.getMessage().equals("DisplayBookingRequest")) {
            toolbar.setTitle(getString(R.string.menu_bookings));
            Fragment selectedScreen = new BookingRequestsFragment();
            showFragment(selectedScreen);
        } else {
            toolbar.setTitle(getString(R.string.menu_chat));
            CommonUsed.ParentID = event.getMessage();
            Fragment selectedScreen = new NannyChatFragment();
            showFragment(selectedScreen);
        }
    }


}