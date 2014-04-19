package com.codepath.travelplanner.dialogs;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import com.codepath.travelplanner.R;
import com.codepath.travelplanner.models.TripLocation;

/**
 * FiltersDialogTrip - dialog containing the filters (eg. activity, distance, price, etc)
 */
public class FiltersDialogTrip extends BaseTripWizardDialog {
	/** views */
	private EditText etActivity;
	private Spinner spDistances;
	private Spinner spPrices;

	/** static function that creates a new filters dialog */
	public static FiltersDialogTrip newInstance(String start) {
		FiltersDialogTrip dialog = new FiltersDialogTrip();
		Bundle bundle = new Bundle();
		bundle.putString(START_EXTRA, start);
		dialog.setArguments(bundle);
		return dialog;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TripLocation tripStart = new TripLocation();

		//newTrip. = getArguments().getParcelableArrayList(START_EXTRA);
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
		spDistances = (Spinner) v.findViewById(R.id.spDistances);
		spPrices = (Spinner) v.findViewById(R.id.spPrices);
	}

	@Override
	protected void onPositiveClick() {
		// TODO: make query for destination results

		// Hack: Test query to yelp API
//		Double latitude = start.latitude;
//		Double longitude = start.longitude;
//		SimpleYelpClient.getRestClient().search("restaurant", latitude, longitude, this);
		// End of Hack

		// TODO: Can't this array be Serializable

		SuggestedPlacesDialogTrip.newInstance(newTrip).show(getFragmentManager(), "destinations");
	}
}
