package com.example.maru.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maru.R;
import com.example.maru.di.DI;
import com.example.maru.event.GetUserEvent;
import com.example.maru.service.MeetingApiService;
import com.example.maru.ui.adapter.ListUserSimpleAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class PickUserDialog extends AppCompatDialogFragment {

    private EditText editTextUser;
    private PickUserDialogListener listener;

    private ListUserSimpleAdapter myAdapter;
    private RecyclerView myRecycler;

    private MeetingApiService myApiService;
    private List<String> myUsers;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        myApiService = DI.getMeetingApiService();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_pick_user, null);

        builder.setView(view)
                .setTitle("Sélectionner un participant")
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(getString(R.string.select), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String user = editTextUser.getText().toString();
                        listener.sendUser(user);
                    }
                });

        editTextUser = view.findViewById(R.id.dialog_pick_user_txtEdit);
        myRecycler = view.findViewById(R.id.dialog_picker_user_list_users);

        return builder.create();
    }

    private void initRecycler() {
        myUsers = myApiService.getUsers();

        myAdapter = new ListUserSimpleAdapter(myUsers);

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
    public void onGetUserEvent(GetUserEvent event) {
        editTextUser.setText(event.user);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (PickUserDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement PickUserDialog");
        }
    }

    public interface PickUserDialogListener {
        void sendUser(String user);
    }
}
