package com.example.maru.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maru.R;
import com.example.maru.di.DI;
import com.example.maru.event.GetRoomEvent;
import com.example.maru.service.MeetingApiService;
import com.example.maru.ui.adapter.ListRoomSimpleAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class PickRoomDialog extends AppCompatDialogFragment {

    private String selectRoom;
    private PickRoomDialogListener listener;

    private ListRoomSimpleAdapter myAdapter;
    private RecyclerView myRecycler;

    private MeetingApiService myApiService;
    private List<String> myRooms;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        myApiService = DI.getMeetingApiService();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_pick_room, null);

        builder.setView(view).setTitle("Sélectionner une salle");
        myRecycler = view.findViewById(R.id.dialog_picker_room_list);

        return builder.create();
    }

    private void initRecycler() {
        myRooms = myApiService.getRooms();

        myAdapter = new ListRoomSimpleAdapter(myRooms);

        myRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        myRecycler.setAdapter(myAdapter);
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
    public void onGetRoomEvent(GetRoomEvent event) {
        listener.sendRoom(event.room);
        this.dismiss();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (PickRoomDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement PickRoomDialog");
        }
    }

    public interface PickRoomDialogListener {
        void sendRoom(String room);
    }
}
