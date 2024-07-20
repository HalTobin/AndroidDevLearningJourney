package com.example.go4lunch.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Calendar;

public class GetRestaurantAttributes {

    public static JsonArray getPlaceList(JsonObject json) {
        if(json.get("results")==null) return null;
        else return json.get("results").getAsJsonArray();
    }

    public static JsonArray getPlacePrediction(JsonObject json) {
        if(json.get("predictions")==null) return null;
        else return json.get("predictions").getAsJsonArray();
    }

    public static JsonObject getPlaceDetails(JsonObject json) {
        if(json.get("result")==null) return null;
        else return json.get("result").getAsJsonObject();
    }

    public static String getPlaceId(JsonObject json) {
        return json.get("place_id").getAsString();
    }

    public static String getPlaceName(JsonObject json) {
        return json.get("name").getAsString();
    }

    public static String getMainText(JsonObject json) {
        return json.get("structured_formatting").getAsJsonObject().get("main_text").getAsString();
    }

    public static boolean getIsARestaurant(JsonObject json) {
        boolean isARestaurant = false;
        final JsonArray myTypes = json.get("types").getAsJsonArray();
        for(JsonElement jsonElement : myTypes) {
            if(jsonElement.getAsString().equals("restaurant")) isARestaurant = true;
        }
        return isARestaurant;
    }

    public static String getPlaceAddress(JsonObject json) {
        if(json.get("vicinity")==null) return "";
        else return json.get("vicinity").getAsString();
    }

    private static JsonObject getPlaceLocation(JsonObject json) {
        return json.get("geometry").getAsJsonObject()
                .get("location").getAsJsonObject();
    }

    public static float getPlaceLatitude(JsonObject json) {
        return getPlaceLocation(json).get("lat").getAsFloat();
    }

    public static float getPlaceLongitude(JsonObject json) {
        return getPlaceLocation(json).get("lng").getAsFloat();
    }

    public static int getPlaceRating(JsonObject json) {
        if(json.get("rating")==null) return 0;
        else return (int)Math.round((((json.get("rating").getAsFloat() - 1) / 4f) * 3));
    }

    public static String getPlaceImageReference(JsonObject json) {
        if(json.get("photos")==null) return "";
        else return json.get("photos").getAsJsonArray()
                .get(0).getAsJsonObject()
                .get("photo_reference").getAsString();
    }

    private static JsonObject getPlaceOpeningHours(JsonObject json) {
        return json.get("opening_hours").getAsJsonObject();
    }

    public static boolean getIsPlaceOpen(JsonObject json) {
        if(getPlaceOpeningHours(json).get("open_now")==null) return false;
        else return getPlaceOpeningHours(json).get("open_now").getAsBoolean();
    }

    public static String getPlaceNextClosure(JsonObject json) {
        String placeNextClosure = "UNKNOWN";
        if(getPlaceOpeningHours(json).get("periods")==null) return placeNextClosure;
        else {
            final JsonArray placePeriodsArray = getPlaceOpeningHours(json).get("periods").getAsJsonArray();
            if(getIsPlaceOpen(json)) {
                for(JsonElement periodsElements : placePeriodsArray) {
                    if(periodsElements!=null) {
                        if(inRange(periodsElements.getAsJsonObject())) {
                            placeNextClosure = periodsElements.getAsJsonObject().get("close").getAsJsonObject().get("time").getAsString();
                        }
                    } else placeNextClosure = "CLOSED";
                }
            } else placeNextClosure = "CLOSED";
        }

        return placeNextClosure;
    }

    // Test if the current hour correspond to an opening hour
    public static boolean inRange(JsonObject myPeriod) {
        boolean condition = false;
        Calendar cal = Calendar.getInstance();

        // Theses temp values are used to handle when a restaurant is open during saturday's night and sunday's morning
        int tempDayTest = cal.get(Calendar.DAY_OF_WEEK);
        if(tempDayTest==7) tempDayTest = 0;
        tempDayTest = tempDayTest - 1;
        int tempDayOut = myPeriod.get("close").getAsJsonObject().get("day").getAsInt();
        if((tempDayOut==0) && (tempDayTest==0)) tempDayOut = 7;

        int rangeTest = formatDayAndTimeInt(tempDayTest, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
        int rangeIn = formatDayAndTimeInt(myPeriod.get("open").getAsJsonObject().get("day").getAsInt(), myPeriod.get("open").getAsJsonObject().get("time").getAsString());
        int rangeOut = formatDayAndTimeInt(tempDayOut, myPeriod.get("close").getAsJsonObject().get("time").getAsString());

        if(rangeTest >= rangeIn && rangeTest <= rangeOut) condition = true;

        return condition;
    }

    // Convert the retrieves values into an unified int
    public static int formatDayAndTimeInt(int day, String time) {
        String temp = day + "" + time;
        return Integer.parseInt(temp);
    }

    // Convert the retrieves values into an unified int
    private static int formatDayAndTimeInt(int day, int hour, int min) {
        String tempHour = hour + "";
        String tempMin = min + "";

        if(hour < 10) tempHour = "0" + hour;
        if(min < 10) tempMin = "0" + min;

        String temp = day + tempHour + tempMin;

        return Integer.parseInt(temp);
    }

    public static String getPlacePhoneNumber(JsonObject json) {
        if(json.get("formatted_phone_number")==null) return "";
        else return json.get("formatted_phone_number").getAsString();
    }

    public static String getPlaceWebsite(JsonObject json) {
        if(json.get("website")==null) return "";
        else return json.get("website").getAsString();
    }

}
