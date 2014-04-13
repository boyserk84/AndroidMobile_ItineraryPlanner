package com.codepath.travelplanner.models;

import java.util.ArrayList;

/**
 * Trip.java
 * Strongly typed data class for trip information
 * @author nkemavaha
 *
 */
public class Trip {
	
	private String timeStamp;
	
	private String tripName;
	
	private String createdBy;
	
	private String createdDate;
	
	private String modifiedDate;
	
	private ArrayList<TripLocation> places;

	/**
	 * @return TimeStamp
	 */
	public String getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @return Name of this trip
	 */
	public String getTripName() {
		return tripName;
	}

	/**
	 * @return Name of user who created this trip.
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @return Timestamp of trip creation
	 */
	public String getCreatedDate() {
		return createdDate;
	}

	/**
	 * @return Timestamp when this trip object is modified.
	 */
	public String getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @return Array of places/locations user is planning to visit for this trip.
	 */
	public ArrayList<TripLocation> getPlaces() {
		return places;
	}

}
