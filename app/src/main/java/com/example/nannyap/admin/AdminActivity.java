package com.example.nannyap.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.nannyap.CheckLanguage;
import com.example.nannyap.EventBus.AdminNannyClick;
import com.example.nannyap.EventBus.PassMassageActionClick;
import com.example.nannyap.R;
import com.example.nannyap.admin.acceptedNannies.AcceptedNannyDetailsFragment;
import com.example.nannyap.admin.acceptedNannies.AcceptedNannyFragment;
import com.example.nannyap.admin.comments.NannyCommentFragment;
import com.example.nannyap.admin.nannyRequests.NannyDetailsFragment;
import com.example.nannyap.admin.nannyRequests.NannyRequestsFragment;
import com.example.nannyap.outerPages.LoginActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nannyap.databinding.ActivityAdminBinding;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityAdminBinding binding;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        CheckLanguage checkLanguage = new CheckLanguage(AdminActivity.this);
        checkLanguage.UpdateLanguage();

        setSupportActionBar(binding.appBarAdmin.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_nanny_requests, R.id.nav_accepted_nanny, R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_admin);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        toolbar = binding.appBarAdmin.toolbar;
        toolbar.setTitle(R.string.menu_nanny_requests);
        setSupportActionBar(toolbar);

        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        Fragment selectedScreen = new NannyRequestsFragment();
        showFragment(selectedScreen);
        toolbar.setTitle(R.string.menu_nanny_requests);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_admin);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_nanny_requests) {
            Fragment selectedScreen = new NannyRequestsFragment();
            showFragment(selectedScreen);
            toolbar.setTitle(R.string.menu_nanny_requests);
        } else if (id == R.id.nav_accepted_nanny) {
            Fragment selectedScreen = new AcceptedNannyFragment();
            showFragment(selectedScreen);
            toolbar.setTitle(R.string.menu_accepted_nanny);
        } else if (id == R.id.nav_logout) {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.confirm))
                    .setContentText(getString(R.string.do_you_want_logout))
                    .setConfirmText(getString(R.string.yes))
                    .setConfirmClickListener(sDialog -> {
                        sDialog.dismiss();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(this, LoginActivity.class));
                        Log.e("signOut","id: "+FirebaseAuth.getInstance().getUid());
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
        transaction.replace(R.id.nav_host_fragment_content_admin, fragment ); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
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
    public void onNannySelected(AdminNannyClick event) {
        if (event.isSuccess()) {
            toolbar.setTitle(getString(R.string.nanny_details));
            Fragment selectedScreen = NannyDetailsFragment.sendData(event.getNannyModel());
            showFragment(selectedScreen);
        }else {
            toolbar.setTitle(getString(R.string.nanny_details));
            Fragment selectedScreen = AcceptedNannyDetailsFragment.sendData(event.getNannyModel());
            showFragment(selectedScreen);
        }
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onPassMessageSelected(PassMassageActionClick event) {
        if (event.getMessage().equals("DisplayNannyRequest")) {
            toolbar.setTitle(R.string.menu_nanny_requests);
            Fragment selectedScreen = new NannyRequestsFragment();
            showFragment(selectedScreen);
        } else if (event.getMessage().equals("DisplayAcceptedNanny")) {
            toolbar.setTitle(R.string.menu_accepted_nanny);
            Fragment selectedScreen = new AcceptedNannyFragment();
            showFragment(selectedScreen);
        }else {
            toolbar.setTitle(R.string.comments);
            Fragment selectedScreen = NannyCommentFragment.sendData(event.getMessage());
            showFragment(selectedScreen);
        }


    }
}