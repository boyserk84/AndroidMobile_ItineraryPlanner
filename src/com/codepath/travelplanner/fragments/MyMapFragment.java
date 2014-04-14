package com.codepath.travelplanner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.MapFragment;

public class MyMapFragment extends MapFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View mapView = super.onCreateView(inflater, container, savedInstanceState);   

	    // Get the button view 
	    View locationButton = ((View) mapView.findViewById(1).getParent()).findViewById(2);

	    // and next place it, for exemple, on bottom right (as Google Maps app)
	    RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
	    // position on right bottom
	    rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
	    rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
	    rlp.setMargins(0, 0, 30, 30);
	    
	    return mapView;
	}
}
