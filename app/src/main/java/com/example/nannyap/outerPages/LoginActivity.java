package com.example.nannyap.outerPages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nannyap.CheckLanguage;
import com.example.nannyap.CommonUsed;
import com.example.nannyap.R;
import com.example.nannyap.admin.AdminActivity;
import com.example.nannyap.databinding.ActivityLoginBinding;
import com.example.nannyap.model.NannyModel;
import com.example.nannyap.model.ParentModel;
import com.example.nannyap.nanny.NannyActivity;
import com.example.nannyap.parent.ParentActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        CheckLanguage checkLanguage = new CheckLanguage(this);
        checkLanguage.UpdateLanguage();

        firebaseAuth = FirebaseAuth.getInstance();

        binding.registrationLink.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterationActivity.class)));
        binding.forgetPasswordLink.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class)));

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = binding.loginEmailData.getText().toString();
                String password = binding.loginPasswordData.getText().toString();
                if (email.isEmpty()) {
                    binding.loginEmailData.setError(getString(R.string.enter_email));
                    binding.loginEmailData.setFocusable(true);
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.loginEmailData.setError(getString(R.string.invalid_email));
                    binding.loginEmailData.setFocusable(true);
                } else if (password.isEmpty()) {
                    binding.loginPasswordData.setError(getString(R.string.write_your_password));
                    binding.loginPasswordData.setFocusable(true);
                } else {
                    final SweetAlertDialog loadingBar = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                    loadingBar.setTitleText(getString(R.string.wait));
                    loadingBar.setContentText(getString(R.string.login));
                    loadingBar.setCancelable(false);
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    signIn(email, password, loadingBar);
                }
            }
        });

    }

    public void signIn(String email, String password, SweetAlertDialog dialog) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String uId = firebaseAuth.getCurrentUser().getUid();
                if (uId != null) {
                    RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child("Parents").child(uId).exists()) {
                                CommonUsed.currentOnlineParent = snapshot.child("Parents").child(uId).getValue(ParentModel.class);
                                startActivity(new Intent(LoginActivity.this, ParentActivity.class));
                                dialog.dismiss();
                                finish();
                            } else if (snapshot.child(CommonUsed.Nannies).child(uId).exists()) {
                                CommonUsed.currentOnlineNanny = snapshot.child(CommonUsed.Nannies).child(uId).getValue(NannyModel.class);
                                startActivity(new Intent(LoginActivity.this, NannyActivity.class));
                                dialog.dismiss();
                                finish();
                            } else if (snapshot.child("Admin").child(uId).exists()) {
                                startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                                dialog.dismiss();
                                finish();
                            } else if (snapshot.child(CommonUsed.NannyRequests).child(uId).exists()) {
                                showMessage(getString(R.string.process_nanny_request));
                                dialog.dismiss();
                            } else if (snapshot.child(CommonUsed.NannyRequests).child(uId).exists() && snapshot.child(CommonUsed.Nannies).child(uId).exists()) {
                                showMessage(getString(R.string.blocked_or_deleted));
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            dialog.dismiss();
                            Toast.makeText(dialog.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    dialog.dismiss();
                    Toast.makeText(dialog.getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            SweetAlertDialog warning = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE);
            warning.setTitle(e.getLocalizedMessage());
            warning.setCancelable(false);
            warning.setCanceledOnTouchOutside(false);
            warning.setConfirmClickListener(sDialog -> {
                warning.dismiss();
            });
            warning.show();
        });
    }

    private void showMessage(String message) {
        SweetAlertDialog dialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE);
        dialog.setTitle(getString(R.string.attention));
        dialog.setContentText(message);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setConfirmButton(getString(R.string.ok), sweetAlertDialog -> {
            dialog.dismiss();
        });
        dialog.show();
    }

}