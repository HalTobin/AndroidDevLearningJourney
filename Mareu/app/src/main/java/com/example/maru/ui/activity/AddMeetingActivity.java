package com.example.maru.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.maru.R;
import com.example.maru.base.BaseActivity;
import com.example.maru.databinding.ActivityAddMeetingBinding;
import com.example.maru.di.DI;
import com.example.maru.ui.adapter.ListUserAdapter;
import com.example.maru.ui.dialog.PickRoomDialog;
import com.example.maru.ui.dialog.PickUserDialog;
import com.example.maru.event.DeleteUserEvent;
import com.example.maru.model.Meeting;
import com.example.maru.service.MeetingApiService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;

import petrov.kristiyan.colorpicker.ColorPicker;

public class AddMeetingActivity extends BaseActivity<ActivityAddMeetingBinding> implements PickUserDialog.PickUserDialogListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, PickRoomDialog.PickRoomDialogListener {

    private MeetingApiService myApiService;

    private ListUserAdapter myAdapter;

    private Meeting newMeeting;
    private String newMeetingName;
    private int newMeetingColor = 0x000000;
    Calendar newMeetingDate = Calendar.getInstance();
    private int newMeetingDay;
    private int newMeetingMonth;
    private int newMeetingYear;
    private int newMeetingHour;
    private int newMeetingMin;
    private String newMeetingRoom;
    private ArrayList<String> newMeetingUsers = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myApiService = DI.getMeetingApiService();
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        binding.activityAddMeetingImgColor.setOnClickListener(v -> openDialColor());

        binding.activityAddMeetingBtPickDate.setOnClickListener(v -> openDialDate());

        binding.activityAddMeetingBtPickTime.setOnClickListener(v -> openDialTime());

        binding.activityAddMeetingBtAddUser.setOnClickListener(v -> openDialAddUser());

        binding.activityAddMeetingBtPickRoom.setOnClickListener(v -> openDialRoom());

        binding.activityAddMeetingTxtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                newMeetingName = s.toString();
                isFilled();
            }
        });

        binding.activityAddMeetingTxtRoom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                newMeetingRoom = s.toString();
                isFilled();
            }
        });

        binding.activityAddMeetingBtCreateMeeting.setOnClickListener(v -> preCreationMeeting());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home : {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initRecycler() {
        myAdapter = new ListUserAdapter(newMeetingUsers);
        binding.activityAddMeetingListUser.setLayoutManager(new LinearLayoutManager(this));
        binding.activityAddMeetingListUser.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.activityAddMeetingListUser.setAdapter(myAdapter);
        isFilled();
    }

    @Override
    public void onResume() {
        super.onResume();
        initRecycler();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onDeleteUser(DeleteUserEvent event) {
        newMeetingUsers.remove(event.user);
        initRecycler();
    }

    private void openDialColor() {
        final ColorPicker colorPickerDialog = new ColorPicker(this);
        colorPickerDialog.disableDefaultButtons(true);
        colorPickerDialog.addListenerButton(getString(R.string.cancel), (v, position, color) -> colorPickerDialog.dismissDialog());
        colorPickerDialog.addListenerButton(getString(R.string.select), (v, position, color) -> {
            newMeetingColor = color;
            binding.activityAddMeetingImgColor.setColorFilter(newMeetingColor);
            colorPickerDialog.dismissDialog();
            isFilled();
        });
        colorPickerDialog.setTitle(getString(R.string.pick_a_color)).setColumns(5).show();
        isFilled();
    }

    private void openDialDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void openDialTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                this,
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                true);
        timePickerDialog.show();
    }

    private void openDialRoom() {
        PickRoomDialog pickRoomDialog = new PickRoomDialog();
        pickRoomDialog.show(getSupportFragmentManager(), "Pick Room Dialog");
    }

    private void openDialAddUser() {
        PickUserDialog pickUserDialog = new PickUserDialog();
        pickUserDialog.show(getSupportFragmentManager(), "Pick User Dialog");
    }

    private void openAlert() {
        AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(this);
        myAlertBuilder.setTitle(getString(R.string.room_not_available_title));
        myAlertBuilder.setMessage(getString(R.string.room_not_available_content));
        myAlertBuilder.setPositiveButton(getString(R.string.select), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createMeeting();
                dialog.dismiss();
            }
        });
        myAlertBuilder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog myAlert = myAlertBuilder.create();
        myAlert.show();
    }

    public static void navigate(FragmentActivity activity) {
        Intent intent = new Intent(activity, AddMeetingActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        binding.activityAddMeetingTxtDate.setText(String.format("%02d", dayOfMonth) + "/" + String.format("%02d", month+1) + "/" + String.format("%04d", year));
        newMeetingDay = dayOfMonth;
        newMeetingMonth = month;
        newMeetingYear = year;
        openDialTime();
        isFilled();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        binding.activityAddMeetingTxtTime.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
        newMeetingHour = hourOfDay;
        newMeetingMin = minute;
        isFilled();
    }

    @Override
    public void sendUser(String user) {
        if(!newMeetingUsers.contains(user)) newMeetingUsers.add(user);
        initRecycler();
    }

    @Override
    public void sendRoom(String room) {
        binding.activityAddMeetingTxtRoom.setText(room);
    }

    private boolean isFilled() {
        boolean state = true;

        if(binding.activityAddMeetingTxtName.getText().toString().isEmpty()) state = false;
        else if(newMeetingColor==0x000000) state = false;
        else if(binding.activityAddMeetingTxtDate.getText().toString().equals(getString(R.string.hint_date))) state = false;
        else if(binding.activityAddMeetingTxtTime.getText().toString().equals(getString(R.string.hint_hour))) state = false;
        else if(binding.activityAddMeetingTxtRoom.getText().toString().equals(getString(R.string.hint_room))) state = false;
        else if(newMeetingUsers.isEmpty()) state = false;

        if(state) binding.activityAddMeetingBtCreateMeeting.setBackground(getDrawable(R.drawable.rounded_shape_on));
        else binding.activityAddMeetingBtCreateMeeting.setBackground(getDrawable(R.drawable.rounded_shape));

        return state;
    }

    private void preCreationMeeting() {
        if(isFilled()) {
            newMeetingDate.set(newMeetingYear, newMeetingMonth, newMeetingDay, newMeetingHour, newMeetingMin);
            System.out.println("AVAILABLE : " + myApiService.isRoomAvailable(newMeetingRoom, newMeetingDate));
            if(myApiService.isRoomAvailable(newMeetingRoom, newMeetingDate)) createMeeting();
            else openAlert();
        }
    }

    private void createMeeting() {
        newMeeting = new Meeting(myApiService.getNextId(), newMeetingName, newMeetingRoom, newMeetingDate.getTimeInMillis(), newMeetingColor, newMeetingUsers);
        myApiService.createMeeting(newMeeting);
        finish();
    }
}
