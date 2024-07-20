package com.example.go4lunch.activity.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.example.go4lunch.base.BaseFragment;
import com.example.go4lunch.databinding.FragmentMapBinding;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.viewModel.MainHubViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class MapFragment extends BaseFragment<FragmentMapBinding> implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Context context;
    private FragmentMapBinding mBinding;
    boolean isPermissionGranted = false;

    private GoogleMap myGoogleMap;
    private final List<Marker> markers = new ArrayList<>();

    private MainHubViewModel restaurantViewModel;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = this.getContext();

        restaurantViewModel = new ViewModelProvider(requireActivity()).get(MainHubViewModel.class);
        checkPermission();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentMapBinding.inflate(getLayoutInflater());
        initMapView(savedInstanceState);

        this.restaurantViewModel.getIsStartedUp().observe(getViewLifecycleOwner(), isStartedUp -> {
            if(isStartedUp.equals("NOT_STARTED")) {
                this.restaurantViewModel.searchUserLocation(this.getContext());
                this.restaurantViewModel.setStartedUp("STARTED");
            }
        });

        this.restaurantViewModel.getRestaurantsAndWorkmates().observe(getViewLifecycleOwner(), this::refreshMarkers);

        return mBinding.getRoot();
    }

    private void initMapView(Bundle savedInstanceState) {
        mBinding.fragmentMapMapview.getMapAsync(this);
        mBinding.fragmentMapMapview.onCreate(savedInstanceState);
        mBinding.fragmentMapBtGeo.setOnClickListener(v -> {
            if (isPermissionGranted) restaurantViewModel.searchUserLocation(this.getContext());
        });
    }

    // Move the map's camera to a location
    private void goToLocation(double latitude, double longitude) {
        LatLng myLatLng = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(myLatLng, 17);
        myGoogleMap.moveCamera(cameraUpdate);
        myGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    // Display a marker for each restaurant
    private void refreshMarkers(List<Restaurant> restaurants) {
        // Delete old markers
        for(Marker marker : markers) {
            marker.remove();
        }
        if(myGoogleMap!=null && restaurants!=null) {
            // Create new markers
            for(Restaurant restaurant : restaurants) {
                // Use a different icon if at least one workmate is eating at the restaurant
                int iconResource = R.drawable.ic_restaurant_marker_yellow;
                if(restaurant.getNumberOfWorkmate() > 0) iconResource = R.drawable.ic_restaurant_marker_green;

                markers.add(myGoogleMap.addMarker(new MarkerOptions().
                        position(new LatLng(restaurant.getLatitude(), restaurant.getLongitude()))
                        .title(restaurant.getName())
                        .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(this.getContext(), iconResource)))));
            }
        }
    }

    // Check if the user have allow the app to access his location
    public void checkPermission() {
        Dexter.withContext(context).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                Log.i("Permission", "granted");
                isPermissionGranted = true;
                restaurantViewModel.searchUserLocation(context);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                isPermissionGranted = false;
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.i("Map", "Ready");
        myGoogleMap = googleMap;

        // Depending on the phone's settings, dark mode is used for the map
        MapStyleOptions myMapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_day);
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if(nightModeFlags==Configuration.UI_MODE_NIGHT_YES) myMapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_night);
        googleMap.setMapStyle(myMapStyleOptions);

        // Get and move the map to the location of the RestaurantViewModel
        this.restaurantViewModel.getFocusedLocation().observe(getViewLifecycleOwner(), myFocusedLocation -> {
            if(myGoogleMap!=null) {
                goToLocation(myFocusedLocation.getX(), myFocusedLocation.getY());
            }
        });

        // Refresh the location used by the RestaurantViewModel when camera is idle
        myGoogleMap.setOnCameraIdleListener(() -> restaurantViewModel.setLocation(myGoogleMap.getCameraPosition().target.latitude, myGoogleMap.getCameraPosition().target.longitude));
    }

    // Get a bitmap from a int that correspond to a drawable
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);

        assert drawable != null;
        drawable = (DrawableCompat.wrap(drawable)).mutate();

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBinding.fragmentMapMapview.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.fragmentMapMapview.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBinding.fragmentMapMapview.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBinding.fragmentMapMapview.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinding.fragmentMapMapview.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        mBinding.fragmentMapMapview.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mBinding.fragmentMapMapview.onLowMemory();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
