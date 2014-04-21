package com.codepath.travelplanner.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import com.codepath.travelplanner.R;
import com.codepath.travelplanner.dialogs.FiltersDialogTrip;
import com.codepath.travelplanner.directions.Segment;
import com.codepath.travelplanner.fragments.MyMapFragment;
import com.codepath.travelplanner.models.Trip;
import com.codepath.travelplanner.models.TripLocation;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity{

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
		//map.createCircle(new LatLng(37.765240, -122.409432), 1000);
	}
	
	protected Trip generateDummyTrip() {
		Trip t = new Trip();
		ArrayList<TripLocation> tlList = new ArrayList<TripLocation>();
		
		TripLocation tl = new TripLocation();
		tl.setLatLng(map.getLatLngFromAddress("699 8th Street, San Francisco, CA"));
		tl.setLocationName("Zynga HQ");
		tl.setDescription("Games are made here");
		tl.setRating(3.5);
		tlList.add(tl);
		
		tl = new TripLocation();
		tl.setLatLng(map.getLatLngFromAddress("Golden Gate Bridge, San Francisco, CA"));
		tl.setLocationName("Golden Gate Bridge");
		tl.setDescription("It's a bridge!!!");
		tl.setRating(4.5);
		tlList.add(tl);
		
		t.setPlaces(tlList);
		
		return t;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.main, menu);
		return true;
	}
	
	/** Display directions */
	public void onDetails(View v) {
		if(segments != null) {
			Intent i = new Intent(MainActivity.this, DetailsActivity.class);
			i.putExtra(SEGMENTS, segments); //this isnt serializing so commenting out for now
			startActivity(i);
		}
	}

	/** callback when new trip button is clicked */
	public void onNewTrip(MenuItem mi) {
		Location myLoc = map.getMap().getMyLocation();
		FiltersDialogTrip.newInstance("", myLoc.getLatitude(), myLoc.getLongitude()).show(getFragmentManager(), "filters");
	}
}
