package com.codepath.travelplanner.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MyMapFragment extends MapFragment implements RoutingListener {
	protected Circle circle;
	protected Polyline polyline;
    
	protected TripLocation start;
	protected TripLocation end;
	
	protected int i = 0;
    
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
	public Circle createCircle(LatLng center, int radiusInMeters) {
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
		
		if(locations.size() > 1) {
			Routing routing = new Routing(Routing.TravelMode.WALKING);
			routing.registerListener(this);
			start = locations.get(0);
			end = locations.get(1);
			routing.execute(start.getLatLng(), end.getLatLng());
		}
	}
	
	/** Convert an address into a LatLng object, ****if you have an exact address use that, if not send the name of the place along with city and state**** */
	public LatLng getLatLngFromAddress(String address) {
		double latitude = 0;
		double longtitude = 0;
		Geocoder geoCoder = new Geocoder(getActivity());
		try {
			List<Address> addresses = geoCoder.getFromLocationName(address, 1); 
			if (addresses.size() >  0) {
				latitude = addresses.get(0).getLatitude();
				longtitude = addresses.get(0).getLongitude();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return new LatLng(latitude, longtitude);
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
    	createPolyline(mPolyOptions);
    	createMarker(start.getLatLng(), R.drawable.start_blue, start.getLocationName(), start.getMarkerDescription());
    	createMarker(end.getLatLng(), R.drawable.end_green, end.getLocationName(), end.getMarkerDescription());
      
    	CameraUpdate center = CameraUpdateFactory.newLatLng(start.getLatLng());
    	CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

    	getMap().moveCamera(zoom);
    	getMap().moveCamera(center);
      
		//TODO: store this is in the trip object later
		MainActivity.segments = new ArrayList<Segment>(segments);
	}
}
