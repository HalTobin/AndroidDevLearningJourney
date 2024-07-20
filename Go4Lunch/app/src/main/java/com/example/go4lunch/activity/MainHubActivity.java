package com.example.go4lunch.activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.activity.fragment.ListFragment;
import com.example.go4lunch.activity.fragment.MapFragment;
import com.example.go4lunch.activity.fragment.WorkmatesFragment;
import com.example.go4lunch.base.BaseActivity;
import com.example.go4lunch.databinding.ActivityMainHubBinding;
import com.example.go4lunch.databinding.NavHeaderMainHubBinding;
import com.example.go4lunch.notification.LunchReminderBroadcast;
import com.example.go4lunch.viewModel.MainHubViewModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@SuppressWarnings("deprecation")
public class MainHubActivity extends BaseActivity<ActivityMainHubBinding> implements NavigationView.OnNavigationItemSelectedListener {

    private Context context;

    private ActivityMainHubBinding mBinding;
    NavHeaderMainHubBinding headerBinding;

    MainHubViewModel myMainHubViewModel;

    SearchView searchView;

    private static final int RC_SIGN_IN = 123;
    private String API_KEY = "NONE";

    private final MapFragment mapFragment = MapFragment.newInstance();
    private final ListFragment listFragment = ListFragment.newInstance();
    private final WorkmatesFragment workmatesFragment = WorkmatesFragment.newInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mBinding = ActivityMainHubBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        this.context = this;

        myMainHubViewModel = new ViewModelProvider(this).get(MainHubViewModel.class);

        // Retrieve the api key from resource and share it with ViewModel
        retrieveApiKey();

