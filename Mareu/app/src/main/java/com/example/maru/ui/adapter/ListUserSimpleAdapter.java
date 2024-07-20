package com.example.maru.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.maru.databinding.ItemListUserSimpleBinding;
import com.example.maru.event.GetUserEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class ListUserSimpleAdapter extends RecyclerView.Adapter<ListUserSimpleAdapter.ViewHolder> {

    private final List<String> mUsers;

    public ListUserSimpleAdapter(List<String> items) {
        mUsers = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemListUserSimpleBinding view = ItemListUserSimpleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String user = mUsers.get(position);
        holder.binding.itemListUserSimpleTxtName.setText(user);
        holder.itemView.setOnClickListener(v -> EventBus.getDefault().post(new GetUserEvent(user)));
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemListUserSimpleBinding binding;

        public ViewHolder(ItemListUserSimpleBinding view) {
            super(view.getRoot());
            binding = view;
        }
    }

}
