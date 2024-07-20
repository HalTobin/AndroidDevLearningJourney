package com.example.maru.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.maru.databinding.ItemListRoomSimpleBinding;
import com.example.maru.event.GetRoomEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class ListRoomSimpleAdapter extends RecyclerView.Adapter<ListRoomSimpleAdapter.ViewHolder> {

    private final List<String> mRooms;

    public ListRoomSimpleAdapter(List<String> items) {
        mRooms = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemListRoomSimpleBinding view = ItemListRoomSimpleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String room = mRooms.get(position);
        holder.binding.itemListRoomSimpleTxtName.setText(room);
        holder.itemView.setOnClickListener(v -> EventBus.getDefault().post(new GetRoomEvent(room)));
    }

    @Override
    public int getItemCount() {
        return mRooms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemListRoomSimpleBinding binding;

        public ViewHolder(ItemListRoomSimpleBinding view) {
            super(view.getRoot());
            binding = view;
        }

    }

}
