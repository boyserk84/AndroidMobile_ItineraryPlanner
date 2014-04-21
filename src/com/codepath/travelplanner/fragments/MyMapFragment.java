package com.codepath.travelplanner.fragments;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.codepath.travelplanner.R;
import com.codepath.travelplanner.activities.MainActivity;
import com.codepath.travelplanner.directions.Routing;
import com.codepath.travelplanner.directions.RoutingListener;
import com.codepath.travelplanner.directions.Segment;
import com.codepath.travelplanner.models.Trip;
import com.codepath.travelplanner.models.TripLocation;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * MyMapFragment - custom map fragment
 */
public class MyMapFragment extends MapFragment implements RoutingListener {
	protected Circle circle;
	protected Polyline polyline;
    
	protected TripLocation start;
	protected TripLocation end;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mapView = super.onCreateView(inflater, container, savedInstanceState);   

		View locationButton = ((View) mapView.findViewById(1).getParent()).findViewById(2);

		RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
		rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
		rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		rlp.setMargins(0, 0, 30, 30);
	
		getMap().getUiSettings().setRotateGesturesEnabled(false);
		getMap().getUiSettings().setZoomControlsEnabled(false);
		getMap().setMyLocationEnabled(true);
		getMap().setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
			@Override
			public void onMyLocationChange(Location location) {
				CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
				CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

				getMap().moveCamera(zoom);
				getMap().moveCamera(center);
				getMap().setOnMyLocationChangeListener(null);
			}
		});
		return mapView;
	}
	
	
	/** Creates a polyline on the map */
	protected void createPolyline(PolylineOptions mPolyOptions) {
		//only allow one polyline on the map? if we use multiple polylines per a route then we will only allow one set of polylines per a map
		if(polyline != null) {
			polyline.remove();
		}
		PolylineOptions polyoptions = new PolylineOptions();
		polyoptions.color(Color.BLUE);
		polyoptions.width(10);
		polyoptions.addAll(mPolyOptions.getPoints());
		polyline = getMap().addPolyline(polyoptions);
	}
	
	/** Creates a marker on the map */
	public Marker createMarker(LatLng start, int drawable, String title, String details) {
		MarkerOptions options = new MarkerOptions();
		options.position(start);
		options.icon(BitmapDescriptorFactory.fromResource(drawable));
		options.title(title);
		options.snippet(details);
		return getMap().addMarker(options);
	}
	
	/** Creates a circle on the map */
	public Circle createCircle(LatLng center, double radiusInMeters) {
		//only allow one circle on the map
		if(circle != null) {
			circle.remove();
		}
		CircleOptions options = new CircleOptions();
		options.center(center);
		options.radius(radiusInMeters);
		options.strokeWidth(2);
		options.strokeColor(Color.BLUE);
		options.fillColor(Color.parseColor("#500084d3"));
		circle = getMap().addCircle(options);
		return circle;
	}
	
	/** Generate new directions */
	public void newRoute(Trip trip) {
		ArrayList<TripLocation> locations = trip.getPlaces();
		if (locations.size() > 1) {
			start = trip.getStart();
			end = trip.getEnd();
			Routing routing = new Routing(Routing.TravelMode.TRANSIT);
			routing.registerListener(this);
			routing.execute(start.getLatLng(), end.getLatLng());
		}
	}
	
	@Override
	public void onRoutingFailure() {
		// The Routing request failed
	}

	@Override
	public void onRoutingStart() {
    	// The Routing Request starts
	}

	@Override
	public void onRoutingSuccess(PolylineOptions mPolyOptions, List<Segment> segments) {
		if (start != null && end != null) {
			createPolyline(mPolyOptions);
			createMarker(start.getLatLng(), R.drawable.start_blue, start.getLocationName(), start.getMarkerDescription());
			createMarker(end.getLatLng(), R.drawable.end_green, end.getLocationName(), end.getMarkerDescription());

			CameraUpdate center = CameraUpdateFactory.newLatLng(start.getLatLng());
			CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

			getMap().moveCamera(zoom);
			getMap().moveCamera(center);

			//TODO: Jeff: store this is in the trip object later
			MainActivity.segments = new ArrayList<Segment>(segments);
		}
	}
}
