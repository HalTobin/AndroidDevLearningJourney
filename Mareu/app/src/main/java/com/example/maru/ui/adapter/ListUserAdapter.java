package com.example.maru.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.maru.databinding.ItemListUserBinding;
import com.example.maru.event.DeleteUserEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class ListUserAdapter extends RecyclerView.Adapter<ListUserAdapter.ViewHolder> {

    private final List<String> mUsers;

    public ListUserAdapter(List<String> items) {
        mUsers = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemListUserBinding view = ItemListUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String user = mUsers.get(position);

        holder.binding.itemListUserTxtName.setText(user);
        holder.binding.itemListUserBtDelete.setOnClickListener(v -> EventBus.getDefault().post(new DeleteUserEvent(user)));

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemListUserBinding binding;

        public ViewHolder(ItemListUserBinding view) {
            super(view.getRoot());
            binding = view;
        }

    }

}
