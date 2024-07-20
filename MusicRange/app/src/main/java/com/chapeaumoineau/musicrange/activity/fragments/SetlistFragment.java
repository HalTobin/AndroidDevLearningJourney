package com.chapeaumoineau.musicrange.activity.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chapeaumoineau.musicrange.R;
import com.chapeaumoineau.musicrange.activity.adapter.SetlistRecyclerViewAdapter;
import com.chapeaumoineau.musicrange.activity.dialog.SavePlaylistDialog;
import com.chapeaumoineau.musicrange.event.InstructionForSetlist;
import com.chapeaumoineau.musicrange.injections.DI;
import com.chapeaumoineau.musicrange.services.player.MasterPlayer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetlistFragment extends Fragment {

    @BindView(R.id.fragment_setlist_position_txt)
    TextView setlistPosition;
    @BindView(R.id.fragment_setlist_save_setlist_bt)
    ImageView saveButton;

    private RecyclerView myRecyclerView;

    private MasterPlayer myPlayer;

    public SetlistFragment() {}

    public static SetlistFragment newInstance() {
        SetlistFragment fragment = new SetlistFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myPlayer = DI.getPlayerApi();
        myPlayer.setSetlistListener(new MasterPlayer.MySetlistListener() {
            @Override
            public void onSongSetted() throws IOException {
                refreshSetlist();
            }

            @Override
            public void onSetlistSetted() {
                refreshSetlist();
            }
        });
    }

    @OnClick(R.id.fragment_setlist_save_setlist_bt)
    void saveButtonClicked(View view) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        SavePlaylistDialog savePlaylistDialog = new SavePlaylistDialog(myPlayer.getCurrentSetlist());
        savePlaylistDialog.show(activity.getSupportFragmentManager(), "Save Playlist");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_setlist, container, false);
        Context context = view.getContext();
        myRecyclerView = view.findViewById(R.id.fragment_setlist_recycler);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        myRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(myRecyclerView);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshSetlist();
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
    public void getTitle(InstructionForSetlist event) throws IOException {
        switch(event.mode) {
            case InstructionForSetlist.MOVETONEXT:
                myPlayer.moveToNext(event.position);
                break;
            case InstructionForSetlist.MOVETOEND:
                myPlayer.moveToEnd(event.position);
                break;
            case InstructionForSetlist.REMOVE:
                myPlayer.removeFromQueu(event.position);
                break;
            case InstructionForSetlist.FORCEPLAY:
                myPlayer.goTo(event.position);
                break;
        }
    }

    private void refreshSetlist() {
        if(myPlayer.isDefined()) {
            setlistPosition.setText(((myPlayer.getSetlistPosition() + 1) + " / " + myPlayer.getSetlistSize()));
            myRecyclerView.setAdapter(new SetlistRecyclerViewAdapter(myPlayer.getCurrentSetlist(), myPlayer.getSetlistPosition()));
        }
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            myPlayer.swapSetlist(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            recyclerView.getAdapter().notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

}
