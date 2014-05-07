package com.codepath.travelplanner.activities;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.codepath.travelplanner.dialogs.StandaloneSuggestedPlacesDialog;
import natemobiles.app.simpleyelpapiforandroid.interfaces.IRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.travelplanner.R;
import com.codepath.travelplanner.apis.SimpleYelpClient;
import com.codepath.travelplanner.dialogs.BaseTripWizardDialog.OnNewTripListener;
import com.codepath.travelplanner.dialogs.FiltersDialog;
import com.codepath.travelplanner.directions.Segment;
import com.codepath.travelplanner.fragments.MyMapFragment;
import com.codepath.travelplanner.fragments.MyMapFragment.MapListener;
import com.codepath.travelplanner.helpers.Util;
import com.codepath.travelplanner.models.Trip;
import com.codepath.travelplanner.models.TripLocation;
import com.codepath.travelplanner.models.YelpFilterRequest;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

/**
 * MainActivity - main screen
 */
public class MainActivity extends FragmentActivity implements OnNewTripListener, IRequestListener, MapListener {
	/** key for the segments bundle */
	public static final String SEGMENTS = "segments";

	/** layout that holds a preview of the info about the route */
	protected RelativeLayout rlDirPreview;
	/** details text view */
	protected TextView tvDirPreview;

	/** layout containing details of the marker that was clicked */
	protected LinearLayout llMarkerDetail;
	/** views inside the marker detail layout */
	protected TextView tvSuggPlaceName;
	protected ImageView ivRating;
	protected ImageView ivLocImg;
	protected TextView tvDistNum;
	protected Button btnMarkerRouteGo;

	/** list of suggested places a yelp query returned */
	protected ArrayList<TripLocation> suggestedPlaces;

	/** list view menu item */
	protected MenuItem miListView;
	
	/** Progress bar showing while query is loading.*/
	protected ProgressBar pbQuickFind;

	/** If true then there is a pathed route currently on the map. */
	protected boolean isRouteActive = false;
	
	/** Flag determining if query is still loading. */
	protected boolean isQueryLoading = false;

	/** if true, show new trip */
	protected boolean newTrip = true;

	/** fragment containing map */
	protected MyMapFragment map;

	/** list of segments in the route */
	public static ArrayList<Segment> segments;

	/** current trip */
	public Trip trip;

	/** trip location associated with the marker that was clicked */
	public TripLocation markerTripLocation = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		map = ((MyMapFragment) getFragmentManager().findFragmentById(R.id.map));
		rlDirPreview = (RelativeLayout) findViewById(R.id.rlDirPreview);
		tvDirPreview = (TextView) findViewById(R.id.tvDirPreview);
		pbQuickFind = (ProgressBar) findViewById(R.id.pbQuickFindLoad);
		pbQuickFind.setVisibility(View.INVISIBLE);

		// setup views associated with the marker details layout
		llMarkerDetail = (LinearLayout) findViewById(R.id.llMarkerDetail);
		tvSuggPlaceName = (TextView) llMarkerDetail.findViewById(R.id.tvSuggPlaceName);
		ivRating = (ImageView) llMarkerDetail.findViewById(R.id.ivRating);
		ivLocImg = (ImageView) llMarkerDetail.findViewById(R.id.ivImgLocation);
		tvDistNum = (TextView) llMarkerDetail.findViewById(R.id.tvDistNum);
		btnMarkerRouteGo = (Button) llMarkerDetail.findViewById(R.id.btnMarkerRouteGo);
		
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
		
		
		if(!Util.isGPSAvailable(this)) {
			sendAlert("Your GPS seems to be disabled");
		}
		
