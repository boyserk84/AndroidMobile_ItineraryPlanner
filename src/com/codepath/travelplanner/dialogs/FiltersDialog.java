package com.codepath.travelplanner.dialogs;

import java.util.ArrayList;

import natemobiles.app.simpleyelpapiforandroid.interfaces.IRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.travelplanner.R;
import com.codepath.travelplanner.apis.SimpleYelpClient;
import com.codepath.travelplanner.models.TripLocation;
import com.codepath.travelplanner.models.YelpFilterRequest;
import com.google.android.gms.maps.model.LatLng;

/**
 * FiltersDialog - dialog containing the filters (eg. activity, distance, price, etc)
 */
public class FiltersDialog extends BaseTripWizardDialog implements IRequestListener{
	/** edit text containing the activity type user wants to search for (eg. food, museum, etc) */
	private EditText etActivity;
	/** spinner containing distances to filter by */
	private Spinner spDistances;
	/** spinner containing prices to filter by */
	private Spinner spPrices;
	
	////////////////////////////
	/// Quick Recommendation(s)
	///////////////////////////
	
	/** Button for quick recommendation to restaurants */
	private Button btnFood;
	
	/** Button for quick recommendation to tourist area/land mark*/
	private Button btnTour;
	
	/** Button for quick recommendation to nightlife/clubs/getting drunk*/
	private Button btnNightlife;
	
	/////////////////////////
	// Internal data fields
	/////////////////////////
	
	private YelpFilterRequest filterRequest;
	private boolean newTrip;
	

	/** static function that creates a new filters dialog */
	public static FiltersDialog newInstance(String start, double latitude, double longitude, boolean newTrip) {
		FiltersDialog dialog = new FiltersDialog();
		Bundle bundle = new Bundle();
		bundle.putString(START_EXTRA, start);
		bundle.putDouble(LATITUDE_EXTRA, latitude);
		bundle.putDouble(LONGITUDE_EXTRA, longitude);
		bundle.putBoolean(NEW_TRIP_EXTRA, newTrip);
		dialog.setArguments(bundle);
		return dialog;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String startQueryStr = getArguments().getString(START_EXTRA);
		
		// Create a dummy YelpFilterRequest
		filterRequest = new YelpFilterRequest();
		
		newTrip = getArguments().getBoolean(NEW_TRIP_EXTRA);
		// TODO: search for "start" instead of using current position
		startQueryStr = "";
		if(!newTrip) {
			filterRequest.latitude = getArguments().getDouble(LATITUDE_EXTRA);
			filterRequest.longitude = getArguments().getDouble(LONGITUDE_EXTRA);
			filterRequest.radius = YelpFilterRequest.LOCAL_SEARCH_RADIUS_IN_METERS;
		} else if (startQueryStr.length() > 0) {
			YelpFilterRequest filter = new YelpFilterRequest();
			filter.latitude = getArguments().getDouble(LATITUDE_EXTRA);
			filter.longitude = getArguments().getDouble(LONGITUDE_EXTRA);
			SimpleYelpClient.getRestClient().search(filter, this);
		} else {
			TripLocation loc = new TripLocation();
			LatLng latLng = new LatLng(getArguments().getDouble(LATITUDE_EXTRA), getArguments().getDouble(LONGITUDE_EXTRA));
			loc.setLatLng(latLng);
			trip.addPlace(loc);
		}
		
		
	}

	@Override
	protected int getNegativeBtnTextId() {
		return R.string.cancel;
	};

	@Override
	protected int getDialogResourceId() {
		return R.layout.dialog_filters;
	}

	@Override
	protected void setupViews(View v) {
		etActivity = (EditText) v.findViewById(R.id.etActivity);
		//spDistances = (Spinner) v.findViewById(R.id.spDistances);
		//spPrices = (Spinner) v.findViewById(R.id.spPrices);
		
		btnFood = (Button) v.findViewById(R.id.btnFood);
		btnNightlife = (Button) v.findViewById( R.id.btnNightlife );
		btnTour = (Button) v.findViewById( R.id.btnTour );
		
		// Listener for click for quick recommendation
		OnClickListener clickListener = new OnClickListener() {

			// Provide a quick recommendation
			@Override
			public void onClick(View v) {
				switch ( v.getId() ) {
				case R.id.btnFood:
					filterRequest.term = "restaurant";
					break;

				case R.id.btnNightlife:
					filterRequest.term = "night club";
					break;

				case R.id.btnTour:
					filterRequest.term = "tour";
					break;

				default:

					break;
				}
				dismiss();
				if(newTrip) {
					updateFilterRequestWithCurrentLocation();
					SuggestedPlacesDialog.newInstance(trip, filterRequest).show(getFragmentManager(), "destinations");
				}
				else {
					OnNewTripListener listener = (OnNewTripListener) getActivity();
					listener.getAddResults(filterRequest);
				}

			}
		};
		
		btnFood.setOnClickListener( clickListener );
		btnNightlife.setOnClickListener( clickListener );
		btnTour.setOnClickListener( clickListener );
	}

	@Override
	protected void onPositiveClick() {
		filterRequest.term = etActivity.getText().toString();
		
		if(newTrip) {
			updateFilterRequestWithCurrentLocation();
			// TODO: add other filters
			SuggestedPlacesDialog.newInstance(trip, filterRequest).show(getFragmentManager(), "destinations");
		}
		else {		
			OnNewTripListener listener = (OnNewTripListener) getActivity();
			listener.getAddResults(filterRequest);
		}
	}

	@Override
	protected void onNegativeClick() {
	}
	
	
	/**
	 * Update filter Request object with the current location (long/lat)
	 */
	private void updateFilterRequestWithCurrentLocation() {
		if ( filterRequest != null && trip != null ) {
			filterRequest.latitude = trip.getStart().getLatLng().latitude;
			filterRequest.longitude = trip.getStart().getLatLng().longitude;
		}
	}

	//////////////////////////////////
	/// Callback from Yelp Api calls
	//////////////////////////////////

	@Override
	public void onSuccess(JSONObject successResult) {
		try {
			ArrayList<TripLocation> places = TripLocation.fromJSONArray( successResult.getJSONArray("businesses") );
			if (places.size() > 0) {
				trip.addPlace(places.get(0));
			} else {
				// TODO handle case when we couldn't find a start destination
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onFailure(JSONObject failureResult) {
		// TODO Auto-generated method stub
	}
}
