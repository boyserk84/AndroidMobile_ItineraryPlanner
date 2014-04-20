package com.codepath.travelplanner.models;


/**
 * YelpFilterRequest.java
 * 
 * Strongly-typed data class for passing into Yelp API.
 * @author nkemavaha
 *
 */
public class YelpFilterRequest {
	public static final int SORT_BY_DISTANCE = 1;
	public static final double DEFAULT_ONE_MILE_RADIUS_IN_METER = 1609.34;
	
	public double longitude;
	
	public double latitude;
	
	public String term;
	
	public double radius = DEFAULT_ONE_MILE_RADIUS_IN_METER;	// 1 mile radius
	
	public int sortType = SORT_BY_DISTANCE;	// by distance
	
	public int limit = 50;	// Limit returning result
	
	/** see http://www.yelp.com/developers/documentation/category_list */
	public String categoryFilter;

}
