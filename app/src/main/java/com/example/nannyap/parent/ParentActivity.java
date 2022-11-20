package com.example.nannyap.parent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nannyap.CheckLanguage;
import com.example.nannyap.CommonUsed;
import com.example.nannyap.EventBus.AdminNannyClick;
import com.example.nannyap.EventBus.ParentBookingRequestClick;
import com.example.nannyap.EventBus.PassMassageActionClick;
import com.example.nannyap.R;
import com.example.nannyap.databinding.ActivityParentBinding;
import com.example.nannyap.outerPages.LoginActivity;
import com.example.nannyap.parent.acceptedRequest.ParentAcceptedRequestsFragment;
import com.example.nannyap.parent.acceptedRequest.ParentRequestDetailsFragment;
import com.example.nannyap.parent.bookingRequests.ParentRequestsFragment;
import com.example.nannyap.parent.chat.ChatFragment;
import com.example.nannyap.parent.chat.ChatHomeFragment;
import com.example.nannyap.parent.comment.ParentAddCommentFragment;
import com.example.nannyap.parent.comment.ParentNannyCommentsFragment;
import com.example.nannyap.parent.nannies.ParentNanniesFragment;
import com.example.nannyap.parent.nannies.ParentNannyDetailsFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ParentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ActivityParentBinding binding;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityParentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        CheckLanguage checkLanguage = new CheckLanguage(this);
        checkLanguage.UpdateLanguage();

        setSupportActionBar(binding.appBarParent.toolbar);

        View headerView = binding.navView.getHeaderView(0);
        TextView parentName = headerView.findViewById(R.id.parentName);
        TextView parentEmail = headerView.findViewById(R.id.parentEmail);
        ImageView parentImage = headerView.findViewById(R.id.parentImage);

        parentName.setText(CommonUsed.currentOnlineParent.getName());
        parentEmail.setText(CommonUsed.currentOnlineParent.getEmail());
        Glide.with(this).load(CommonUsed.currentOnlineParent.getImageUrl()).into(parentImage);

        toolbar = binding.appBarParent.toolbar;
        setSupportActionBar(toolbar);

        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        toolbar.setTitle(R.string.menu_nannies);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        Fragment selectedScreen = new ParentNanniesFragment();
        toolbar.setTitle(R.string.menu_nannies);
        showFragment(selectedScreen);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_parent_nannies) {
            Fragment selectedScreen = new ParentNanniesFragment();
            showFragment(selectedScreen);
            toolbar.setTitle(R.string.menu_nannies);
        } else if (id == R.id.nav_parent_booking_requests) {
            Fragment selectedScreen = new ParentRequestsFragment();
            showFragment(selectedScreen);
            toolbar.setTitle(R.string.menu_bookings);
        } else if (id == R.id.nav_parent_booking_accepted) {
            Fragment selectedScreen = new ParentAcceptedRequestsFragment();
            showFragment(selectedScreen);
            toolbar.setTitle(R.string.menu_accepted_booking);
        } else if (id == R.id.nav_chat) {
            Fragment selectedScreen = new ChatHomeFragment();
            showFragment(selectedScreen);
            toolbar.setTitle(R.string.menu_chat);
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
        transaction.replace(R.id.nav_host_fragment_content_parent, fragment);
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
    public void onParentNannySelected(AdminNannyClick event) {
        if (event.isSuccess()) {
            toolbar.setTitle(getString(R.string.nanny_details));
            Fragment selectedScreen = ParentNannyDetailsFragment.sendData(event.getNannyModel());
            showFragment(selectedScreen);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onParentBookingSelected(ParentBookingRequestClick event) {
        if (event.isSuccess()) {
            toolbar.setTitle(getString(R.string.booking_details));
            Fragment selectedScreen = ParentRequestDetailsFragment.sendData(event.getBooknigParent(), event.getBookingModel());
            showFragment(selectedScreen);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessagePassed(PassMassageActionClick event) {
       if (event.getMessage().equals("openBookingFragment")) {
            toolbar.setTitle(getString(R.string.menu_booking_nanny));
            Fragment selectedScreen = new BookingFragment();
            showFragment(selectedScreen);
        } else if (event.getMessage().equals("openCommentFragment")) {
            toolbar.setTitle(getString(R.string.comments));
            Fragment selectedScreen = new ParentNannyCommentsFragment();
            showFragment(selectedScreen);
        } else if (event.getMessage().equals("openChatWithThisNanny")) {
            toolbar.setTitle(getString(R.string.menu_chat));
            Fragment selectedScreen = new ChatFragment();
            showFragment(selectedScreen);
        } else if (event.getMessage().equals("openAddCommentFragment")) {
            toolbar.setTitle(getString(R.string.add_comment));
            Fragment selectedScreen = new ParentAddCommentFragment();
            showFragment(selectedScreen);
        } else if (event.getMessage().equals("openAcceptedRequestsFragment")) {
            toolbar.setTitle(getString(R.string.menu_accepted_booking));
            Fragment selectedScreen = new ParentAcceptedRequestsFragment();
            showFragment(selectedScreen);
        } else if (event.getMessage().equals("openNanniesFragment")) {
            toolbar.setTitle(getString(R.string.menu_nannies));
            Fragment selectedScreen = new ParentNanniesFragment();
            showFragment(selectedScreen);
        }
    }


}