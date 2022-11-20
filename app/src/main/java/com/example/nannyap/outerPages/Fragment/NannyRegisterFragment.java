package com.example.nannyap.outerPages.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nannyap.CommonUsed;
import com.example.nannyap.R;
import com.example.nannyap.databinding.FragmentNannyRegisterBinding;
import com.example.nannyap.model.NannyModel;
import com.example.nannyap.outerPages.LoginActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class NannyRegisterFragment extends Fragment {

    private FragmentNannyRegisterBinding binding;
    private SweetAlertDialog loadingBar;
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private static final int GalleryPick = 10;
    private Uri ImageUri;
    private StorageReference parentImagesRef;
    private DatabaseReference parentRef;
    private String downloadImageUrl;
    private WifiManager wifiManager;
    private final static int PLACE_PICKER_REQUEST = 100;

    public NannyRegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNannyRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child(CommonUsed.NannyRequests);
        parentImagesRef = FirebaseStorage.getInstance().getReference().child("Nanny Images");

        binding.nannyRegistrationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        binding.nannyRegistrationBtn.setOnClickListener(v -> {
            valitation();
        });
    }

    private void valitation() {
        String name, email, password, confirmPassword, phone, city, street, home;

        name = binding.nannyRegistrationNameData.getText().toString();
        email = binding.nannyRegistrationEmailData.getText().toString();
        password = binding.nannyRegistrationPasswordData.getText().toString();
        confirmPassword = binding.nannyRegistrationConfirmPasswordData.getText().toString();
        phone = binding.nannyRegistrationPhoneData.getText().toString();
        city = binding.nannyEgistrationCityData.getText().toString();
        street = binding.nannyRegistrationStreetData.getText().toString();
        home = binding.nannyRegistrationHomeNumberData.getText().toString();

        if (ImageUri == null) {
            Toast.makeText(getContext(), "اختار صورة من فضلك", Toast.LENGTH_SHORT).show();
        } else if (name.isEmpty()) {
            binding.nannyRegistrationNameData.setError(getString(R.string.write_your_name));
            binding.nannyRegistrationNameData.setFocusable(true);
        } else if (email.isEmpty()) {
            binding.nannyRegistrationEmailData.setError(getString(R.string.write_your_email));
            binding.nannyRegistrationEmailData.setFocusable(true);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.nannyRegistrationEmailData.setError(getString(R.string.invalid_email));
            binding.nannyRegistrationEmailData.setFocusable(true);
        } else if (password.isEmpty()) {
            binding.nannyRegistrationPasswordData.setError(getString(R.string.write_your_password));
            binding.nannyRegistrationPasswordData.setFocusable(true);
        } else if (password.length() < 8 ) {
            binding.nannyRegistrationPasswordData.setError(getString(R.string.password_length));
            binding.nannyRegistrationPasswordData.setFocusable(true);
        } else if (!confirmPassword.equals(password)) {
            binding.nannyRegistrationConfirmPasswordData.setError(getString(R.string.password_not_match));
            binding.nannyRegistrationConfirmPasswordData.setFocusable(true);
        } else if (phone.isEmpty()) {
            binding.nannyRegistrationPhoneData.setError(getString(R.string.write_your_phone));
            binding.nannyRegistrationPhoneData.setFocusable(true);
        } else if (phone.length() != 11 || !phone.startsWith("05")) {
            binding.nannyRegistrationPhoneData.setError(getString(R.string.phone_length));
            binding.nannyRegistrationPhoneData.setFocusable(true);
        } else if (city.isEmpty()) {
            binding.nannyEgistrationCityData.setError(getString(R.string.write_your_city));
            binding.nannyEgistrationCityData.setFocusable(true);
        } else if (street.isEmpty()) {
            binding.nannyRegistrationStreetData.setError(getString(R.string.write_your_street));
            binding.nannyRegistrationStreetData.setFocusable(true);
        } else if (home.isEmpty()) {
            binding.nannyRegistrationHomeNumberData.setError(getString(R.string.write_your_home));
            binding.nannyRegistrationHomeNumberData.setFocusable(true);
        } else {
            final SweetAlertDialog loadingBar = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            loadingBar.setTitleText(getString(R.string.wait));
            loadingBar.setContentText(getString(R.string.signup));
            loadingBar.setCancelable(false);
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            NannyModel nannyModel = new NannyModel("", name, email, city, street, home,"", phone,0);
            storeNannyImage(nannyModel, password, loadingBar);
        }
    }

    private void storeNannyImage(NannyModel model, String password, SweetAlertDialog loadingBar) {

        final String imageRandomKey, saveCurrentDate, saveCurrentTime;

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd,MM,yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        imageRandomKey = saveCurrentDate + saveCurrentTime;
        final StorageReference filePath = parentImagesRef.child(ImageUri.getLastPathSegment() + imageRandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.getLocalizedMessage().toString();
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        downloadImageUrl = task.getResult().toString();
                        model.setImageUrl(downloadImageUrl);
                        signUp(model, password, loadingBar);
                    }
                });
            }
        });
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public void signUp(NannyModel model, String password, SweetAlertDialog loadingBar) {
        firebaseAuth.createUserWithEmailAndPassword(model.getEmail(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    model.setNannyId(getCurrentUser().getUid());
                    reference.child(model.getNannyId()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            loadingBar.dismiss();
                            SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                            dialog.setTitle("تم إنشاء الحساب بنجاح\n طلبكم قيد المعالجة الان بعد تأكيد الطلب يمكنك الاستمتاع بخدماتنا");
                            dialog.setCancelable(false);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setConfirmClickListener(sDialog -> {
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                firebaseAuth.signOut();
                                dialog.dismissWithAnimation();
                            });
                            dialog.show();
                        }
                    });

                } else {
                    loadingBar.dismiss();
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == Activity.RESULT_OK && data != null) {
            ImageUri = data.getData();
            binding.nannyRegistrationImage.setImageURI(ImageUri);
        }

    }

}

