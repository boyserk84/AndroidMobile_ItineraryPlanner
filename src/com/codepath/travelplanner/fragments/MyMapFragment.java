package com.codepath.travelplanner.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.*;
import natemobiles.app.simpleyelpapiforandroid.interfaces.IRequestListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * MyMapFragment - custom map fragment
 */
public class MyMapFragment extends MapFragment implements RoutingListener, IRequestListener, GoogleMap.OnCameraChangeListener {
	/** color of the multi circles */
	private static final String MULTI_CIRCLE_COLOR = "#40E6E600";
	/** list of markers used in the route sorted alphabetically */
	private static final List<Integer> ORDERED_ROUTE_MARKERS = Arrays.asList(R.drawable.blue_marker_a, R.drawable.blue_marker_b, R.drawable.blue_marker_c, R.drawable.blue_marker_d,
			R.drawable.blue_marker_e, R.drawable.blue_marker_f, R.drawable.blue_marker_g, R.drawable.blue_marker_h);
	/** map of long/lat to circle object for the multi circles */
	protected HashMap<String, Circle> coordToCircles = new HashMap<String, Circle>();
	protected ArrayList<Marker> suggestedPlaces = new ArrayList<Marker>();
	protected ArrayList<Polyline> polylines = new ArrayList<Polyline>();

	/** the marker replaced the marker that was clicked */
	protected Marker replacementMarker = null;
	
    protected ArrayList<TripLocation> currentlyRouting;
    protected int currentNode;
	protected TripLocation start;
	protected TripLocation end;
	protected int totalDuration;
	protected ArrayList<Marker> routeMarkers = new ArrayList<Marker>();
	
	protected ArrayList<TripLocation> suggPlacesList;

