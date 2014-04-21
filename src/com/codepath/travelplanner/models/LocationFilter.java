package com.codepath.travelplanner.models;

import java.io.Serializable;

/**
 * LocationFilter -
 */
public class LocationFilter implements Serializable {

	private String activity;

	private double rating;

	private double distance;

	public LocationFilter() {}

	public LocationFilter(String activity) {
		this.activity = activity;
	}

	public String getActivity() {
		return activity;
	}

	public double getDistance() {
		return distance;
	}

	public double getRating() {
		return rating;
	}
}
