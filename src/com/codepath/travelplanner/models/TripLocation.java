package com.codepath.travelplanner.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * TripLocation.java
 * 
 * Strongly typed data class for trip location.
 * @author nkemavaha
 *
 */
public class TripLocation {
	
	private double longitude;
	
	private double latitude;
	
	private String locationName;
	
	private double rating;
	
	private double distance;
	
	private double fromLongitude;
	
	private double fromLatitude;
		
	private ArrayList<String> directions;
	
	private String imageUrl;
	
	private String mobileUrl;
	
	private String snippetText;
	
	private String snippetImageUrl;
	
	private LocationAddress address;
	
	
	/**
	 * @return Array of instruction how to get to this location from the starting location.
	 */
	public ArrayList<String> getDirections() {
		return directions;
	}

	/**
	 * @return Longitude of the current location
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @return Latitude of the current location
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @return Location or place name
	 */
	public String getLocationName() {
		return locationName;
	}

	/**
	 * @return Yelp rating score
	 */
	public double getRating() {
		return rating;
	}

	/**
	 * @return How far this place/location from the starting location
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * @return Longitude of starting location to this location.
	 */
	public double getFromLongitude() {
		return fromLongitude;
	}

	/**
	 * @return Latitude of starting location to this location.
	 */
	public double getFromLatitude() {
		return fromLatitude;
	}
	
	/**
	 * @return Get image URL of this location.
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * @return Mobile URL to place's website
	 */
	public String getMobileUrl() {
		return mobileUrl;
	}

	/**
	 * @return snippet Text of this location
	 */
	public String getSnippetText() {
		return snippetText;
	}

	/**
	 * @return snippet Image Url of this location
	 */
	public String getSnippetImageUrl() {
		return snippetImageUrl;
	}
	
	/**
	 * @return Address object of this location.
	 */
	public LocationAddress getAddress() {
		return address;
	}
	
	/**
	 * Helper function to convert JSONObject to strongly-typed data
	 * @param object	JSON Object (raw data)
	 * @return TripLocation Object. Otherwise, null is returned.
	 */
	public static TripLocation fromJSON( JSONObject object ){
		TripLocation tripLoc = new TripLocation();
		
		// TODO: make sure we get the correct key
		try {
			//tripLoc.longitude = object.getDouble("longitude");
			//tripLoc.latitude = object.getDouble("latitude");
			
			// From Yelp Api
			tripLoc.locationName = object.getString("name");
			tripLoc.rating = object.getDouble("rating");
			tripLoc.imageUrl = object.getString("image_url");
			tripLoc.mobileUrl = object.getString("mobile_url");
			tripLoc.snippetText = object.getString("snippet_text");
			tripLoc.snippetImageUrl = object.getString("snippet_image_url");
			tripLoc.distance = object.getDouble("distance");
			tripLoc.address = LocationAddress.fromJSON( object.getJSONObject("location"));

			// TODO: Add more
		} catch (JSONException e) {
			tripLoc = null;
		}
		
		return tripLoc;
	}

	/**
	 * Helper function convert JSONArray to strong-typed data array.
	 * @param arr		JSONArray (raw data)
	 * @return ArrayList of TripLocation data. Otherwise, empty array is returned.
	 */
	public static ArrayList<TripLocation> fromJSONArray( JSONArray arr ) {
		ArrayList<TripLocation> list = new ArrayList<TripLocation>();
		
		for (int i = 0; i < arr.length(); ++i ) {
			try {
				list.add( TripLocation.fromJSON( arr.getJSONObject( i ) ) );
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return list;
		
	}





}
