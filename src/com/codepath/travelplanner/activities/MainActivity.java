package com.codepath.travelplanner.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.codepath.travelplanner.R;
import com.codepath.travelplanner.apis.SimpleYelpClient;
import com.codepath.travelplanner.dialogs.BaseTripWizardDialog.OnNewTripListener;
import com.codepath.travelplanner.dialogs.ConfirmDestinationDialog;
import com.codepath.travelplanner.dialogs.FiltersDialogTrip;
import com.codepath.travelplanner.directions.Segment;
import com.codepath.travelplanner.fragments.MyMapFragment;
import com.codepath.travelplanner.fragments.MyMapFragment.MapListener;
import com.codepath.travelplanner.models.Trip;
import com.codepath.travelplanner.models.TripLocation;
import com.codepath.travelplanner.models.YelpFilterRequest;
import com.google.android.gms.maps.model.LatLng;
import natemobiles.app.simpleyelpapiforandroid.interfaces.IRequestListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * MainActivity - main screen
 */
public class MainActivity extends FragmentActivity implements OnNewTripListener, IRequestListener, MapListener {
	/** key for the segments bundle */
	public static final String SEGMENTS = "segments";

	/** details "button" relative layout */
	protected RelativeLayout rlDirDetails;

	/** details text view */
	protected TextView tvDirDetails;
	
	/** Progress bar showing while query is loading.*/
	protected ProgressBar pbQuickFind;
	
	/** Flag determining if query is still loading. */
	protected boolean isQueryLoading = false;
	
	protected boolean newTrip = true;

	/** fragment containing map */
	protected MyMapFragment map;
	/** list of segments in the route */
	public static ArrayList<Segment> segments;
	/** current trip */
	public Trip trip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		map = ((MyMapFragment) getFragmentManager().findFragmentById(R.id.map));
		rlDirDetails = (RelativeLayout) findViewById(R.id.rlDirDetails);
		tvDirDetails = (TextView) findViewById(R.id.tvDirDetails);
		pbQuickFind = (ProgressBar) findViewById(R.id.pbQuickFindLoad);
		pbQuickFind.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onRouteListener(Trip updatedTrip) {
		trip = updatedTrip;
		map.exitMapSelectionMode();
		map.newRoute(trip);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.main, menu);
		return true;
	}
	
	/**
	 * Determining a quick find/recommendation based on what being selected
	 * @param item
	 */
	public void onQuickFindClick(MenuItem item) {
		if ( isQueryLoading == false ) {
			YelpFilterRequest filterRequest = new YelpFilterRequest();
			Location myLoc = map.getMap().getMyLocation();
			if(myLoc == null) {
				return;
			}
			filterRequest.latitude = myLoc.getLatitude();
			filterRequest.longitude = myLoc.getLongitude();
			switch ( item.getItemId() ) {
			case R.id.miQuickRecTour:
				filterRequest.term = "tourist";
				filterRequest.sortType = YelpFilterRequest.SORT_BY_DISTANCE;
				break;

			case R.id.miQuickRecFood:
				filterRequest.term = "restaurants";
				filterRequest.sortType = YelpFilterRequest.SORT_BY_DISTANCE;
				break;

			case R.id.miQuickRecDrink:
				filterRequest.term = "night club";
				filterRequest.sortType = YelpFilterRequest.SORT_BY_DISTANCE;
				break;
			}
			
			isQueryLoading = true;
			pbQuickFind.setVisibility(View.VISIBLE);
			
			newTrip = true;
			SimpleYelpClient.getRestClient().search( filterRequest, this);
		}
	}
	
	@Override
	public void onBackPressed() {
		map.exitMapSelectionMode();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.miNewTrip:
	        	onNewTrip( item );
	            return true;
	        default:
	        	onQuickFindClick( item );
	            return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public void onRouted(String durationString) {
		tvDirDetails.setText(durationString);
		rlDirDetails.setVisibility(View.VISIBLE);
		rlDirDetails.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bottom_in));
	}

	/** Display directions */
	public void onDetails(View v) {
		if(segments != null) {
			Intent i = new Intent(MainActivity.this, DetailsActivity.class);
			i.putExtra(SEGMENTS, segments);
			startActivity(i);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
		}
	}

	/** Callback when new trip button is clicked */
	public void onNewTrip(MenuItem mi) {
		Location myLoc = map.getMap().getMyLocation();
		if(myLoc != null) {
			FiltersDialogTrip.newInstance("", myLoc.getLatitude(), myLoc.getLongitude(), true).show(getFragmentManager(), "filters");
		}
	}

	@Override
	public void enterMapView(ArrayList<TripLocation> suggPlacesList, Trip trip, boolean newTrip) {
		this.trip = trip;
		map.enterMapSelectionMode(suggPlacesList, newTrip);
	}

	@Override
	public void openConfirmDialog(TripLocation destination) {
		ConfirmDestinationDialog.newInstance(trip, destination).show(getFragmentManager(), "confirm");
	}
	
	@Override
	public void openAddDialog(LatLng location) {
		if(trip != null && trip.getPlaces() != null && trip.getPlaces().size() > 1) {
			FiltersDialogTrip.newInstance("", location.latitude, location.longitude, false).show(getFragmentManager(), "filters");
		}
	}
	
	@Override
	public void getAddResults(YelpFilterRequest filterRequest) {
		isQueryLoading = true;
		pbQuickFind.setVisibility(View.VISIBLE);
		newTrip = false;
		SimpleYelpClient.getRestClient().search(filterRequest, this);
	}

	@Override
	public void onSuccess(JSONObject successResult) {
		if(newTrip) {
			trip = new Trip();
			Location myLoc = map.getMap().getMyLocation();
			if(myLoc != null) {
				TripLocation loc = new TripLocation();
				LatLng latLng = new LatLng(myLoc.getLatitude(), myLoc.getLongitude());
				loc.setLatLng(latLng);
				trip.addPlace(loc);
			}
		}
		
		try {
			this.enterMapView( TripLocation.fromJSONArray(successResult.getJSONArray("businesses")) , trip, newTrip);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		isQueryLoading = false;
		pbQuickFind.setVisibility( View.INVISIBLE );		
	}

	@Override
	public void onFailure(JSONObject failureResult) {
		// TODO Auto-generated method stub
		isQueryLoading = false;
	}
}
