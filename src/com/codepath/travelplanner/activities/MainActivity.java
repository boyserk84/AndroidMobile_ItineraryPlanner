package com.codepath.travelplanner.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.codepath.travelplanner.R;
import com.codepath.travelplanner.dialogs.BaseTripWizardDialog.OnNewTripListener;
import com.codepath.travelplanner.dialogs.ConfirmDestinationDialog;
import com.codepath.travelplanner.dialogs.FiltersDialogTrip;
import com.codepath.travelplanner.directions.Segment;
import com.codepath.travelplanner.fragments.MyMapFragment;
import com.codepath.travelplanner.models.Trip;
import com.codepath.travelplanner.models.TripLocation;
import com.codepath.travelplanner.models.YelpFilterRequest;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * MainActivity - main screen
 */
public class MainActivity extends FragmentActivity implements OnNewTripListener {
	/** key for the segments bundle */
	public static final String SEGMENTS = "segments";

	/** details button */
	protected Button btnDetails;

	/** fragment containing map */
	protected MyMapFragment map;
	/** list of segments in the route */
	public static ArrayList<Segment> segments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		map = ((MyMapFragment) getFragmentManager().findFragmentById(R.id.map));
		btnDetails = (Button) findViewById(R.id.btnDetails);
	}

	@Override
	public void onRouteListener(Trip trip) {
		map.exitMapSelectionMode();
		map.newRoute(trip);
		map.createCircle(new LatLng(trip.getEnd().getLatLng().latitude,
				trip.getEnd().getLatLng().longitude), YelpFilterRequest.DEFAULT_ONE_MILE_RADIUS_IN_METER/2);
		btnDetails.setVisibility(View.VISIBLE);
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
			i.putExtra(SEGMENTS, segments);
			startActivity(i);
		}
	}

	/** callback when new trip button is clicked */
	public void onNewTrip(MenuItem mi) {
		Location myLoc = map.getMap().getMyLocation();
		if(myLoc != null) {
			FiltersDialogTrip.newInstance("", myLoc.getLatitude(), myLoc.getLongitude()).show(getFragmentManager(), "filters");
		}
	}

	@Override
	public void enterMapView(ArrayList<TripLocation> suggPlacesList, Trip newTrip) {
		map.enterMapSelectionMode(suggPlacesList, newTrip);
	}

	@Override
	public void openConfirmDialog(TripLocation destination, Trip newTrip) {
		ConfirmDestinationDialog.newInstance(newTrip, destination).show(getFragmentManager(), "confirm");
	}
}
