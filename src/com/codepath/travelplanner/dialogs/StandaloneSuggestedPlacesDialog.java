package com.codepath.travelplanner.dialogs;

import android.os.Bundle;
import android.view.View;
import com.codepath.travelplanner.R;
import com.codepath.travelplanner.models.Trip;
import com.codepath.travelplanner.models.TripLocation;

import java.util.ArrayList;

/**
 * StandaloneSuggestedPlacesDialog - dialog containing suggested places without making a query (places data is passed down from another source)
 */
public class StandaloneSuggestedPlacesDialog extends SuggestedPlacesDialog {

	/** suggested places list that was pre-populated by another source (and therefore do not need to make a query again) */
	private static ArrayList<TripLocation> prePopulatedSuggPlaces = null;

	/** static function that creates a new suggested places dialog */
	public static StandaloneSuggestedPlacesDialog newInstance(Trip trip, ArrayList<TripLocation> suggestedPlaces) {
		StandaloneSuggestedPlacesDialog dialog = new StandaloneSuggestedPlacesDialog();
		Bundle bundle = new Bundle();
		bundle.putSerializable(TRIP_EXTRA, trip);
		dialog.setArguments(bundle);
		prePopulatedSuggPlaces = suggestedPlaces;
		return dialog;
	}

	@Override
	protected void setupViews(View v) {
		super.setupViews(v);
		if (prePopulatedSuggPlaces != null) {
			adapter.addAll(prePopulatedSuggPlaces);
			prePopulatedSuggPlaces = null;
			pbSuggLoading.setVisibility(View.INVISIBLE);
			lvSuggPlaces.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onPositiveClick() {
		dismiss();
	}

	@Override
	protected void onNegativeClick() {
		dismiss();
	}
}
