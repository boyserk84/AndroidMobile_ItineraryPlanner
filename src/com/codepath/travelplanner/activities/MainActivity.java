package com.codepath.travelplanner.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.codepath.travelplanner.R;
import com.codepath.travelplanner.directions.Routing;
import com.codepath.travelplanner.directions.RoutingListener;
import com.codepath.travelplanner.directions.Segment;
import com.codepath.travelplanner.fragments.InputDialog;
import com.codepath.travelplanner.fragments.MyMapFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends FragmentActivity implements RoutingListener {
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
	
	public void onNewTrip(View v) {
		DialogFragment newFragment = new InputDialog();
	    newFragment.show(getFragmentManager(), "new_trip");
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
}
