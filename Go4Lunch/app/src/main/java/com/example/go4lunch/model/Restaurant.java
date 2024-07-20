package com.example.go4lunch.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Restaurant {

    /** Identifier **/
    private String id;

    /** Name of the restaurant **/
    private String name;

    /** Used to know if the place is a restaurant, when using autocomplete **/
    private boolean isARestaurant;

    /** Adress of the restaurant **/
    private String address;

    /** Latitude of the restaurant's location **/
    private double latitude;

    /** Longitude of the restaurant's location **/
    private double longitude;

    /** Distance between the user and the restaurant **/
    private int distance;

    /** Type of food served by the restaurant **/
    private int type;

    /** Rating of the restaurant **/
    private int stars = 0;

    /** Hour (in milliseconds) of the opening of the restaurant **/
    private boolean isOpen;

    /** Hour (in milliseconds) of the closure of the restaurant **/
    private String nextClosure;

    /** Reference of a photo for Google Place API **/
    private String imageReference;

    /** URL of an image of the restaurant **/
    private String imageURL;

    /** URL of the website of the restaurant **/
    private String webURL;

    /** Phone number of the restaurant **/
    private String phoneNumber;

    /** List of Workmate who eat here **/
    private List<Workmate> workmateList;

    public Restaurant(String id, String name, String address, float latitude, float longitude, int type, boolean isOpen, String nextClosure, String imageReference, int stars, String webURL, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.isOpen = isOpen;
        this.nextClosure = nextClosure;
        this.imageReference = imageReference;
        this.stars = stars;
        this.workmateList = new ArrayList<>();
        this.webURL = webURL;
        this.phoneNumber = phoneNumber;
    }

    public Restaurant(String id, String name, String address, float latitude, float longitude, int type, boolean isOpen, String nextClosure, String imageReference, int stars) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.isOpen = isOpen;
        this.nextClosure = nextClosure;
        this.imageReference = imageReference;
        this.stars = stars;
        this.workmateList = new ArrayList<>();
        this.webURL = "UNKNOWN";
        this.phoneNumber = "UNKNOWN";
    }

    public Restaurant(String id, String name, boolean isARestaurant) {
        this.id = id;
        this.name = name;
        this.isARestaurant = isARestaurant;
    }

    public Restaurant(boolean isOpen, String nextClosure, String website, String phone) {
        this.isOpen = isOpen;
        this.nextClosure = nextClosure;
        this.webURL = website;
        this.phoneNumber = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStars() {
        return stars;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public String getNextClosure() {
        return nextClosure;
    }

    public void setNextClosure(String  nextClosure) {
        this.nextClosure = nextClosure;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageReference() {
        return imageReference;
    }

    public String getWebURL() {
        return webURL;
    }

    public void setWebURL(String webURL) {
        this.webURL = webURL;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Workmate> getWorkmateList() {
        return workmateList;
    }

    public int getNumberOfWorkmate() {
        return workmateList.size();
    }

    public void setWorkmateList(List<Workmate> workmateList) {
        this.workmateList = workmateList;
    }

    public void addWorkmate(Workmate newWorkmate) {
        this.workmateList.add(newWorkmate);
    }

    public boolean isARestaurant() {
        return isARestaurant;
    }

    // Load an image from web
    public static Bitmap loadImageFromWeb(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");

            Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(),
                    d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            d.draw(canvas);

            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "Restaurant{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", isARestaurant=" + isARestaurant +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", distance=" + distance +
                ", type=" + type +
                ", stars=" + stars +
                ", isOpen=" + isOpen +
                ", nextClosure='" + nextClosure + '\'' +
                ", imageReference='" + imageReference + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", webURL='" + webURL + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", workmateList=" + workmateList +
                '}';
    }
}
