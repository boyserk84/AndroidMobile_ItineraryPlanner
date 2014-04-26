package com.codepath.travelplanner.dialogs;

import java.text.DecimalFormat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.travelplanner.R;
import com.codepath.travelplanner.models.Trip;
import com.codepath.travelplanner.models.TripLocation;
import com.codepath.travelplanner.models.YelpFilterRequest;
import com.squareup.picasso.Picasso;

/**
 * Confirmation Dialog - dialog confirming map view selection
 */
public class ConfirmDestinationDialog extends BaseTripWizardDialog {
	private Trip newTrip;
	private TripLocation tripLocation;

	/** static function that creates a new filters dialog */
	public static ConfirmDestinationDialog newInstance(Trip trip, TripLocation tripLocation) {
		ConfirmDestinationDialog dialog = new ConfirmDestinationDialog();
		Bundle bundle = new Bundle();
		bundle.putSerializable(TRIP_EXTRA, trip);
		bundle.putSerializable(DESTINATION_EXTRA, tripLocation);
		dialog.setArguments(bundle);
		return dialog;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		newTrip = (Trip) getArguments().getSerializable(TRIP_EXTRA);
		tripLocation = (TripLocation) getArguments().getSerializable(DESTINATION_EXTRA);
	}

	@Override
	protected int getNegativeBtnTextId() {
		return R.string.cancel;
	};
	
	@Override
	protected int getPositiveBtnTextId() {
		return R.string.done;
	};

	@Override
	protected int getDialogResourceId() {
		return R.layout.dialog_confirm;
	}

	@Override
	protected void setupViews(View v) {
		if (tripLocation != null) {
			DecimalFormat df;
			TextView tvSuggPlaceName = (TextView) v.findViewById(R.id.tvSuggPlaceName);
			tvSuggPlaceName.setText(tripLocation.getLocationName());
			ImageView ivRating = (ImageView) v.findViewById(R.id.ivConfirmImageRating);
			Picasso.with( v.getContext() ).load( tripLocation.getRatingImgUrl() ).into( ivRating );
			
			TextView tvDistNum = (TextView) v.findViewById(R.id.tvDistNum);
			df = new DecimalFormat("#0.00");
			tvDistNum.setText(df.format(tripLocation.getDistance()/YelpFilterRequest.DEFAULT_ONE_MILE_RADIUS_IN_METER));
		} else {
			Log.d("travelIt", "location is null in confirm dialog???");
		}
	}

	@Override
	protected void onPositiveClick() {
		newTrip.addPlace(tripLocation);
		OnNewTripListener listener = (OnNewTripListener) getActivity();
		listener.onRouteListener(newTrip);
	}

	@Override
	protected void onNegativeClick() {
		getDialog().cancel();
	}
}
