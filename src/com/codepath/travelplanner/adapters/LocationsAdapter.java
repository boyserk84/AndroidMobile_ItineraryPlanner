package com.codepath.travelplanner.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.codepath.travelplanner.R;

import java.util.List;

/**
 * LocationsAdapter - adapter for locations
 * TODO: change "Parcelable" item to location item
 */
public class LocationsAdapter extends ArrayAdapter<Parcelable>{

	/** constructor */
	public LocationsAdapter(Context context, List<Parcelable> locs) {
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
