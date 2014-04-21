package com.codepath.travelplanner.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.codepath.travelplanner.R;
import com.codepath.travelplanner.models.TripLocation;

import java.text.DecimalFormat;
import java.util.List;

/**
 * LocationsAdapter - adapter for list of trip locations
 */
public class LocationsAdapter extends ArrayAdapter<TripLocation>{
	/** number of meters in one mile */
	private static final double NUM_METERS_PER_MILE = 1609.34;

	/** constructor */
	public LocationsAdapter(Context context, List<TripLocation> locs) {
			super(context, 0, locs);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			v = inflater.inflate(R.layout.location_item, null);
		}
		TripLocation tripLoc = getItem(position);
		if (tripLoc != null) {
			DecimalFormat df;
			TextView tvSuggPlaceName = (TextView) v.findViewById(R.id.tvSuggPlaceName);
			tvSuggPlaceName.setText(tripLoc.getLocationName());
			TextView tvRatingNum = (TextView) v.findViewById(R.id.tvRatingNum);
			df = new DecimalFormat("#.#");
			tvRatingNum.setText(df.format(tripLoc.getRating()));
			TextView tvDistNum = (TextView) v.findViewById(R.id.tvDistNum);
			df = new DecimalFormat("#0.00");
			tvDistNum.setText(df.format(tripLoc.getDistance()/NUM_METERS_PER_MILE));
		} else {
			Log.d("travelIt", "LocationsAdapter.getView error -- triplocation is null, pos = " + position);
		}
		return v;
	}
}