		if(!Util.isNetworkAvailable(this)) {
			sendAlert("Can't connect to internet");
		}
	}
	
	private void sendAlert(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message);
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public void onRouteListener(Trip updatedTrip) {
		pbQuickFind.setVisibility(View.VISIBLE);
		trip = updatedTrip;
		map.exitMapSelectionMode();
		map.newRoute(trip);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.main, menu);
		if (miListView == null) {
			miListView = menu.findItem(R.id.miListView);
			toggleMiListView(false); // always hide list view menu item at first
		}
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
		// first try to hide the marker details layout, then if that didn't work, try to clear the map
		if (!hideMarkerDetails()) {
			map.exitMapSelectionMode();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.miNewTrip:
	        	onNewTrip( item );
	            return true;
	        case R.id.miHelp:
	        	onHelp( item );
	        	return true;
	        default:
	        	onQuickFindClick( item );
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/** *
	 * Callback to show a feedback page
	 * @param item
	 */
	public void onHelp(MenuItem item) {
		Intent i = new Intent(getBaseContext(), HelpActivity.class);
		startActivity(i);
	}

	@Override
	public void onRouted(String durationString) {
		pbQuickFind.setVisibility(View.INVISIBLE);
		tvDirPreview.setText(durationString);
		isRouteActive = true;
		showDirectionsPreview();
	}

	@Override
	public void onMapClick() {
		hideMarkerDetails();
	}

	@Override
	public void onMarkerClick(TripLocation tripLocation) {
		if (tripLocation != null) {
			markerTripLocation = tripLocation;
			DecimalFormat df;
			tvSuggPlaceName.setText(tripLocation.getLocationName());
			Picasso.with(llMarkerDetail.getContext()).load( tripLocation.getRatingImgUrl() ).into( ivRating );
			Picasso.with(llMarkerDetail.getContext()).load( tripLocation.getImageUrl() ).into( ivLocImg );
			df = new DecimalFormat("#0.00");
			tvDistNum.setText(df.format(tripLocation.getDistance()/YelpFilterRequest.DEFAULT_ONE_MILE_RADIUS_IN_METER));
			if (newTrip) {
				btnMarkerRouteGo.setText("Go!");
			} else {
				btnMarkerRouteGo.setText("Add Midpoint");
			}
			showMarkerDetails();
		} else {
			Log.d("travelIt", "location is null in confirm dialog???");
		}
	}

	/** Animate the marker details layout up */
	private void showMarkerDetails() {
		if (llMarkerDetail.getVisibility() == View.GONE) {
			hideDirectionsPreview();
			llMarkerDetail.setVisibility(View.VISIBLE);
			llMarkerDetail.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bottom_in));
		}
	}

	/**
	 * Animate the marker details layout out
	 * @return		true if successfully hided the marker details layout
	 */
 	private boolean hideMarkerDetails() {
		if (llMarkerDetail.getVisibility() == View.VISIBLE) {
			map.replaceReplacementMarker();
			markerTripLocation = null;
			llMarkerDetail.setVisibility(View.GONE);
			llMarkerDetail.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bottom_out));
			if (isRouteActive) {
				showDirectionsPreview();
			}
			return true;
		}
		return false;
	}

	/** Animate the directions preview layout up */
	private void showDirectionsPreview() {
		if (rlDirPreview.getVisibility() == View.GONE) {
			rlDirPreview.setVisibility(View.VISIBLE);
			rlDirPreview.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bottom_in));
		}
	}

	/**
	 * Animate the directions preview layout out
	 */
	private void hideDirectionsPreview() {
		if (rlDirPreview.getVisibility() == View.VISIBLE) {
			rlDirPreview.setVisibility(View.GONE);
			rlDirPreview.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bottom_out));
		}
	}

	/** Callback for when the Route button is clicked from the marker details layout */
	public void onMapViewRouteClick(View v) {
		if (markerTripLocation != null) {
			trip.addPlace(markerTripLocation);
			hideMarkerDetails();
			onRouteListener(trip);
		}
	}

	/** Display directions */
	public void onDirectionDetails(View v) {
		if(segments != null) {
			Intent i = new Intent(MainActivity.this, DetailsActivity.class);
			i.putExtra(SEGMENTS, segments);
			startActivity(i);
			overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
		}
	}

	/** Callback when new trip button is clicked */
	public void onNewTrip(MenuItem mi) {
		Location myLoc = map.getMap().getMyLocation();
		if(myLoc != null) {
			FiltersDialog.newInstance("", myLoc.getLatitude(), myLoc.getLongitude(), true).show(getFragmentManager(), "filters");
		}
	}

	@Override
	public void onExitMapView() {
		toggleMiListView(false);
	}

	@Override
	public void enterMapView(ArrayList<TripLocation> suggPlacesList, Trip trip, boolean newTrip) {
		suggestedPlaces = suggPlacesList;
		this.trip = trip;
		map.enterMapSelectionMode(suggPlacesList, newTrip);
		toggleMiListView(true);
		
		isQueryLoading = false;	
	}
	
	@Override
	public void openAddDialog(LatLng location) {
		if(trip != null && trip.getPlaces() != null && trip.getPlaces().size() > 1) {
			FiltersDialog.newInstance("", location.latitude, location.longitude, false).show(getFragmentManager(), "filters");
		}
	}
	
	@Override
	public void getAddResults(YelpFilterRequest filterRequest) {
		isQueryLoading = true;
		pbQuickFind.setVisibility(View.VISIBLE);
		newTrip = false;
		SimpleYelpClient.getRestClient().search(filterRequest, this);
	}

	/**
	 * Toggles the visibility of the list view menu item
	 * @param enabled	if true, then show the menu item
	 */
	protected void toggleMiListView(boolean enabled) {
		miListView.setVisible(enabled);
		miListView.setEnabled(enabled);
		if (enabled) {
			miListView.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		} else {
			miListView.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		}
	}

	/** callback when list view menu button is clicked */
	public void onListView(MenuItem mi) {
		StandaloneSuggestedPlacesDialog.newInstance(trip, suggestedPlaces).show(getFragmentManager(), "destinations");
	}
	
	/** Stop showing a progress bar. */
	private void stopShowingProgressBar() {
		pbQuickFind.setVisibility( View.INVISIBLE );
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
			e.printStackTrace();
			isQueryLoading = false;	
			stopShowingProgressBar();
		}
	}

	@Override
	public void onFailure(JSONObject failureResult) {
		isQueryLoading = false;	
		stopShowingProgressBar();
	}

	@Override
	public void onMapLoadedComplete() {
		stopShowingProgressBar();
	}

	@Override
	public void onMapLoadedCancel() {
		stopShowingProgressBar();
	}
}
