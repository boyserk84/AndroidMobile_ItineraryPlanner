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
	
	private long tripId;
	
	private String tripName;
	
	private String createdBy;
	
	private String createdDate;
	
	private String modifiedDate;
	
	private ArrayList<TripLocation> places;

	/** Default constructor*/
	public Trip() {
		this.places = new ArrayList<TripLocation>();
	}


	/**
	 * Constructor
	 * @param name	Name of the trip
	 */
	public Trip(String name) {
		this.tripName = name;
	}
	
	/**
	 * @return Trip unique Id
	 */
	public long getTripId() {
		return tripId;
	}
	
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
	
	/**
	 * @return Array of places/locations user is planning to visit for this trip.
	 */
	public void setPlaces(ArrayList<TripLocation> places) {
		this.places = places;
	}

}
