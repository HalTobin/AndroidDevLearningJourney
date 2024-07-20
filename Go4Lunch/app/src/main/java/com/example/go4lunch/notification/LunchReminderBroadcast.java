package com.example.go4lunch.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.go4lunch.R;
import com.example.go4lunch.api.GooglePlacesApi;
import com.example.go4lunch.api.RetrofitClient;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.Workmate;
import com.example.go4lunch.repository.WorkmateRepository;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LunchReminderBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Retrofit retrofit = RetrofitClient.getClient();
        GooglePlacesApi googlePlacesApi = retrofit.create(GooglePlacesApi.class);
        WorkmateRepository workmateRepository = WorkmateRepository.getInstance();

        // Retrieve the data required to get the details of a restaurant
        String apiKey = (String) intent.getSerializableExtra("ApiKey");
        String workmateId = (String) intent.getSerializableExtra("WorkmateId");
        String restaurantId = (String) intent.getSerializableExtra("RestaurantId");

        // Send a query to get the details of a restaurant
        Call<Restaurant> call = googlePlacesApi.getDetails(restaurantId, apiKey, "name,opening_hours,formatted_phone_number,website,place_id,geometry,vicinity,photo,rating");
        call.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                assert response.body() != null;
                workmateRepository.getWorkmatesFromRestaurantAsTask(restaurantId).addOnCompleteListener(task -> {
                    List<Workmate> alarmWorkmates = new ArrayList<>();
                    for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
                        Workmate temp = snapshot.toObject(Workmate.class);
                        temp.setId(snapshot.getId());
                        // Add a user to the list only if it's not the current user
                        if(!temp.getId().equals(workmateId)) alarmWorkmates.add(temp);
                    }
                    buildAlarm(context, response.body(), alarmWorkmates);
                });
            }

            @Override
            public void onFailure(Call<Restaurant> subCall, Throwable subT) {
                //Log.e("ERROR" + subT.getMessage());
            }
        });
    }

    private void buildAlarm(Context context, Restaurant restaurant, List<Workmate> workmateList) {
        // Build the String to display in notification
        StringBuilder notificationText;
        if(workmateList.isEmpty()) notificationText = new StringBuilder(context.getString(R.string.notification_alone));
        else {
            notificationText = new StringBuilder(context.getString(R.string.notification_not_alone) + " ");
            for(Workmate workmate : workmateList) {
                if(workmateList.indexOf(workmate)== workmateList.size()-2) notificationText.append(workmate.getFirstName()).append(" ").append(context.getString(R.string.notification_and)).append(" ");
                else if (workmateList.indexOf(workmate)== workmateList.size()-1) notificationText.append(workmate.getFirstName()).append(" ");
                else notificationText.append(workmate.getFirstName()).append(", ");
            }
            notificationText.append(context.getString(R.string.notification_not_alone_end));
        }

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.notification_news_channel_id))
                .setSmallIcon(R.drawable.ic_go4lunch)
                .setContentTitle(restaurant.getName() + " - " + restaurant.getAddress())
                .setContentText(notificationText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setChannelId(context.getString(R.string.notification_news_channel_id))
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(200, builder.build());
    }
}
