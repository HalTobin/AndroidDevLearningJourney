package com.example.go4lunch.activity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.go4lunch.activity.RestaurantDetailsActivity;
import com.example.go4lunch.activity.adapter.ListRestaurantAdapter;
import com.example.go4lunch.base.BaseFragment;
import com.example.go4lunch.databinding.FragmentListBinding;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.viewModel.MainHubViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends BaseFragment<FragmentListBinding> implements ListRestaurantAdapter.OnItemClick {

    private FragmentListBinding mBinding;
    private ListRestaurantAdapter mAdapter;

    private MainHubViewModel restaurantViewModel;

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restaurantViewModel = new ViewModelProvider(requireActivity()).get(MainHubViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentListBinding.inflate(getLayoutInflater());
        Context context = this.requireContext();
        mAdapter = new ListRestaurantAdapter(new ArrayList<>(), context, this);
        mBinding.fragmentRestaurantList.setLayoutManager(new LinearLayoutManager(context));
        mBinding.fragmentRestaurantList.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        mBinding.fragmentRestaurantList.setAdapter(mAdapter);

        // Observe the list of restaurant received with RestaurantViewModel and display it
        restaurantViewModel.getRestaurantsAndWorkmates().observe(getViewLifecycleOwner(), myRestaurants -> {
            initRecycler();
            refreshRecycler(myRestaurants);
        });

        return mBinding.getRoot();
    }

    private void initRecycler() {
        mBinding.fragmentRestaurantList.setAdapter(mAdapter);
    }

    private void refreshRecycler(List<Restaurant> myList) { mAdapter.updateList(myList); }

    @Override
    public void onResume() {
        super.onResume();
        initRecycler();
    }

    @Override
    public void onClick(String value) {
        RestaurantDetailsActivity.openDetails(value, this.requireView());
    }
}
