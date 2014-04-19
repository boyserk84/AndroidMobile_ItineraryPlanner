package com.codepath.travelplanner.models;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

/**
 * TripLocation.java
 * 
 * Strongly typed data class for trip location.
 * @author nkemavaha
 *
 */
public class TripLocation {
	
	private LatLng latLng;
	
	private String locationName;
	
	private String description;
	
	private double rating;
	
	private double distance;
	
	
	private ArrayList<String> directions;
	
	/**
	 * @return Array of instruction how to get to this location from the starting location.
	 */
	public ArrayList<String> getDirections() {
		return directions;
	}

	/**
	 * @return LatLng of the current location
	 */
	public LatLng getLatLng() {
		return latLng;
	}
	
	/**
	 * @param LatLng of the current location
	 */
	public void setLatLng(LatLng latLng) {
		this.latLng = latLng;
	}

	/**
	 * @return Location or place name
	 */
	public String getLocationName() {
		return locationName;
	}
	
	/**
	 * @param Location or place name
	 */
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	/**
	 * @return Description of place
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @param Description of place
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return Yelp rating score
	 */
	public double getRating() {
		return rating;
	}
	
	/**
	 * @param Yelp rating score
	 */
	public void setRating(double rating) {
		this.rating = rating;
	}
	
	public String getMarkerDescription() {
		return description + " " + rating + " stars.";
	}

	/**
	 * @return How far this place/location from the starting location
	 */
	public double getDistance() {
		return distance;
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
			// TODO: Add more
		} catch (JSONException e) {
			tripLoc = null;
		}
		
		return tripLoc;
	}


	

}
