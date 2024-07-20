package com.example.go4lunch.model;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Workmate implements Serializable {

    /** Identifier **/
    private String id;

    /** First name **/
    private String firstName;

    /** Avatar"s URL **/
    @Nullable
    private String avatarUrl;

    /** Name of the restaurant where the workmate plans to eat **/
    @Nullable
    private String restaurantName;

    /** ID of the restaurant where the workmate plans to eat **/
    @Nullable
    private String restaurantUrl;

    /** List of IDs of the workmate's favorites restaurants **/
    @Nullable
    private List<String> favoritesUrl = new ArrayList<>();

    public static final String COLLECTION_NAME = "workmates";
    public static final String FIRST_NAME_FIELD = "firstName";
    public static final String AVATAR_URL_FIELD = "avatarUrl";
    public static final String RESTAURANT_URL_FIELD = "restaurantUrl";
    public static final String RESTAURANT_NAME_FIELD = "restaurantName";
    public static final String FAVORITE_RESTAURANT_URL = "favoritesUrl";

    public Workmate() { }

    public Workmate(String id, String firstName, @Nullable String avatarUrl) {
        this.id = id;
        this.firstName = firstName;
        this.avatarUrl = avatarUrl;
        this.restaurantUrl = "";
        this.restaurantName = "";
    }

    public Workmate(String id, String firstName, @Nullable String avatarUrl, @Nullable String restaurantName, @Nullable String restaurantUrl) {
        this.id = id;
        this.firstName = firstName;
        this.avatarUrl = avatarUrl;
        this.restaurantName = restaurantName;
        this.restaurantUrl = restaurantUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    @Nullable
    public String getAvatarUrl() {
        return avatarUrl;
    }

    @Nullable
    public String getRestaurantName() {
        return restaurantName;
    }

    @Nullable
    public String getRestaurantUrl() {
        return restaurantUrl;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setRestaurantUrl(String restaurantUrl) {
        this.restaurantUrl = restaurantUrl;
    }

    @Nullable
    public List<String> getFavoritesRestaurantsUrl() {
        return favoritesUrl;
    }

    public void setFavoritesUrl(@Nullable List<String> favoritesUrl) {
        this.favoritesUrl = favoritesUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workmate workmate = (Workmate) o;
        return Objects.equals(id, workmate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
