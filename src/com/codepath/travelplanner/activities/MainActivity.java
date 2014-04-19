package com.codepath.travelplanner.activities;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.codepath.travelplanner.R;
import com.codepath.travelplanner.dialogs.FiltersDialog;
import com.codepath.travelplanner.dialogs.SuggestedPlacesDialog;
import com.codepath.travelplanner.dialogs.SummaryDialog;
import com.codepath.travelplanner.directions.Segment;
import com.codepath.travelplanner.fragments.MyMapFragment;
import com.codepath.travelplanner.helpers.OnPositiveListener;
import com.codepath.travelplanner.models.Trip;
import com.codepath.travelplanner.models.TripLocation;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends FragmentActivity implements OnPositiveListener {
	/** views */
	ImageButton ibtnNewTrip;
	EditText etNewTrip;

	protected MyMapFragment map;
	public static ArrayList<Segment> segments;
    
	public static final String SEGMENTS = "segments";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		map = ((MyMapFragment) getFragmentManager()
				.findFragmentById(R.id.map));
		
		Trip t = generateDummyTrip();
		
		map.newRoute(t);
		
		map.createCircle(new LatLng(37.765240, -122.409432), 1000);
	}
	
	protected Trip generateDummyTrip() {
		Trip t = new Trip();
		ArrayList<TripLocation> tlList = new ArrayList<TripLocation>();
		
		TripLocation tl = new TripLocation();
		tl.setLatLng(new LatLng(37.765240, -122.409432));
		tl.setLocationName("Franklin Square");
		tl.setDescription("Pretty cool place to hangout.");
		tl.setRating(4);
		tlList.add(tl);
		
		tl = new TripLocation();
		tl.setLatLng(new LatLng(37.770379, -122.404110));
		tl.setLocationName("Zynga HQ");
		tl.setDescription("Games are made here");
		tl.setRating(3.5);
		tlList.add(tl);
		
		t.setPlaces(tlList);
		
		return t;
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
	
	/** Display directions */
	public void onDetails(View v) {
		if(segments != null) {
			Intent i = new Intent(MainActivity.this, DetailsActivity.class);
			//i.putExtra(SEGMENTS, segments); //this isnt serializing so commenting out for now
			startActivity(i);
		}
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
