package com.example.nannyap.parent;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nannyap.CommonUsed;
import com.example.nannyap.EventBus.PassMassageActionClick;
import com.example.nannyap.R;
import com.example.nannyap.databinding.FragmentBookingBinding;
import com.example.nannyap.model.BookingModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class BookingFragment extends Fragment {

    private FragmentBookingBinding binding;
    private final Calendar myCalendar = Calendar.getInstance();
    private SimpleDateFormat dateFormatter, timeFormatter;
    String bookingType, specialNeeds;

    public BookingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        timeFormatter = new SimpleDateFormat("HH:mm a", Locale.US);

        ArrayList<String> array = new ArrayList<>();
        array.add("نعم");
        array.add("لا");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.specialNeedsData.setAdapter(adapter);
        binding.specialNeedsData.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                specialNeeds = adapterView.getItemAtPosition(i).toString();
                //Toast.makeText(getContext(), "تم اختيار: " + specialNeeds, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("يومي");
        arrayList.add("اسبوعي");
        arrayList.add("شهري");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.bookingTypeData.setAdapter(arrayAdapter);
        binding.bookingTypeData.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bookingType = adapterView.getItemAtPosition(i).toString();
               // Toast.makeText(getContext(), "تم اختيار: " + bookingType, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });


        binding.parentNannyStartDateData.setOnClickListener(v -> getDate(binding.parentNannyStartDateData));
        binding.parentNannyEndDateData.setOnClickListener(v -> getDate(binding.parentNannyEndDateData));

        binding.parentNannyStartTimeData.setOnClickListener(view1 -> getTime(binding.parentNannyStartTimeData));
        binding.parentNannyEndTimeData.setOnClickListener(view1 -> getTime(binding.parentNannyEndTimeData));

        binding.bookingBtn.setOnClickListener(view1 -> {
            saveBooking();
        });
    }

    private void saveBooking() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(CommonUsed.BooknigRequests);
        String bookingId, parentId, startDate, endDate, startTime, endTime, age, type, note;

        bookingId = reference.push().getKey();
        parentId = FirebaseAuth.getInstance().getUid();
        startDate = binding.parentNannyStartDateData.getText().toString();
        endDate = binding.parentNannyEndDateData.getText().toString();
        startTime = binding.parentNannyStartTimeData.getText().toString();
        endTime = binding.parentNannyEndTimeData.getText().toString();
        age = binding.parentNannyChildAgeData.getText().toString();
        note = binding.parentNannyNoteData.getText().toString();

        SimpleDateFormat dfDate = new SimpleDateFormat("dd-MM-yyyy");
        String dayDate = new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(Calendar.getInstance().getTime());

        try {
            if (startDate.isEmpty()) {
                binding.parentNannyStartDateData.setError(getString(R.string.write_start_date));
                binding.parentNannyStartDateData.setFocusable(false);

            } else if (dfDate.parse(startDate).before(dfDate.parse(dayDate))) {
                binding.parentNannyStartDateData.setError(getString(R.string.wrong_start_date));
                binding.parentNannyStartDateData.setFocusable(false);

            } else if (endDate.isEmpty()) {
                binding.parentNannyEndDateData.setError(getString(R.string.write_end_date));
                binding.parentNannyEndDateData.setFocusable(false);
            } else if (startTime.isEmpty()) {
                binding.parentNannyStartTimeData.setError(getString(R.string.write_start_time));
                binding.parentNannyStartTimeData.setFocusable(false);
            } else if (endTime.isEmpty()) {
                binding.parentNannyEndTimeData.setError(getString(R.string.write_end_time));
                binding.parentNannyEndTimeData.setFocusable(false);
            } else if (endTime.isEmpty()) {
                binding.parentNannyEndTimeData.setError(getString(R.string.write_end_time));
                binding.parentNannyEndTimeData.setFocusable(false);
            } else if (age.isEmpty()) {
                binding.parentNannyChildAgeData.setError(getString(R.string.write_your_child_age));
                binding.parentNannyChildAgeData.setFocusable(false);
            } else if (note.isEmpty()) {
                binding.parentNannyNoteData.setError(getString(R.string.write_note));
                binding.parentNannyNoteData.setFocusable(false);
            } else {
                final SweetAlertDialog loadingBar = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                loadingBar.setTitleText(getString(R.string.wait));
                loadingBar.setContentText(getString(R.string.update));
                loadingBar.setCancelable(false);
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                BookingModel bookingModel = new BookingModel(bookingId, CommonUsed.NannyID, parentId, startDate, endDate, startTime, endTime, age, specialNeeds, bookingType, note, getString(R.string.status));
                reference.child(bookingId).setValue(bookingModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loadingBar.dismiss();
                        SweetAlertDialog alertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                        alertDialog.setTitle("تم ارسال الحجز بنجاح");
                        alertDialog.setCancelable(false);
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setConfirmButton(getContext().getString(R.string.ok), ssDialog -> {
                            EventBus.getDefault().postSticky(new PassMassageActionClick("openNanniesFragment"));
                            ssDialog.dismiss();
                        });
                        alertDialog.show();
                    }
                });
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    private void getDate(TextView dateTv) {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog package_start = new DatePickerDialog(getContext(), (DatePickerDialog.OnDateSetListener) (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            dateTv.setText(dateFormatter.format(newDate.getTime()));
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        package_start.show();
    }


    private void getTime(TextView timeTv) {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mCurrentTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCurrentTime.set(Calendar.MINUTE, minute);
                timeTv.setText(timeFormatter.format(mCurrentTime.getTime()));
            }
        }, hour, minute, true);
        mTimePicker.show();
    }
}