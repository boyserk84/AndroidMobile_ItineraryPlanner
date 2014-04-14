package com.codepath.travelplanner.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.codepath.travelplanner.R;
import com.codepath.travelplanner.dialogs.FiltersDialog;
import com.codepath.travelplanner.dialogs.SuggestedPlacesDialog;
import com.codepath.travelplanner.dialogs.SummaryDialog;
import com.codepath.travelplanner.directions.Routing;
import com.codepath.travelplanner.directions.RoutingListener;
import com.codepath.travelplanner.directions.Segment;
import com.codepath.travelplanner.fragments.MyMapFragment;
import com.codepath.travelplanner.helpers.OnPositiveListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements RoutingListener,OnPositiveListener {
	/** views */
	ImageButton ibtnNewTrip;
	EditText etNewTrip;

	protected GoogleMap map;
    protected LatLng start;
    protected LatLng end;
    public static ArrayList<Segment> segments;
    
    public static final String SEGMENTS = "segments";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		map = ((MyMapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();
		
		map.getUiSettings().setRotateGesturesEnabled(false);
		map.getUiSettings().setZoomControlsEnabled(false);
		map.setMyLocationEnabled(true);
                
        /*LatLng sydney = new LatLng(-33.867, 151.206);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        map.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney));*/
		
		CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(37.765240,-122.409432));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        map.moveCamera(center);
        map.animateCamera(zoom);

        start = new LatLng(37.765240, -122.409432);
        end = new LatLng(37.770379, -122.404110);

        Routing routing = new Routing(Routing.TravelMode.WALKING);
        routing.registerListener(this);
        routing.execute(start, end);
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
	
	public void onDetails(View v) {
		if(segments != null) {
			Intent i = new Intent(MainActivity.this, DetailsActivity.class);
			//i.putExtra(SEGMENTS, segments); //this isnt serializing so commenting out for now
			startActivity(i);
		}
	}

	@Override
    public void onRoutingFailure() {
      // The Routing request failed
    }

    @Override
    public void onRoutingStart() {
      // The Routing Request starts
    }

    @Override
    public void onRoutingSuccess(PolylineOptions mPolyOptions, List<Segment> segments) {
      PolylineOptions polyoptions = new PolylineOptions();
      polyoptions.color(Color.BLUE);
      polyoptions.width(10);
      polyoptions.addAll(mPolyOptions.getPoints());
      map.addPolyline(polyoptions);

      // Start marker
      MarkerOptions options = new MarkerOptions();
      options.position(start);
      options.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue));
      options.title("Franklin Square");
      options.snippet("Pretty cool place to hangout. 4 stars");
      map.addMarker(options);

      // End marker
      options = new MarkerOptions();
      options.position(end);
      options.icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green));
      options.title("Zynga HQ");
      options.snippet("Games are made here. 3.5 stars");
      map.addMarker(options);
      
      MainActivity.segments = new ArrayList<Segment>(segments);
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
