package com.example.maru.ui.activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import com.example.maru.R;
import com.example.maru.base.BaseActivity;
import com.example.maru.di.DI;
import com.example.maru.databinding.ActivityListMeetingBinding;
import com.example.maru.event.DeleteMeetingEvent;
import com.example.maru.model.Meeting;
import com.example.maru.service.MeetingApiService;
import com.example.maru.ui.adapter.ListMeetingAdapter;
import com.example.maru.ui.dialog.PickRoomDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;
import java.util.List;

public class ListMeetingActivity extends BaseActivity<ActivityListMeetingBinding> implements DatePickerDialog.OnDateSetListener, PickRoomDialog.PickRoomDialogListener {

    private ListMeetingAdapter myAdapter;

    private MeetingApiService myApiService;
    private List<Meeting> myMeetings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApiService = DI.getMeetingApiService();

        binding.addMeeting.setOnClickListener(v -> addMeeting());
        setSupportActionBar(binding.toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.filter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.list_meetings_filter_date:
                openDialDate();
                return true;
            case R.id.list_meetings_filter_room:
                openDialRoom();
                return true;
            case R.id.list_meetings_filter_reset:
                myMeetings = myApiService.getMeetings();
                initRecycler();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        myMeetings = myApiService.getMeetings();
        initRecycler();
    }

    private void initRecycler() {
        myAdapter = new ListMeetingAdapter(myMeetings);
        binding.listMeetings.setLayoutManager(new LinearLayoutManager(this));
        binding.listMeetings.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.listMeetings.setAdapter(myAdapter);
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

    /**
     * Fired if the user clicks on a delete button
     * @param event
     */
    @Subscribe
    public void onDeleteMeeting(DeleteMeetingEvent event) {
        myApiService.deleteMeeting(event.meeting);
        initRecycler();
    }

    private void openDialDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE));
        datePickerDialog.show();
    }

    private void openDialRoom() {
        PickRoomDialog pickRoomDialog = new PickRoomDialog();
        pickRoomDialog.show(getSupportFragmentManager(), "Pick Room Dialog");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        System.out.println("DATE PICKER DIALOG : " + dayOfMonth+"/"+month+"/"+year);
        Calendar forFilter = Calendar.getInstance();
        forFilter.set(year, month, dayOfMonth);
        myMeetings = myApiService.getMeetingsByDate(forFilter);
        initRecycler();
    }

    @Override
    public void sendRoom(String room) {
        System.out.println("GET BY ROOM");
        myMeetings = myApiService.getMeetingsByRoom(room);
        initRecycler();
    }

    void addMeeting() {
        AddMeetingActivity.navigate(this);
    }
}