package com.example.go4lunch.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.go4lunch.R;
import com.example.go4lunch.activity.adapter.ListWorkmateAdapter;
import com.example.go4lunch.base.BaseActivity;
import com.example.go4lunch.databinding.ActivityRestaurantDetailsBinding;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.Workmate;
import com.example.go4lunch.viewModel.RestaurantDetailsViewModel;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDetailsActivity extends BaseActivity<ActivityRestaurantDetailsBinding> implements ListWorkmateAdapter.OnItemClick {

    private ActivityRestaurantDetailsBinding mBinding;
    private ListWorkmateAdapter mAdapter;

    private RestaurantDetailsViewModel restaurantDetailsViewModel;

    String restaurantId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityRestaurantDetailsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        restaurantDetailsViewModel = new ViewModelProvider(this).get(RestaurantDetailsViewModel.class);

        // Retrieve the id of the selected restaurant
        restaurantId = (String) getIntent().getSerializableExtra("RestaurantId");

        // Set up an observer to retrieve the data of the selected restaurant
        restaurantDetailsViewModel.getRestaurantDetails(restaurantId).observe(this, myRestaurant -> {
            mBinding.restaurantDetailImage.setImageBitmap(Restaurant.loadImageFromWeb(myRestaurant.getImageURL() + "&maxwidth=1440"));
            mBinding.restaurantDetailName.setText(myRestaurant.getName());
            mBinding.restaurantDetailTypeAndAdress.setText(myRestaurant.getAddress());
            // If phone number isn't available, the button is shaded
            // Else a setOnClickListener is set up
            if(isNotAvailable(myRestaurant.getPhoneNumber())) setTextViewOff(mBinding.restaurantDetailCall);
            else mBinding.restaurantDetailCall.setOnClickListener(v -> {
                // Open the phone app, with the number's field already filled in
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + myRestaurant.getPhoneNumber()));
                startActivity(intent);
            });
            // If website url isn't available, the button is shaded
            // Else a setOnClickListener is set up
            if(isNotAvailable(myRestaurant.getWebURL())) setTextViewOff(mBinding.restaurantDetailWebsite);
            else mBinding.restaurantDetailWebsite.setOnClickListener(v -> {
                // Open the browser with the url of the restaurant's website
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myRestaurant.getWebURL()));
                startActivity(browserIntent);
            });
            mBinding.restaurantDetailEatHere.setOnClickListener(v -> {
                // Notify the restaurantDetailsViewModel that the button has been clicked
                refreshRecycler(new ArrayList<>());
                restaurantDetailsViewModel.invertEatHere(myRestaurant.getName(), myRestaurant.getId());
            });
            mBinding.restaurantDetailLike.setOnClickListener(v -> {
                // Notify the restaurantDetailsViewModel that the button has been clicked
                restaurantDetailsViewModel.invertFavorite(restaurantId);
            });
        });

        // Get the list of workmate who eat at the selected restaurant
        restaurantDetailsViewModel.getWorkmatesFromRestaurant(restaurantId).observe(this, this::refreshRecycler);

        // Get data of the current user
        restaurantDetailsViewModel.getCurrentWorkmate().observe(this, currentWorkmate -> {
            // Change the color of the "eat_here" button depending on if the user working at the selected restaurant or not
            assert currentWorkmate.getRestaurantUrl() != null;
            if(currentWorkmate.getRestaurantUrl().equals(restaurantId)) {
                mBinding.restaurantDetailEatHere.setImageResource(R.drawable.ic_check_circle);
                mBinding.restaurantDetailEatHere.setColorFilter(ContextCompat.getColor(this, R.color.quantum_googgreenA200));
            }
            else {
                mBinding.restaurantDetailEatHere.setImageResource(R.drawable.ic_menu_silverware);
                mBinding.restaurantDetailEatHere.setColorFilter(ContextCompat.getColor(this, R.color.orange_500));

            }
            setTextDrawableColor(mBinding.restaurantDetailName, R.color.favorite_off);
            if(currentWorkmate.getFavoritesRestaurantsUrl() != null) {
                if (currentWorkmate.getFavoritesRestaurantsUrl().contains(restaurantId)) setTextDrawableColor(mBinding.restaurantDetailName, R.color.favorite_on);
            }
        });

        mAdapter = new ListWorkmateAdapter(new ArrayList<>(), false, this);
        mBinding.restaurantDetailWorkmates.setLayoutManager(new LinearLayoutManager(this));
        mBinding.restaurantDetailWorkmates.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mBinding.restaurantDetailWorkmates.setAdapter(mAdapter);
    }

    // Change a text and its drawable color to gray
    private void setTextViewOff(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.off));
        setTextDrawableColor(textView, R.color.off);
    }

    // Change the color of the drawable attached to a textview
    private void setTextDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(textView.getContext(), color), PorterDuff.Mode.SRC_IN));
            }
        }
    }

    // Test if a String correspond to "UNKNOWN" or if it's empty
    private boolean isNotAvailable(String toTest) {
        return toTest.equals("UNKNOWN")||toTest.equals("");
    }

    // Refresh the list of workmate's who eats at this place
    private void refreshRecycler(List<Workmate> myList) { mAdapter.updateList(myList); }

    // Open the current activity
    public static void openDetails(String restaurantId, View v) {
        Intent intent = new Intent(v.getContext(), RestaurantDetailsActivity.class);
        intent.putExtra("RestaurantId", restaurantId);
        ActivityCompat.startActivity(v.getContext(), intent, null);
    }

    @Override
    public void onClick(String value) {

    }
}
