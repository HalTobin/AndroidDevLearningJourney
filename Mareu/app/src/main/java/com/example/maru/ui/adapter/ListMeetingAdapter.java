package com.example.maru.ui.adapter;

import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.maru.databinding.ItemListMeetingBinding;
import com.example.maru.event.DeleteMeetingEvent;
import com.example.maru.model.Meeting;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class ListMeetingAdapter extends RecyclerView.Adapter<ListMeetingAdapter.ViewHolder> {

    private final List<Meeting> mMeetings;

    public ListMeetingAdapter(List<Meeting> items) {
        mMeetings = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemListMeetingBinding view = ItemListMeetingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Meeting meeting = mMeetings.get(position);
        holder.binding.itemListMeetingTxtName.setText(meeting.getTitleString());
        holder.binding.itemListMeetingImg.setColorFilter(meeting.getColorInt(), PorterDuff.Mode.SRC_ATOP);
        holder.binding.itemListDeleteButton.setOnClickListener(v -> EventBus.getDefault().post(new DeleteMeetingEvent(meeting)));
        holder.binding.itemListMeetingTxtUsers.setText(meeting.getUsersString());
    }

    @Override
    public int getItemCount() {
        return mMeetings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemListMeetingBinding binding;

        public ViewHolder(ItemListMeetingBinding view) {
            super(view.getRoot());
            binding = view;
        }

    }

}
