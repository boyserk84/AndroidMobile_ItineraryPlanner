package com.codepath.travelplanner.directions;

import android.os.AsyncTask;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Routing extends AsyncTask<LatLng, Void, Route> {
	protected ArrayList<RoutingListener> _aListeners;
	protected TravelMode _mTravelMode;

	public enum TravelMode {
		BIKING("biking"),
		DRIVING("driving"),
		WALKING("walking"),
		TRANSIT("transit");
	
		protected String _sValue;
	
		private TravelMode(String sValue) {
			this._sValue = sValue;
		}
	
		protected String getValue() {
			return _sValue;
		}
	}


	public Routing(TravelMode mTravelMode) {
		this._aListeners = new ArrayList<RoutingListener>();
		this._mTravelMode = mTravelMode;
	}

	public void registerListener(RoutingListener mListener) {
		_aListeners.add(mListener);
	}

	protected void dispatchOnStart() {
		for (RoutingListener mListener: _aListeners) {
			mListener.onRoutingStart();
		}
	}

	protected void dispatchOnFailure() {
		for (RoutingListener mListener: _aListeners) {
			mListener.onRoutingFailure();
		}
	}

	protected void dispatchOnSuccess(PolylineOptions mOptions, List<Segment> segments, int duration) {
		for (RoutingListener mListener: _aListeners) {
			mListener.onRoutingSuccess(mOptions, segments, duration);
		}
	}

	/**
	* Performs the call to the google maps API to acquire routing data and 
	*   deserializes it to a format the map can display.
	* @param aPoints
	*/
	@Override
	protected Route doInBackground(LatLng... aPoints) {
		for (LatLng mPoint : aPoints) {
			if (mPoint == null) {
				return null;
			}
		}
		return new GoogleParser(constructURL(aPoints)).parse();
	}

	/**
	 * Constructs a URL that queries for directions from a path of points
	 * @param points	list of points in the route
	 * @return		URL for the query
	 */
	protected String constructURL(LatLng... points) {
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		long time = Math.round(c.getTimeInMillis() / 1000);
		if(hour > 0 && hour < 6) {
			time = 1343340000;
		}
		LatLng start = points[0];
		LatLng dest = points[1];
		String sJsonURL = "http://maps.googleapis.com/maps/api/directions/json?";

		final StringBuffer mBuf = new StringBuffer(sJsonURL);
		mBuf.append("origin=");
		mBuf.append(start.latitude);
		mBuf.append(',');
		mBuf.append(start.longitude);
		mBuf.append("&destination=");
		mBuf.append(dest.latitude);
		mBuf.append(',');
		mBuf.append(dest.longitude);
		mBuf.append("&sensor=true&mode=");
		mBuf.append(_mTravelMode.getValue());
		mBuf.append("&departure_time=");
		mBuf.append(time);

        return mBuf.toString();
	}

	@Override
	protected void onPreExecute() {
		dispatchOnStart();
	}

	@Override
	protected void onPostExecute(Route result) {		
		if(result==null) {
			dispatchOnFailure();
		}
		else {
			PolylineOptions mOptions = new PolylineOptions();
		
			for (LatLng point : result.getPoints()) {
				mOptions.add(point);
			}
			dispatchOnSuccess(mOptions, result.getSegments(), result.getDuration());
		}
	}
}