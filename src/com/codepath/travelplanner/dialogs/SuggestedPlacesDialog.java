package com.codepath.travelplanner.dialogs;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ListView;
import com.codepath.travelplanner.R;
import com.codepath.travelplanner.adapters.LocationsAdapter;

import java.util.ArrayList;

/**
 * SuggestedPlacesDialog - dialog containing the destinations results from the query
 */
public class SuggestedPlacesDialog extends BaseWizardDialog {
	/** name of places argument in the bundle */
	private static final String PLACES_EXTRA = "places";

	/** views */
	private ListView lvSuggPlaces;
	private LocationsAdapter adapter;
	private ArrayList<Parcelable> suggPlacesList = new ArrayList<Parcelable>();

	/** static function that creates a new suggested places dialog */
	public static SuggestedPlacesDialog newInstance(ArrayList<Parcelable> places) {
		SuggestedPlacesDialog dialog = new SuggestedPlacesDialog();
		Bundle args = new Bundle();
		args.putParcelableArrayList(PLACES_EXTRA, places);
		dialog.setArguments(args);
		return dialog;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		suggPlacesList = getArguments().getParcelableArrayList(PLACES_EXTRA);
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
		listener.onSuggestedPlacesPositive(); // TODO: pass data back
	}
}
