package com.codepath.travelplanner.directions;

import java.util.List;

import com.google.android.gms.maps.model.PolylineOptions;

public interface RoutingListener {
	public void onRoutingFailure();
	public void onRoutingStart();
	public void onRoutingSuccess(PolylineOptions mPolyOptions, List<Segment> segments, int duration);
}