package com.codepath.travelplanner.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.codepath.travelplanner.R;
import com.codepath.travelplanner.adapters.LocationsAdapter;
import com.codepath.travelplanner.apis.SimpleYelpClient;
import com.codepath.travelplanner.helpers.Util;
import com.codepath.travelplanner.models.Trip;
import com.codepath.travelplanner.models.TripLocation;
import com.codepath.travelplanner.models.YelpFilterRequest;
import natemobiles.app.simpleyelpapiforandroid.interfaces.IRequestListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * SuggestedPlacesDialog - dialog containing the destinations results from the query
 */
public class SuggestedPlacesDialog extends BaseTripWizardDialog implements IRequestListener {
	/** list view of the suggested places */
	protected ListView lvSuggPlaces;
	/** suggested places adapter */
	protected LocationsAdapter adapter;
	/** list of the trip locations associated with the listview */
	protected ArrayList<TripLocation> suggPlacesList = new ArrayList<TripLocation>();
	/** progress bar indicating when suggested places query is loading */
	protected ProgressBar pbSuggLoading;

	/** search filter when searching for suggested places */
	protected YelpFilterRequest filter = null;

	/** static function that creates a new suggested places dialog */
	public static SuggestedPlacesDialog newInstance(Trip trip, YelpFilterRequest filter) {
		SuggestedPlacesDialog dialog = new SuggestedPlacesDialog();
		Bundle bundle = new Bundle();
		bundle.putSerializable(TRIP_EXTRA, trip);
		bundle.putSerializable(FILTER_EXTRA, filter);
		dialog.setArguments(bundle);
		return dialog;
	}

	@Override
	protected int getPositiveBtnTextId() {
		return R.string.map_view;
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		trip = (Trip) getArguments().getSerializable(TRIP_EXTRA);
		if (getArguments().containsKey(FILTER_EXTRA)) {
			filter = (YelpFilterRequest) getArguments().getSerializable(FILTER_EXTRA);
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		if (filter != null) {
			SimpleYelpClient.getRestClient().search(filter, this);
		}
		return super.onCreateDialog(savedInstanceState);
	}

	@Override
	protected int getDialogResourceId() {
		return R.layout.dialog_suggested_places;
	}

	@Override
	protected void setupViews(View v) {
		pbSuggLoading = (ProgressBar) v.findViewById(R.id.pbSuggLoading);
		lvSuggPlaces = (ListView) v.findViewById(R.id.lvSuggPlaces);
		adapter = new LocationsAdapter(getActivity(), suggPlacesList);
		lvSuggPlaces.setAdapter(adapter);
		lvSuggPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				// add the selected item to the trip's places list
				TripLocation tripLocation = (TripLocation) adapterView.getItemAtPosition(i);
				tripLocation.setLatLng(Util.getLatLngFromAddress(tripLocation.getAddress().toString(), getActivity()));
				trip.addPlace(tripLocation);
				OnNewTripListener listener = (OnNewTripListener) getActivity();
				listener.onRouteListener(trip);
				dismiss();
			}
		});
		pbSuggLoading.setVisibility(View.VISIBLE);
		lvSuggPlaces.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onPositiveClick() {
		//go to map view
		OnNewTripListener listener = (OnNewTripListener) getActivity();
		listener.enterMapView(suggPlacesList, trip, true);
	}

	@Override
	protected void onNegativeClick() {
		if ( trip != null ) {
			TripLocation loc = trip.getStart();
			if ( loc.getLatLng() != null ) {
				FiltersDialog.newInstance("", loc.getLatLng().latitude, loc.getLatLng().longitude, true).show(getFragmentManager(), "filters");
			}
		}
	}

	@Override
	public void onSuccess(JSONObject successResult) {
		try {
			pbSuggLoading.setVisibility(View.INVISIBLE);
			lvSuggPlaces.setVisibility(View.VISIBLE);
			adapter.addAll(TripLocation.fromJSONArray(successResult.getJSONArray("businesses")));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFailure(JSONObject failureResult) {
		// TODO: handle failure case
	}
}
