package com.example.go4lunch.activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ItemListWorkmateBinding;
import com.example.go4lunch.model.Workmate;

import java.util.List;

public class ListWorkmateAdapter extends RecyclerView.Adapter<ListWorkmateAdapter.ViewHolder> {

    private List<Workmate> mWorkmates;
    private final boolean isForWorkmateFragment;
    private Context context;
    private final OnItemClick mCallback;

    public ListWorkmateAdapter(List<Workmate> items, boolean isForWorkmateFragment, OnItemClick listener) {
        mWorkmates = items;
        this.isForWorkmateFragment = isForWorkmateFragment;
        this.mCallback = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemListWorkmateBinding view = ItemListWorkmateBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        this.context = view.getRoot().getContext();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Workmate workmate = mWorkmates.get(position);
        String postNameText;

        // The behavior change depending on whether the adapter is load from WorkmatesFragment or RestaurantDetailsActivity
        if(isForWorkmateFragment) {
            //if(workmate.getRestaurantUrl() != null)
            if(workmate.getRestaurantUrl().isEmpty()) {
                postNameText = context.getResources().getString(R.string.workmate_not_decided);
                holder.binding.itemWorkmateText.setTypeface(null, Typeface.ITALIC);
            } else {
                postNameText = context.getResources().getString(R.string.workmate_eating) + " " + workmate.getRestaurantName();
                holder.binding.itemWorkmateText.setTypeface(null, Typeface.NORMAL);
            }
        }
        else {
            postNameText = context.getResources().getString(R.string.workmate_joining);
            holder.binding.itemWorkmateText.setTypeface(null, Typeface.NORMAL);
        }
        holder.binding.itemWorkmateText.setText(workmate.getFirstName() + " " + postNameText);

        // Load the workmate's avatar
        Glide.with(holder.itemView.getContext())
                .load(workmate.getAvatarUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.binding.itemWorkmateUserAvatar);

        // If the workmate is eating at a restaurant, than a shortcut to RestaurantDetailsActivity is created
        assert workmate.getRestaurantUrl() != null;
        if(!workmate.getRestaurantUrl().equals("")) {
            holder.itemView.setOnClickListener(v -> {
                mCallback.onClick(workmate.getRestaurantUrl());
            });
        }
    }

    @Override
    public int getItemCount() {
        return mWorkmates.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(@NonNull final List<Workmate> workmates) {
        this.mWorkmates = workmates;
        notifyDataSetChanged();
    }

    public interface OnItemClick {
        void onClick(String value);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemListWorkmateBinding binding;

        public ViewHolder(ItemListWorkmateBinding view) {
            super(view.getRoot());
            binding = view;
        }

    }

}
