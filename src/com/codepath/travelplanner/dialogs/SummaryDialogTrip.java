package com.codepath.travelplanner.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.codepath.travelplanner.R;
import com.codepath.travelplanner.models.Trip;

/**
 * SummaryDialogTrip - dialog showing the summary of the itinerary the wizard will create
 */
public class SummaryDialogTrip extends BaseTripWizardDialog {
	/** views */
	private TextView tvSummStart;
	private TextView tvSummEnd;
	private EditText etSummTripName;

	/** static function that creates a new filters dialog */
	public static SummaryDialogTrip newInstance(Trip trip) {
		SummaryDialogTrip dialog = new SummaryDialogTrip();
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
		return R.layout.dialog_summary;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return super.onCreateDialog(savedInstanceState);
	}

	@Override
	protected void onPositiveClick() {
		//listener.onSummaryPositive(); // TODO: send back itinerary data
	}

	@Override
	protected void setupViews(View v) {
		tvSummStart = (TextView) v.findViewById(R.id.tvSummStart);
		tvSummEnd = (TextView) v.findViewById(R.id.tvSummEnd);
		etSummTripName = (EditText) v.findViewById(R.id.etSummTripName);
	}
}
