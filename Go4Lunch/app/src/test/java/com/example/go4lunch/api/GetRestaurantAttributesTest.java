package com.example.go4lunch.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Calendar;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Calendar.class, GetRestaurantAttributes.class})
public class GetRestaurantAttributesTest {

    public final String BASE_PATH = "src/test/java/com/example/go4lunch/api/restaurantsPlaceholder/";
    public final String JSON_PATH_NEARBYSEARCH = BASE_PATH + "nearbySearch.json";
    public final String JSON_PATH_AUTOCOMPLETE = BASE_PATH + "autocomplete.json";
    public final String JSON_PATH_RESTAURANTDETAILS = BASE_PATH + "restaurantDetails.json";
    public final String JSON_PATH_HOURS = BASE_PATH + "restaurantHours.json";

    JsonObject nearbySearchJson;
    JsonObject autocompleteJson;
    JsonObject restaurantDetailsJson;

    JsonObject hoursJson;

    @InjectMocks
    private GetRestaurantAttributes getRestaurantAttributes = new GetRestaurantAttributes();

    @Before
    public void setup() {
        // Load Json placeholders
        nearbySearchJson = loadLocalJson(JSON_PATH_NEARBYSEARCH).getAsJsonObject();
        autocompleteJson = loadLocalJson(JSON_PATH_AUTOCOMPLETE).getAsJsonObject();
        restaurantDetailsJson = loadLocalJson(JSON_PATH_RESTAURANTDETAILS).getAsJsonObject();

        hoursJson = loadLocalJson(JSON_PATH_HOURS).getAsJsonObject();

        // Mock calendar
        /*Calendar now = Calendar.getInstance();
        now.set(2021, Calendar.OCTOBER, 11 ,19,45,0);
        PowerMockito.mockStatic(Calendar.class);
        PowerMockito.when(Calendar.getInstance()).thenReturn(now);*/
    }

    public void setCalendarMockUp(int year, int month, int date, int hourOfDay, int minute, int sec) {
        // Mock calendar
        Calendar now = Calendar.getInstance();
        now.set(year, month, date ,hourOfDay,minute,sec);
        PowerMockito.mockStatic(Calendar.class);
        PowerMockito.when(Calendar.getInstance()).thenReturn(now);
    }

    @Test
    public void getNearbySearchResults() {
        JsonArray toTest = GetRestaurantAttributes.getPlaceList(nearbySearchJson);
        assertEquals(20, toTest.size());
    }

    @Test
    public void getAutocompleteResults() {
        JsonArray toTest = GetRestaurantAttributes.getPlacePrediction(autocompleteJson);
        assertEquals(5, toTest.size());
    }

    @Test
    public void getPlaceDetails() {
        setCalendarMockUp(2021, Calendar.OCTOBER, 11 ,19,45,0);
        JsonObject toTest = GetRestaurantAttributes.getPlaceDetails(restaurantDetailsJson);
        testId(toTest);
        testName(toTest);
        testAddress(toTest);
        testLatitude(toTest);
        testLongitude(toTest);
        testIsOpen(toTest);
        testNextClosure(toTest);
        testImageReference(toTest);
        testRating(toTest);
        testWebsite(toTest);
        testPhoneNumber(toTest);
    }

    public void testId(JsonObject toTest) {
        assertEquals("ChIJ2bUYI4pt5kcRzXOD0ADpne0", GetRestaurantAttributes.getPlaceId(toTest));
    }

    public void testName(JsonObject toTest) {
        assertEquals("Saporo", GetRestaurantAttributes.getPlaceName(toTest));
    }

    public void testAddress(JsonObject toTest) {
        assertEquals("152 Boulevard de Charonne, Paris", GetRestaurantAttributes.getPlaceAddress(toTest));
    }

    public void testLatitude(JsonObject toTest) {
        assertEquals(48.8567467f, GetRestaurantAttributes.getPlaceLatitude(toTest), 0.0001f);
    }

    public void testLongitude(JsonObject toTest) {
        assertEquals(2.3945803f, GetRestaurantAttributes.getPlaceLongitude(toTest), 0.0001f);
    }

    public void testIsOpen(JsonObject toTest) {
        assertTrue(GetRestaurantAttributes.getIsPlaceOpen(toTest));
    }

    public void testNextClosure(JsonObject toTest) {
        assertEquals("2300", GetRestaurantAttributes.getPlaceNextClosure(toTest));
    }

    public void testImageReference(JsonObject toTest) {
        assertEquals("Aap_uEArulC94GqRgLcC-DQ00v-VVgw6-Hc_x8K9fg2YaHUfXvh9WbShg9smmVWeUrWBFbl_R3Ovxu63igzVUB-QL2nrOxx0Itbt2tLOKHC0PwjozvBoKQnVbCNKbSPaDex86IGjBS3LhwuBSSm10xb1Usw1x6LxwsE6Oo6-bUFzf1zuiJ0H", GetRestaurantAttributes.getPlaceImageReference(toTest));
    }

    public void testRating(JsonObject toTest) {
        assertEquals(2, GetRestaurantAttributes.getPlaceRating(toTest));
    }

    public void testWebsite(JsonObject toTest) {
        assertEquals("http://www.saporo20.com/", GetRestaurantAttributes.getPlaceWebsite(toTest));
    }

    public void testPhoneNumber(JsonObject toTest) {
        assertEquals("01 43 73 88 70", GetRestaurantAttributes.getPlacePhoneNumber(toTest));
    }

    @Test
    public void testFormatDayAndTimeInt() {
        int test = GetRestaurantAttributes.formatDayAndTimeInt(2, "1145");
        assertEquals(21145, test);
        test = GetRestaurantAttributes.formatDayAndTimeInt(3, "0030");
        assertEquals(30030, test);
    }

    @Test
    public void testInRange() {
        final JsonObject firstTest = hoursJson.get("periods").getAsJsonArray().get(0).getAsJsonObject();
        final JsonObject secondTest = hoursJson.get("periods").getAsJsonArray().get(1).getAsJsonObject();

        // Test on a monday
        setCalendarMockUp(2021, Calendar.OCTOBER, 11 ,19,45,0);
        assertEquals(false, GetRestaurantAttributes.inRange(firstTest));
        assertEquals(true, GetRestaurantAttributes.inRange(secondTest));

        // Test on a sunday
        setCalendarMockUp(2021, Calendar.OCTOBER, 17, 19, 45, 0);
        assertEquals(true, GetRestaurantAttributes.inRange(firstTest));
        assertEquals(false, GetRestaurantAttributes.inRange(secondTest));
    }

    // Load a Json file from local resources
    public JsonElement loadLocalJson(String path) {
        try (Reader reader = new InputStreamReader(new FileInputStream(path), "UTF-8")) {
            return JsonParser.parseReader(reader);
        } catch (Exception e) {
            return null;
        }
    }

}
