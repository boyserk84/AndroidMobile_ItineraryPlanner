package com.codepath.travelplanner.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.codepath.travelplanner.R;

import java.io.Serializable;

/**
 * SummaryDialog - dialog showing the summary of the itinerary the wizard will create
 */
public class SummaryDialog extends BaseWizardDialog {
	/** name of itinerary in the bundle */
	private static final String ITINERARY_EXTRA = "itinerary";

	/** views */
	private TextView tvSummStart;
	private TextView tvSummEnd;
	private EditText etSummTripName;

	/** summary of itinerary to show */
	private Serializable itinerary; // TODO: change to actual model

	/** static function that creates a new filters dialog */
	public static SummaryDialog newInstance(Serializable it) {
		SummaryDialog dialog = new SummaryDialog();
		Bundle args = new Bundle();
		args.putSerializable(ITINERARY_EXTRA, it);
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
		itinerary = getArguments().getSerializable(ITINERARY_EXTRA);
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
		listener.onSummaryPositive(); // TODO: send back itinerary data
	}

	@Override
	protected void setupViews(View v) {
		tvSummStart = (TextView) v.findViewById(R.id.tvSummStart);
		tvSummEnd = (TextView) v.findViewById(R.id.tvSummEnd);
		etSummTripName = (EditText) v.findViewById(R.id.etSummTripName);
	}
}
