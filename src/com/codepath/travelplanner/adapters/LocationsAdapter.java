package com.codepath.travelplanner.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.codepath.travelplanner.R;
import com.codepath.travelplanner.models.TripLocation;
import com.codepath.travelplanner.models.YelpFilterRequest;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

/**
 * LocationsAdapter - adapter for list of trip locations
 */
public class LocationsAdapter extends ArrayAdapter<TripLocation> {

	/** constructor */
	public LocationsAdapter(Context context, List<TripLocation> locs) {
			super(context, 0, locs);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			v = inflater.inflate(R.layout.item_location, null);
		}
		TripLocation tripLoc = getItem(position);
		if (tripLoc != null) {
			DecimalFormat df;
			TextView tvSuggPlaceName = (TextView) v.findViewById(R.id.tvSuggPlaceName);
			tvSuggPlaceName.setText(tripLoc.getLocationName());
			
			ImageView ivRating = (ImageView)v.findViewById(R.id.ivRating ); 
			Picasso.with( getContext() ).load( tripLoc.getRatingImgUrl() ).into( ivRating );
			
			ImageView ivLocationImage = (ImageView) v.findViewById( R.id.ivImgLocation );
			Picasso.with( getContext() ).load( tripLoc.getImageUrl() ).into(ivLocationImage);

			TextView tvDistNum = (TextView) v.findViewById(R.id.tvDistNum);
			df = new DecimalFormat("#0.00");
			tvDistNum.setText(df.format(tripLoc.getDistance()/YelpFilterRequest.DEFAULT_ONE_MILE_RADIUS_IN_METER));
		} else {
			Log.d("travelIt", "LocationsAdapter.getView error -- triplocation is null, pos = " + position);
		}
		return v;
	}
}
