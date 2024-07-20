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
import com.example.go4lunch.activity.adapter.ListWorkmateAdapter;
import com.example.go4lunch.base.BaseFragment;
import com.example.go4lunch.databinding.FragmentWorkmatesBinding;
import com.example.go4lunch.model.Workmate;
import com.example.go4lunch.viewModel.WorkmateViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorkmatesFragment extends BaseFragment<FragmentWorkmatesBinding> implements ListWorkmateAdapter.OnItemClick {

    private ListWorkmateAdapter mAdapter;
    WorkmateViewModel workmateViewModel;

    public static WorkmatesFragment newInstance() {
        return new WorkmatesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workmateViewModel = new ViewModelProvider(requireActivity()).get(WorkmateViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentWorkmatesBinding mBinding = FragmentWorkmatesBinding.inflate(getLayoutInflater());
        Context context = this.requireContext();
        mAdapter = new ListWorkmateAdapter(new ArrayList<>(), true, this);
        mBinding.fragmentWorkmatesList.setLayoutManager(new LinearLayoutManager(context));
        mBinding.fragmentWorkmatesList.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        mBinding.fragmentWorkmatesList.setAdapter(mAdapter);

        workmateViewModel.getWorkmatesList().observe(getViewLifecycleOwner(), this::refreshRecycler);

        return mBinding.getRoot();
    }

    @Override
    public void onClick(String restaurantId) {
        RestaurantDetailsActivity.openDetails(restaurantId, this.requireView());
    }

    private void refreshRecycler(List<Workmate> myList) {
        mAdapter.updateList(myList);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
