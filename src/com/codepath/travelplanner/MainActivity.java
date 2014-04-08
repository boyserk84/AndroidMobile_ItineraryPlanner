package com.codepath.travelplanner;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		GoogleMap map = ((MyMapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();
		
		map.getUiSettings().setRotateGesturesEnabled(false);
		map.getUiSettings().setZoomControlsEnabled(false);
                
        LatLng sydney = new LatLng(-33.867, 151.206);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        map.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney));
	}
	
	public void onNewTrip(View v) {
		DialogFragment newFragment = new InputDialog();
	    newFragment.show(getFragmentManager(), "new_trip");
	}

}
