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
import android.widget.ProgressBar;
import android.widget.Toast;
import com.codepath.travelplanner.R;
import com.codepath.travelplanner.apis.SimpleYelpClient;
import com.codepath.travelplanner.dialogs.BaseTripWizardDialog.OnNewTripListener;
import com.codepath.travelplanner.dialogs.ConfirmDestinationDialog;
import com.codepath.travelplanner.dialogs.FiltersDialogTrip;
import com.codepath.travelplanner.directions.Segment;
import com.codepath.travelplanner.fragments.MyMapFragment;
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
public class MainActivity extends FragmentActivity implements OnNewTripListener, IRequestListener {
	/** key for the segments bundle */
	public static final String SEGMENTS = "segments";

	/** details button */
	protected Button btnDetails;
	
	/** Progress bar showing while query is loading.*/
	protected ProgressBar pbQuickFind;
	
	/** Flag determining if query is still loading. */
	protected boolean isQueryLoading = false;

	/** fragment containing map */
	protected MyMapFragment map;
	/** list of segments in the route */
	public static ArrayList<Segment> segments;
	/** current trip */
	public static Trip trip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		map = ((MyMapFragment) getFragmentManager().findFragmentById(R.id.map));
		btnDetails = (Button) findViewById(R.id.btnDetails);
		pbQuickFind = (ProgressBar) findViewById(R.id.pbQuickFindLoad);
		pbQuickFind.setVisibility(View.INVISIBLE);
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

			SimpleYelpClient.getRestClient().search( filterRequest, this);
		}
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
	
	@Override
	public void openAddDialog(LatLng location) {
		Toast.makeText(this, "V2 support coming soon", Toast.LENGTH_LONG).show();
		//FiltersDialogTrip.newInstance("", location.latitude, location.longitude).show(getFragmentManager(), "filters");
	}

	@Override
	public void onSuccess(JSONObject successResult) {
		Trip newTrip = new Trip();
		Location myLoc = map.getMap().getMyLocation();
		if(myLoc != null) {
			TripLocation loc = new TripLocation();
			LatLng latLng = new LatLng(myLoc.getLatitude(), myLoc.getLongitude());
			loc.setLatLng(latLng);
			newTrip.addPlace(loc);
		}
		
		try {
			this.enterMapView( TripLocation.fromJSONArray(successResult.getJSONArray("businesses")) , newTrip );
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
