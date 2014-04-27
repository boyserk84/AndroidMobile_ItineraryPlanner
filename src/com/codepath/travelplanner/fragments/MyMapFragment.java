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
import com.codepath.travelplanner.apis.GooglePlacesClient;
import com.codepath.travelplanner.dialogs.BaseTripWizardDialog.OnNewTripListener;
import com.codepath.travelplanner.directions.Routing;
import com.codepath.travelplanner.directions.RoutingListener;
import com.codepath.travelplanner.directions.Segment;
import com.codepath.travelplanner.helpers.Util;
import com.codepath.travelplanner.models.GooglePlace;
import com.codepath.travelplanner.models.Trip;
import com.codepath.travelplanner.models.TripLocation;
import com.codepath.travelplanner.models.YelpFilterRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.*;
import natemobiles.app.simpleyelpapiforandroid.interfaces.IRequestListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * MyMapFragment - custom map fragment
 */
public class MyMapFragment extends MapFragment implements RoutingListener, IRequestListener, GoogleMap.OnCameraChangeListener {
	/** color of the multi circles */
	private static final String MULTI_CIRCLE_COLOR = "#40E6E600";
	/** map of long/lat to circle object for the multi circles */
	protected HashMap<String, Circle> coordToCircles = new HashMap<String, Circle>();
	protected ArrayList<Marker> suggestedPlaces = new ArrayList<Marker>();
	protected Circle circle;
	protected Polyline polyline;
    
	protected TripLocation start;
	protected TripLocation end;
	
	protected Marker startMarker;
	protected Marker endMarker;
	
	protected ArrayList<TripLocation> suggPlacesList;
    
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

	@Override
	public void onCameraChange(CameraPosition cameraPosition) {
		// redo the search for subway stations around the area when camera position has changed
		searchNearbySubwayStations(cameraPosition.target);
	}

	/**
	 * Searches the Google Places API for the nearby subway stations at some location
	 * @param location		location to perform the search
	 */
	protected void searchNearbySubwayStations(LatLng location) {
		(new GooglePlacesClient()).search(GooglePlacesClient.SUBWAY_STATION_QUERY, location.latitude, location.longitude, this);
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

	/** Adds a circle to the map but do not remove previous circles of this type */
	public void addMultiCircle(LatLng center, double radiusInMeters) {
		CircleOptions options = new CircleOptions();
		options.center(center);
		options.radius(radiusInMeters);
		options.strokeWidth(2);
		options.strokeColor(Color.parseColor(MULTI_CIRCLE_COLOR));
		options.fillColor(Color.parseColor(MULTI_CIRCLE_COLOR));
		coordToCircles.put(center.latitude+","+center.longitude, getMap().addCircle(options));
	}

	/** Removes all 'multi' circles on the map */
	public void removeAllMultiCircles() {
		for (Circle circle : coordToCircles.values()) {
			circle.remove();
		}
		coordToCircles.clear();
	}
	
	/** Removes all suggested places from the map */
	public void clearSuggestedPlaces() {
		for (Marker marker : suggestedPlaces) {
			marker.remove();
		}
		suggestedPlaces.clear();
	}
	
	/** Generate new directions */
	public void newRoute(Trip trip) {
		ArrayList<TripLocation> locations = trip.getPlaces();
		if (locations.size() > 1) {
			getMap().setOnMapClickListener(null);
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
			if(startMarker != null) {
				startMarker.remove();
			}
			if(endMarker != null) {
				endMarker.remove();
			}
			startMarker = createMarker(start.getLatLng(), R.drawable.start_blue, start.getLocationName(), start.getMarkerDescription());
			endMarker = createMarker(end.getLatLng(), R.drawable.end_green, end.getLocationName(), end.getMarkerDescription());

			CameraUpdate center = CameraUpdateFactory.newLatLng(start.getLatLng());
			CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

			getMap().moveCamera(zoom);
			getMap().moveCamera(center);
			
			getMap().setOnMapClickListener(new OnMapClickListener() {
	            @Override
	            public void onMapClick(LatLng point) {
	            	OnNewTripListener listener = (OnNewTripListener) getActivity();
					listener.openAddDialog(point);
	            }
	        });

			//TODO: Jeff: store this is in the trip object later
			MainActivity.segments = new ArrayList<Segment>(segments);
		}
	}
	
	public void enterMapSelectionMode(ArrayList<TripLocation> suggPlaces, boolean newTrip) {
		suggPlacesList = suggPlaces;
		removeAllMultiCircles();
		getMap().setOnMapClickListener(null);
		clearSuggestedPlaces();
		for(int i = 0; i < suggPlacesList.size(); i++) {
			TripLocation toAdd = suggPlacesList.get(i);
			toAdd.setLatLng(Util.getLatLngFromAddress(toAdd.getAddress().toString(), getActivity()));
			MarkerOptions options = new MarkerOptions();
			options.position(suggPlacesList.get(i).getLatLng());
			options.title(Integer.toString(i));
			suggestedPlaces.add(getMap().addMarker(options));
		}
		getMap().setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker selected) {
				try{
					// try to show confirm route dialog
					int index = Integer.parseInt(selected.getTitle());
					TripLocation tripLocation = suggPlacesList.get(index);
					OnNewTripListener listener = (OnNewTripListener) getActivity();
					listener.openConfirmDialog(tripLocation);
					return true;
				}
				catch(Exception e) {
					// do default behavior
					return false;
				}
			}
		});
		
		CameraUpdate zoom;
		if(newTrip) {
			zoom = CameraUpdateFactory.zoomTo(12);
		} else {
			zoom = CameraUpdateFactory.zoomTo(15);
		}
		getMap().setOnCameraChangeListener(this);
		getMap().animateCamera(zoom);
	}
	
	public void exitMapSelectionMode() {
		suggPlacesList = null;
		clearSuggestedPlaces();
		removeAllMultiCircles();
		getMap().setOnMarkerClickListener(null);
		getMap().setOnMapClickListener(new OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
            	OnNewTripListener listener = (OnNewTripListener) getActivity();
				listener.openAddDialog(point);
            }
        });
	}

	@Override
	public void onSuccess(JSONObject successResult) {
		try {
			JSONArray results = successResult.getJSONArray("results");
			for (int i = 0; i < results.length(); i++) {
				GooglePlace place = GooglePlace.fromJSONObject((JSONObject) results.get(i));
				if (coordToCircles.get(place.getLatitude()+","+place.getLongitude()) == null) {
					// only add circle if it's at a new place
					LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());
					addMultiCircle(latLng, YelpFilterRequest.DEFAULT_ONE_MILE_RADIUS_IN_METER/4);
					createMarker(latLng, R.drawable.ic_subway, place.getName(), "");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFailure(JSONObject failureResult) {
		// TODO Auto-generated method stub
	}
}
