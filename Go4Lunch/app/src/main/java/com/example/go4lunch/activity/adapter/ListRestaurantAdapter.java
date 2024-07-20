package com.example.go4lunch.activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.activity.RestaurantDetailsActivity;
import com.example.go4lunch.databinding.ItemListRestaurantBinding;
import com.example.go4lunch.model.Restaurant;

import java.util.List;

public class ListRestaurantAdapter extends RecyclerView.Adapter<ListRestaurantAdapter.ViewHolder> {

    private final Context context;

    private List<Restaurant> mRestaurants;

    private final OnItemClick mCallback;

    public ListRestaurantAdapter(List<Restaurant> items, Context context, OnItemClick listener) {
        mRestaurants = items;
        this.context = context;
        this.mCallback = listener;
    }

    public interface OnItemClick {
        void onClick(String value);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemListRestaurantBinding view = ItemListRestaurantBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Restaurant restaurant = mRestaurants.get(position);

        // Display the name of the restaurant
        holder.binding.itemRestaurantName.setText(restaurant.getName());

        // String typeAndAddress = getFoodTypeInString(restaurant.getType()) + " - " + restaurant.getAddress();
        // Display the address of the restaurant
        holder.binding.itemRestaurantTypeAndAdress.setText(restaurant.getAddress());

        // Display the closure's hour of the restaurant
        String openUntil;
        if(restaurant.isOpen()) {
            if(restaurant.getNextClosure().equals("UNKNOWN")) openUntil = context.getResources().getString(R.string.open_unknown);
            else openUntil = context.getResources().getString(R.string.open_until) + " " + restaurant.getNextClosure().substring(0, 2) + ":" + restaurant.getNextClosure().substring(2, 4);
        } else openUntil = context.getResources().getString(R.string.closed);
        holder.binding.itemRestaurantClosure.setText(openUntil);

        // Display the rating of the restaurant
        holder.binding.itemRestaurantRate.setRating(restaurant.getStars());

        // Display the distance between the user and the restaurant
        if(restaurant.getDistance() < 1000) holder.binding.itemRestaurantDistance.setText(restaurant.getDistance() + "m");
        else holder.binding.itemRestaurantDistance.setText((int)(restaurant.getDistance()/1000) + "km");

        String toLoad = restaurant.getImageURL() + "&maxwidth=200";
        holder.binding.itemRestaurantImage.setImageBitmap(Restaurant.loadImageFromWeb(toLoad));

        // Display the number of people who eats at the restaurant
        if(restaurant.getNumberOfWorkmate() == 0) {
            holder.binding.itemRestaurantNbPeople.setText("");
            holder.binding.itemRestaurantPeople.setVisibility(View.INVISIBLE);
        } else {
            holder.binding.itemRestaurantNbPeople.setText("(" + restaurant.getNumberOfWorkmate() + ")");
            holder.binding.itemRestaurantPeople.setVisibility(View.VISIBLE);
        }

        // Set up the onClickListener to open the RestaurantDetailsActivity
        holder.itemView.setOnClickListener(v -> mCallback.onClick(restaurant.getId()));
    }

    @Override
    public int getItemCount() {
        if(mRestaurants!=null) return mRestaurants.size();
        else return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(@NonNull final List<Restaurant> restaurants) {
        this.mRestaurants = restaurants;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemListRestaurantBinding binding;

        public ViewHolder(ItemListRestaurantBinding view) {
            super(view.getRoot());
            binding = view;
        }

    }

}
