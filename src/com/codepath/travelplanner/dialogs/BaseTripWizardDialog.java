package com.codepath.travelplanner.dialogs;

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
import com.codepath.travelplanner.models.YelpFilterRequest;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

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
	/** whether this is an old trip or not in the bundle */
	protected static final String NEW_TRIP_EXTRA = "adding";

	/** trip object that the wizard is making */
	protected Trip trip = new Trip();

	/**
	 * OnNewTripListener - Interface for listening to events related to making a new trip
	 */
	public interface OnNewTripListener {
		/** called when we want to route the new trip */
		public Trip trip = new Trip();

		/**
		 * Perform route using the trip's info
		 * @param updatedTrip		trip to route
		 */
		public void onRouteListener(Trip updatedTrip);

		/**
		 * When entering map view
		 * @param suggPlacesList	list of suggested places to pin
		 * @param trip				the current trip
		 * @param newTrip			whether or not this is a new trip or should be a waypoint
		 */
		public void enterMapView(ArrayList<TripLocation> suggPlacesList, Trip trip, boolean newTrip);

		/**
		 * Opens the add waypoint dialog
		 * @param location	location to search around
		 */
		public void openAddDialog(LatLng location);

		/**
		 * Gets results around the waypoint location
		 * @param filterRequest		filters to apply to the search
		 */
		public void getAddResults(YelpFilterRequest filterRequest);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View v = inflater.inflate(getDialogResourceId(), null);
		// TODO: Shouldn't this be called in onCreateView(), otherwise,we may get a race condition.
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
