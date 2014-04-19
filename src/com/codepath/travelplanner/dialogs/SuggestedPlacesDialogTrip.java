package com.codepath.travelplanner.dialogs;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.codepath.travelplanner.R;
import com.codepath.travelplanner.adapters.LocationsAdapter;
import com.codepath.travelplanner.models.Trip;
import com.codepath.travelplanner.models.TripLocation;

import java.util.ArrayList;

/**
 * SuggestedPlacesDialogTrip - dialog containing the destinations results from the query
 */
public class SuggestedPlacesDialogTrip extends BaseTripWizardDialog {
	/** views */
	private ListView lvSuggPlaces;
	private LocationsAdapter adapter;
	private ArrayList<TripLocation> suggPlacesList = new ArrayList<TripLocation>();

	/** static function that creates a new suggested places dialog */
	public static SuggestedPlacesDialogTrip newInstance(Trip trip) {
		SuggestedPlacesDialogTrip dialog = new SuggestedPlacesDialogTrip();
		Bundle args = new Bundle();
		args.putSerializable(TRIP_EXTRA, trip);
		dialog.setArguments(args);
		return dialog;
	}

	@Override
	protected int getPositiveBtnTextId() {
		return R.string.done;
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		newTrip = (Trip) getArguments().getSerializable(TRIP_EXTRA);
	}

	@Override
	protected int getDialogResourceId() {
		return R.layout.dialog_suggested_places;
	}

	@Override
	protected void setupViews(View v) {
		lvSuggPlaces = (ListView) v.findViewById(R.id.lvSuggPlaces);
		adapter = new LocationsAdapter(getActivity(), suggPlacesList);
		lvSuggPlaces.setAdapter(adapter);
	}

	@Override
	protected void onPositiveClick() {
		SummaryDialogTrip.newInstance(null).show(getFragmentManager(), "summary");
	}
}
