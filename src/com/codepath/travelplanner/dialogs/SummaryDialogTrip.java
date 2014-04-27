package com.codepath.travelplanner.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.codepath.travelplanner.R;
import com.codepath.travelplanner.models.Trip;
import com.codepath.travelplanner.models.YelpFilterRequest;

/**
 * SummaryDialogTrip - dialog showing the summary of the itinerary the wizard will create
 */
public class SummaryDialogTrip extends BaseTripWizardDialog {
	/** text view containing start location */
	private TextView tvSummStart;
	/** text view containing end location */
	private TextView tvSummEnd;
	/** edit text for the trip name */
	private EditText etSummTripName;

	/** search filter to pass to the previous dialog if we go back */
	private YelpFilterRequest filter;

	/** static function that creates a new filters dialog */
	public static SummaryDialogTrip newInstance(Trip trip, YelpFilterRequest filter) {
		SummaryDialogTrip dialog = new SummaryDialogTrip();
		Bundle bundle = new Bundle();
		bundle.putSerializable(TRIP_EXTRA, trip);
		bundle.putSerializable(FILTER_EXTRA, filter);
		dialog.setArguments(bundle);
		return dialog;
	}

	@Override
	protected int getPositiveBtnTextId() {
		return R.string.done;
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		trip = (Trip) getArguments().getSerializable(TRIP_EXTRA);
		filter = (YelpFilterRequest) getArguments().getSerializable(FILTER_EXTRA);
	}

	@Override
	protected int getDialogResourceId() {
		return R.layout.dialog_summary;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return super.onCreateDialog(savedInstanceState);
	}

	@Override
	protected void onPositiveClick() {
		// route
		OnNewTripListener listener = (OnNewTripListener) getActivity();
		listener.onRouteListener(trip);
	}

	@Override
	protected void onNegativeClick() {
		SuggestedPlacesDialogTrip.newInstance(trip, filter).show(getFragmentManager(), "destinations");
	}

	@Override
	protected void setupViews(View v) {
		tvSummStart = (TextView) v.findViewById(R.id.tvSummStart);
		tvSummEnd = (TextView) v.findViewById(R.id.tvSummEnd);
		etSummTripName = (EditText) v.findViewById(R.id.etSummTripName);
		String startLocationName = "Current Location";
		String endLocationName = trip.getEnd().getLocationName();
		tvSummStart.setText(startLocationName);
		tvSummEnd.setText(endLocationName);
		etSummTripName.setHint("Trip to " + endLocationName);
	}
}
