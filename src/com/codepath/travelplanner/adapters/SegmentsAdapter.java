package com.codepath.travelplanner.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.travelplanner.R;
import com.codepath.travelplanner.directions.Segment;
import com.squareup.picasso.Picasso;

public class SegmentsAdapter extends ArrayAdapter<Segment> {
	public SegmentsAdapter(Context context, ArrayList<Segment> segments) {
		super(context, 0, segments);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		Segment segment = getItem(position);
		
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.item_segment, null);
		}
        
		// Lookup views within item layout
		TextView tvDirection = (TextView) convertView.findViewById(R.id.tvDirection);
		TextView tvDistance = (TextView) convertView.findViewById(R.id.tvDistance);
		ImageView ivDirection = (ImageView) convertView.findViewById(R.id.ivDirection);
        
		// Populate the data into the template view using the data object
		tvDirection.setText(segment.getInstruction());
		tvDistance.setText(segment.getLength());
		if(segment.getIconURL() != null) {
			Picasso.with(getContext()).load(segment.getIconURL()).into(ivDirection);
		}
		else {
			ivDirection.setImageResource(segment.getIcon());
		}
        
		// Return the completed view to render on screen
		return convertView;
	}
}