	protected MapListener mapListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mapListener = (MapListener) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mapView = super.onCreateView(inflater, container, savedInstanceState);
	
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
		getMap().setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng latLng) {
				mapListener.onMapClick();
			}
		});
		getMap().setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker selected) {
				try {
					// try to show marker details
					int index = Integer.parseInt(selected.getTitle());
					TripLocation tripLocation = suggPlacesList.get(index);
					mapListener.onMarkerClick(tripLocation);
					// replace selected marker with a different marker icon to differentiate from other markers
					if (replacementMarker != null) {
						replaceReplacementMarker(); // replaces the old replacement marker
					}
					replacementMarker = createMarker(selected.getPosition(), R.drawable.ic_big_marker, selected.getTitle(), "");
					selected.remove();
					return true;
				} catch (Exception e) {
					// do default behavior
					return false;
				}
			}
		});
		getMap().setOnCameraChangeListener(this);
		return mapView;
	}

	@Override
	public void onCameraChange(CameraPosition cameraPosition) {
		// redo the search for subway stations around the area when camera position has changed
		searchNearbyTransitStations(cameraPosition.target);
	}

	/**
	 * Searches the Google Places API for the nearby transit stations at some location
	 * @param location		location to perform the search
	 */
	protected void searchNearbyTransitStations(LatLng location) {
		(new GooglePlacesClient()).search(GooglePlacesClient.TRANSIT_STATION_QUERY, location.latitude, location.longitude, this);
	}

	/** Creates a polyline on the map */
	protected void createPolyline(PolylineOptions mPolyOptions) {
		PolylineOptions polyoptions = new PolylineOptions();
		polyoptions.color(getResources().getColor(R.color.theme_color));
		polyoptions.width(10);
		polyoptions.addAll(mPolyOptions.getPoints());
		polylines.add(getMap().addPolyline(polyoptions));
	}
	
	/** Removes all suggested places from the map */
	public void clearPolylines() {
		for (Polyline pLine : polylines) {
			pLine.remove();
		}
		polylines.clear();
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

	/** Adds a circle to the map but do not remove previous circles of this type */
	public void addMultiCircle(LatLng center, double radiusInMeters) {
		CircleOptions options = new CircleOptions();
		options.center(center);
		options.radius(radiusInMeters);
		options.strokeWidth(2);
		options.strokeColor(Color.parseColor(MULTI_CIRCLE_COLOR));
		options.fillColor(Color.parseColor(MULTI_CIRCLE_COLOR));
		coordToCircles.put(center.latitude + "," + center.longitude, getMap().addCircle(options));
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
	
	/** Removes all route markers from the map */
	public void clearRouteMarkers() {
		for (Marker marker : routeMarkers) {
			marker.remove();
		}
		routeMarkers.clear();
	}
	
	/** Generate new directions */
	public void newRoute(Trip trip) {
		currentlyRouting = trip.getPlaces();
		if (currentlyRouting.size() > 1) {
			clearPolylines();
			clearRouteMarkers();
			MainActivity.segments = new ArrayList<Segment>();
			getMap().setOnMapLongClickListener(null);
			totalDuration = 0;
			currentNode = 1;
			nextRoute();
		}
	}

	protected boolean nextRoute() {
		if(currentNode == currentlyRouting.size()) {
			return false;
		}
		
		start = currentlyRouting.get(currentNode - 1);
		end = currentlyRouting.get(currentNode);
		
		Routing routing = new Routing(Routing.TravelMode.TRANSIT);
		routing.registerListener(this);
		routing.execute(start.getLatLng(), end.getLatLng());
		return true;
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
	public void onRoutingSuccess(PolylineOptions mPolyOptions, List<Segment> segments, int duration) {
		if (start != null && end != null) {
			//TODO: Jeff: store this is in the trip object later
			MainActivity.segments.addAll(segments);
			createPolyline(mPolyOptions);
			totalDuration += duration;

			if (currentNode == 1) {
				// add start marker if we're on the first node
				routeMarkers.add(createMarker(start.getLatLng(), ORDERED_ROUTE_MARKERS.get(0), "Start", ""));
			}
			routeMarkers.add(createMarker(end.getLatLng(), ORDERED_ROUTE_MARKERS.get(currentNode % ORDERED_ROUTE_MARKERS.size()), end.getLocationName(), end.getMarkerDescription()));
			
			currentNode++;
			if(!nextRoute()) {
				CameraUpdate center = CameraUpdateFactory.newLatLng(start.getLatLng());
				CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
	
				getMap().moveCamera(zoom);
				getMap().moveCamera(center);
				
				getMap().setOnMapLongClickListener(new OnMapLongClickListener() {
		            @Override
		            public void onMapLongClick(LatLng point) {
		            	OnNewTripListener listener = (OnNewTripListener) getActivity();
						listener.openAddDialog(point);
		            }
		        });
	
				mapListener.onRouted(Util.getFormattedDuration(totalDuration));
			}
		}
	}
	
	/**
	 * Enter Map Selection mode
	 * @param suggPlaces		Array of suggested places
	 * @param newTrip			Whether this is a new trip
	 * @param callback			Callback when map is done loading.
	 */
	public void enterMapSelectionMode(ArrayList<TripLocation> suggPlaces, boolean newTrip) {
		suggPlacesList = suggPlaces;
		getMap().setOnMapLongClickListener(null);
		clearSuggestedPlaces();
		for(int i = 0; i < suggPlacesList.size(); i++) {
			TripLocation toAdd = suggPlacesList.get(i);
			toAdd.setLatLng(Util.getLatLngFromAddress(toAdd.getAddress().toString(), getActivity()));
			suggestedPlaces.add(createMarker(suggPlacesList.get(i).getLatLng(), R.drawable.ic_pin, Integer.toString(i), ""));
		}
		
		boolean shouldWaitForCallback = false;
		CameraUpdate zoom;
		if(newTrip) {
			zoom = CameraUpdateFactory.zoomTo(12);
			shouldWaitForCallback = (int) getMap().getCameraPosition().zoom != 12;
		} else {
			zoom = CameraUpdateFactory.zoomTo(15);
			shouldWaitForCallback = (int) getMap().getCameraPosition().zoom != 15;
		}
		
		// Caveat: this method will only be executed if zoom level value has changed.
		getMap().animateCamera(zoom, new CancelableCallback() {
			
			// Notifying listener when map animated camera is done
			
			@Override
			public void onFinish() {
				mapListener.onMapLoadedComplete();
			}
			
			@Override
			public void onCancel() {
				mapListener.onMapLoadedComplete();
				
			}
		});
		
		// Google Map API's animateCamera would only be dispatching a callback event
		// when zoom level value changed, otherwise, onFinish() won't be dispatched.
		// Therefore, we need this boolean check so that we know when to wait and when to not wait for a callback
		if ( shouldWaitForCallback == false ) {
			// notify listener directly, no need to wait for a callback
			mapListener.onMapLoadedComplete();
		}

	}

	/** replaces the temporary replacement marker with the standard marker*/
	public void replaceReplacementMarker() {
		if (replacementMarker != null) {
			suggestedPlaces.add(createMarker(replacementMarker.getPosition(), R.drawable.ic_pin, replacementMarker.getTitle(), ""));
			replacementMarker.remove();
			replacementMarker = null;
		}
	}
	
	public void exitMapSelectionMode() {
		mapListener.onExitMapView();
		suggPlacesList = null;
		clearSuggestedPlaces();
		getMap().setOnMapLongClickListener(new OnMapLongClickListener() {
			@Override
			public void onMapLongClick(LatLng point) {
				OnNewTripListener listener = (OnNewTripListener) getActivity();
				listener.openAddDialog(point);
			}
		});
	}

	@Override
	public void onSuccess(JSONObject successResult) {
		// on successful transit station query
		try {
			JSONArray results = successResult.getJSONArray("results");
			for (int i = 0; i < results.length(); i++) {
				GooglePlace place = GooglePlace.fromJSONObject((JSONObject) results.get(i));
				if (coordToCircles.get(place.getLatitude()+","+place.getLongitude()) == null) {
					// only add circle if it's at a new place
					LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());
					addMultiCircle(latLng, YelpFilterRequest.LOCAL_SEARCH_RADIUS_IN_METERS);
					if (place.hasType(GooglePlace.TYPE_SUBWAY_STATION) || place.hasType(GooglePlace.TYPE_TRAIN_STATION)) {
						createMarker(latLng, R.drawable.ic_subway, place.getName(), "Transit Station");
					} else {
						createMarker(latLng, R.drawable.ic_bus, place.getName(), "Bus Stop");
					}
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

	/**
	 * MapListener - Interface for listening to events related to map fragment
	 */
	public interface MapListener {
		/**
		 * Called after a route query returns successfully
		 * @param durationString	string to display for the duration of the route
		 */
		public void onRouted(String durationString);

		/**
		 * Callback for when map fragment is clicked
		 */
		public void onMapClick();

		/**
		 * Callback for when a marker is clicked
		 * @param tripLocation	trip loaction of the marker
		 */
		public void onMarkerClick(TripLocation tripLocation);

		/**
		 * Callback for when map clears out the suggested points
		 */
		public void onExitMapView();
		
		/**
		 * Callback for when map is completely loaded. 
		 */
		public void onMapLoadedComplete();
		
		/**
		 * Callback for when map loading is cancelled.
		 */
		public void onMapLoadedCancel();
	}
}
