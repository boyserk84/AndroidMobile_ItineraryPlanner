package com.codepath.travelplanner.models;

import java.util.ArrayList;

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
	 * Helper function to convert JSONObject to strongly-typed data
	 * @param object
	 * @return
	 */
	public static TripLocation fromJSON( JSONObject object ){
		TripLocation tripLoc = new TripLocation();
		
		// TODO: make sure we get the correct key
		try {
			tripLoc.distance = object.getDouble("distance");
			tripLoc.rating = object.getDouble("rating");
			tripLoc.longitude = object.getDouble("longitude");
			tripLoc.latitude = object.getDouble("latitude");
			// TODO: Add more
		} catch (JSONException e) {
			tripLoc = null;
		}
		
		return tripLoc;
	}




}
