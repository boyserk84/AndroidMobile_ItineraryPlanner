package com.codepath.travelplanner.models;


import java.io.Serializable;

/**
 * YelpFilterRequest.java
 * 
 * Strongly-typed data class for passing into Yelp API.
 * @author nkemavaha
 *
 */
public class YelpFilterRequest implements Serializable {
	private static final long serialVersionUID = 1587982519033411932L;
	
	public static final int SORT_BY_DISTANCE = 1;
	public static final int SORT_BY_BEST_MATCH = 0;
	public static final int SORT_BY_HIGHEST_RATE = 2;
	
	public static final double DEFAULT_ONE_MILE_RADIUS_IN_METER = 1609.34;
	public static final double DEFAULT_SEARCH_RADIUS_IN_METER = 1609.34 * 20; //20 miles
	public static final double LOCAL_SEARCH_RADIUS_IN_METERS = 402.336; //.25 miles
	
	public double longitude;
	
	public double latitude;
	
	public String term = "";
	
	public double radius = DEFAULT_SEARCH_RADIUS_IN_METER;	// 25 mile radius
	
	public int sortType = SORT_BY_BEST_MATCH;	// by BestMatch
	
	public int limit = 20;	// Limit returning result
	
	/** see http://www.yelp.com/developers/documentation/category_list */
	public String categoryFilter = "";

	/** constructor */
	public YelpFilterRequest() {};
}
