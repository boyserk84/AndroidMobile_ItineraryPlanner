package com.codepath.travelplanner;

import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.codepath.travelplanner.dialogs.SuggestedPlacesDialog;
import com.codepath.travelplanner.dialogs.FiltersDialog;
import com.codepath.travelplanner.dialogs.SummaryDialog;
import com.codepath.travelplanner.helpers.OnPositiveListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements OnPositiveListener {
	/** views */
	ImageButton ibtnNewTrip;
	EditText etNewTrip;

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.main, menu);
		MenuItem mi = menu.findItem(R.id.miNewTrip);
		View v = mi.getActionView();
		ibtnNewTrip = (ImageButton) v.findViewById(R.id.ibtnNewTrip);
		etNewTrip = (EditText) v.findViewById(R.id.etNewTrip);
		setupMenuItemListeners();
		return true;
	}

	/** setup listeners for the menu item */
	private void setupMenuItemListeners() {
		etNewTrip.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					// make query after "search" button clicked on keyboard
					onNewTrip();
					return true;
				}
				return false;
			}
		});
		ibtnNewTrip.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onNewTrip();
			}
		});
	}

	@Override
	public void onFilterPositive() {
		// TODO: make query for destination results
		SuggestedPlacesDialog.newInstance(new ArrayList<Parcelable>()).show(getFragmentManager(), "destinations");
	}

	@Override
	public void onSuggestedPlacesPositive() {
		SummaryDialog.newInstance(null).show(getFragmentManager(), "summary");
	}

	/** callback when new trip button is clicked */
	public void onNewTrip() {
		FiltersDialog.newInstance().show(getFragmentManager(), "filters");
	}

	@Override
	public void onSummaryPositive() {
		// TODO: map out the route
	}
}
