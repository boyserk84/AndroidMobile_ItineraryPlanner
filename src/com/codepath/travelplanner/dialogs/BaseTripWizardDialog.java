package com.codepath.travelplanner.dialogs;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.codepath.travelplanner.R;
import com.codepath.travelplanner.models.Trip;
import com.codepath.travelplanner.models.TripLocation;

/**
 * BaseTripWizardDialog - base dialog for the create-a-new-trip "wizard" dialogs
 */
public abstract class BaseTripWizardDialog extends DialogFragment {
	/** start place extra */
	protected static final String START_EXTRA = "start";
	/** end place extra */
	protected static final String DESTINATION_EXTRA = "end";
	/** latitude place extra */
	protected static final String LATITUDE_EXTRA = "latitude";
	/** longitude place extra */
	protected static final String LONGITUDE_EXTRA = "longitude";
	/** name of trip in the bundle */
	protected static final String TRIP_EXTRA = "trip";
	/** name of location filter in the bundle */
	protected static final String FILTER_EXTRA = "filter";

	/** trip object that the wizard is making */
	protected Trip newTrip = new Trip();

	/**
	 * OnNewTripListener - Interface for listening to events related to making a new trip
	 */
	public interface OnNewTripListener {
		/** called when we want to route the new trip */
		public void onRouteListener(Trip trip);
		public void enterMapView(ArrayList<TripLocation> suggPlacesList, Trip newTrip);
		public void openConfirmDialog(TripLocation destination, Trip newTrip);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View v = inflater.inflate(getDialogResourceId(), null);
		setupViews(v);

		builder.setView(v);
		setPositiveButton(builder);
		builder.setNegativeButton(getNegativeBtnTextId(), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				onNegativeClick();
			}
		});

		return builder.create();
	}

	/** get the resource id of the layout associated with this dialog */
	protected abstract int getDialogResourceId();

	/** sets up the positive button */
	protected void setPositiveButton(AlertDialog.Builder builder) {
		builder.setPositiveButton(getPositiveBtnTextId(), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				onPositiveClick();
			}
		});
	}

	/** get text id for the positive button */
	protected int getPositiveBtnTextId() {
		return R.string.next;
	};

	/** get text id for the negative button */
	protected int getNegativeBtnTextId() {
		return R.string.back;
	};

	/** setup the views */
	protected abstract void setupViews(View v);

	/** callback for when the positive button is clicked */
	protected abstract void onPositiveClick();

	/** callback for when the negative button is clicked */
	protected abstract void onNegativeClick();
}
