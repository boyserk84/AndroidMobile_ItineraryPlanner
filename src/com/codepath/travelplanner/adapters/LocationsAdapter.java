package com.codepath.travelplanner.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.codepath.travelplanner.R;
import com.codepath.travelplanner.models.TripLocation;

import java.util.List;

/**
 * LocationsAdapter - adapter for locations
 */
public class LocationsAdapter extends ArrayAdapter<TripLocation>{

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
		// TODO: setup correct values into the view
		return v;
	}
}
