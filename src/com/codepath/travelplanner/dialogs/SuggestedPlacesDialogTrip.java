package com.codepath.travelplanner.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.codepath.travelplanner.R;
import com.codepath.travelplanner.adapters.LocationsAdapter;
import com.codepath.travelplanner.apis.SimpleYelpClient;
import com.codepath.travelplanner.models.LocationFilter;
import com.codepath.travelplanner.models.Trip;
import com.codepath.travelplanner.models.TripLocation;
import natemobiles.app.simpleyelpapiforandroid.interfaces.IRequestListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * SuggestedPlacesDialogTrip - dialog containing the destinations results from the query
 */
public class SuggestedPlacesDialogTrip extends BaseTripWizardDialog implements IRequestListener {
	/** list view of the suggested places */
	private ListView lvSuggPlaces;
	/** suggested places adapter */
	private LocationsAdapter adapter;
	/** list of the trip locations associated with the listview */
	private ArrayList<TripLocation> suggPlacesList = new ArrayList<TripLocation>();
	/** progress bar indicating when suggested places query is loading */
	private ProgressBar pbSuggLoading;

	/** search filter when searching for suggested places */
	private LocationFilter filter;

	/** static function that creates a new suggested places dialog */
	public static SuggestedPlacesDialogTrip newInstance(Trip trip, LocationFilter filter) {
		SuggestedPlacesDialogTrip dialog = new SuggestedPlacesDialogTrip();
		Bundle bundle = new Bundle();
		bundle.putSerializable(TRIP_EXTRA, trip);
		bundle.putSerializable(FILTER_EXTRA, filter);
		dialog.setArguments(bundle);
		return dialog;
	}

	@Override
	protected void setPositiveButton(AlertDialog.Builder builder) {
		// do nothing, this dialog does not want a positive button
	}

	@Override
	protected int getPositiveBtnTextId() {
		return R.string.next;
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		newTrip = (Trip) getArguments().getSerializable(TRIP_EXTRA);
		filter = (LocationFilter) getArguments().getSerializable(FILTER_EXTRA);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		TripLocation start = newTrip.getStart();
		SimpleYelpClient.getRestClient().search(filter.getActivity(), start.getLatitude(), start.getLongitude(), this);
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
				newTrip.addPlace((TripLocation) adapterView.getItemAtPosition(i));
				// go to next page
				dismiss();
				SummaryDialogTrip.newInstance(newTrip, filter).show(getFragmentManager(), "summary");
			}
		});
		pbSuggLoading.setVisibility(View.VISIBLE);
		lvSuggPlaces.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onPositiveClick() {
		// do nothing, this dialog does not have a positive button
	}

	@Override
	protected void onNegativeClick() {
		TripLocation loc = newTrip.getStart();
		FiltersDialogTrip.newInstance("", loc.getLatitude(), loc.getLongitude()).show(getFragmentManager(), "filters");
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
