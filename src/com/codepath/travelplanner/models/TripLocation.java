package com.codepath.travelplanner.models;

import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * TripLocation.java
 * 
 * Strongly typed data class for trip location.
 * @author nkemavaha
 *
 */
public class TripLocation implements Serializable{

	private static final long serialVersionUID = 3786896511670324501L;

	private LatLng latLng;
	
	private String locationName;
	
	private String description;
	
	private double rating;
	
	private double distance;
		
	private ArrayList<String> directions;
	
	private String imageUrl;
	
	private String mobileUrl;
	
	private String snippetText;
	
	private String snippetImageUrl;
	
	private String ratingImgUrl;
	
	private LocationAddress address;

	/** empty constructor */
	public TripLocation() {}
	
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
		return (description != null ? description + " " : "") + rating + " stars.";
	}

	/**
	 * @return How far this place/location from the starting location
	 */
	public double getDistance() {
		return distance;
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
	 * @return Image of rating of this location corresponding to rating value.
	 */
	public String getRatingImgUrl() {
		return ratingImgUrl;
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
			// From Yelp Api
			tripLoc.locationName = object.getString("name");
			tripLoc.rating = object.getDouble("rating");
			if (object.has("image_url")) {
				tripLoc.imageUrl = object.getString("image_url");
			}
			tripLoc.mobileUrl = object.getString("mobile_url");
			tripLoc.snippetText = object.getString("snippet_text");
			tripLoc.snippetImageUrl = object.getString("snippet_image_url");
			tripLoc.distance = object.getDouble("distance");
			tripLoc.ratingImgUrl = object.getString("rating_img_url");
			tripLoc.address = LocationAddress.fromJSON( object.getJSONObject("location"), tripLoc.locationName );

			// TODO: Add more
		} catch (JSONException e) {
			Log.d("travelIt", "TripLocation.fromJSON error - " + tripLoc.locationName + " :: " + e.getMessage());
			e.printStackTrace();
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