        checkIsUserConnected();
    }

    // Check if user is already connected
    // If user is connected, the app directly display MainHubActivity and setup the listeners and observers
    // Else, the app display the Firebase's sign in activity before
    public void checkIsUserConnected() {
        if (myMainHubViewModel.getCurrentUser() != null) {
            showSnackBar(getString(R.string.auth_success));
            setUpListenersAndObservers();
        } else {
            startSignInWithActivity();
        }
    }

    // Setup the UI's listeners and the data's observer
    @SuppressLint("NonConstantResourceId")
    public void setUpListenersAndObservers() {
        myMainHubViewModel.searchUserLocation(this);

        createNotificationChannel();

        setSupportActionBar(mBinding.mainHubToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mBinding.mainHubDrawerLayout, mBinding.mainHubToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mBinding.mainHubDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        setFragment(mapFragment);
        mBinding.mainHubSideNavView.setNavigationItemSelectedListener(this);

        headerBinding = NavHeaderMainHubBinding.bind(mBinding.mainHubSideNavView.getHeaderView(0));

        // Allow navigation between fragments (Map, List, Workmates)
        mBinding.mainHubBottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.main_hub_menu_map:
                    searchView.setQueryHint(MainHubActivity.this.getResources().getString(R.string.action_search_restaurants));
                    setFragment(mapFragment);
                    return true;
                case R.id.main_hub_menu_list:
                    searchView.setQueryHint(MainHubActivity.this.getResources().getString(R.string.action_search_restaurants));
                    setFragment(listFragment);
                    return true;
                case R.id.main_hub_menu_workmates:
                    searchView.setQueryHint(MainHubActivity.this.getResources().getString(R.string.action_search_workmates));
                    setFragment(workmatesFragment);
                    return true;
                default:
                    return false;
            }
        });

        // Display the user's data once received
        myMainHubViewModel.getMyProfile().observe(this, this::loadProfile);

        myMainHubViewModel.getMyWorkmateData().observe(this, workmate -> {
            // If the user have selected a restaurant to eat, then the notification is activate
            assert workmate.getRestaurantUrl() != null;
            if(!workmate.getRestaurantUrl().equals("")) activateNotification(workmate.getId(), workmate.getRestaurantUrl());
            mBinding.mainHubSideNavView.setNavigationItemSelectedListener(item -> {
                // The side menu is closed when one of its button is touched
                mBinding.mainHubDrawerLayout.closeDrawer(GravityCompat.START);
                switch(item.getItemId()) {
                    case R.id.main_hub_menu_lunch:
                        // Open the RestaurantDetailsActivity
                        if(!workmate.getRestaurantUrl().isEmpty()) RestaurantDetailsActivity.openDetails(workmate.getRestaurantUrl(), mBinding.getRoot());
                        else showSnackBar(this.getResources().getString(R.string.nav_your_meal_no_restaurant_selected));
                        return true;
                    case R.id.main_hub_menu_settings:
                        // Open the settings of the app
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.fromParts("package", getPackageName(), null));
                        startActivity(intent);
                        return true;
                    case R.id.main_hub_menu_logout:
                        // Allow the user to log out
                        myMainHubViewModel.signOut(this).addOnSuccessListener(aVoid -> checkIsUserConnected());
                        return true;
                    default:
                        return false;
                }
            });
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        MenuItem myMenuItem = menu.findItem(R.id.main_hub_action_search);
        searchView = (SearchView) myMenuItem.getActionView();
        searchView.setQueryHint(this.getResources().getString(R.string.action_search_restaurants));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Start a search query when text is submitted by the user
                myMainHubViewModel.searchAction(mBinding.mainHubBottomNav.getSelectedItemId(), query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Start a search query when the text have changed
                myMainHubViewModel.searchAction(mBinding.mainHubBottomNav.getSelectedItemId(), newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onBackPressed() {
        // If the side menu is open, we close it when the user use the back button
        // Else the back button behave as normal
        if(mBinding.mainHubDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.mainHubDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Display the user's data in the side menu
    public void loadProfile(FirebaseUser user) {
        myMainHubViewModel.createWorkmate();
        if (user != null) {
            // Display the user's information inside the header of the NavigationView
            headerBinding.navHeaderTxtName.setText(user.getDisplayName());
            headerBinding.navHeaderTxtEmail.setText(user.getEmail());
            if(user.getPhotoUrl()!=null) Glide.with(this)
                        .load(user.getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(headerBinding.navHeaderImgAvatar);
            else Glide.with(this)
                        .load(R.drawable.img_avatar_default)
                        .apply(RequestOptions.circleCropTransform())
                        .into(headerBinding.navHeaderImgAvatar);
        }
    }

    // Setup fragment
    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_hub_frame_layout, fragment);
        fragmentTransaction.commit();
    }

    // Display a snack bar
    private void showSnackBar(String txt) {
        Log.i("OLD SNACK BACK", txt);
        //Snackbar.make(mBinding.getRoot(), txt, Snackbar.LENGTH_LONG).show();
    }

    // Create a notification channel dedicated to lunch reminder
    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(getString(R.string.notification_news_channel_id), getString(R.string.notification_channel_news), NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(getString(R.string.notification_description));

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private void activateNotification(String workmateId, String restaurantId) {
        Intent intent = new Intent(MainHubActivity.this, LunchReminderBroadcast.class);
        intent.putExtra("ApiKey", API_KEY);
        intent.putExtra("WorkmateId", workmateId);
        intent.putExtra("RestaurantId", restaurantId);

        intent.setComponent(new ComponentName(this.context, LunchReminderBroadcast.class));

        boolean alarmRunning = (PendingIntent.getBroadcast(this.context, 0, intent, PendingIntent.FLAG_NO_CREATE) != null);
        if(alarmRunning) PendingIntent.getBroadcast(this.context, 0, intent, PendingIntent.FLAG_NO_CREATE).cancel();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        // Set a calendar at 12 o'clock
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if(!alarmRunning) alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void startSignInWithActivity() {
        // Set the authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build());

        // Launch the activity for the authentication
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setLogo(R.drawable.ic_go4lunch)
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, false)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
        if(FirebaseAuth.getInstance().getCurrentUser()==null) startSignInWithActivity();
    }

    // Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        if(requestCode == RC_SIGN_IN) {
            // SUCCESS
            if(resultCode == RESULT_OK) {
                showSnackBar("Connection succeed !");
                setUpListenersAndObservers();
            }
            else {
                // ERRORS
                if(response == null) {
                    showSnackBar("Error while trying to authenticate");
                }
                else if(response.getError() != null) {
                    if(response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                        showSnackBar("No internet connection");
                    }
                    else if(response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                        showSnackBar("Unknow error");
                    }
                }
            }
        }
    }

    private void retrieveApiKey() {
        ApplicationInfo app = null;
        try {
            app = this.getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(app!=null) {
            Bundle bundle = app.metaData;
            API_KEY = bundle.getString("com.google.android.geo.API_KEY");
            myMainHubViewModel.setApiKey(API_KEY);
        }
    }

}
