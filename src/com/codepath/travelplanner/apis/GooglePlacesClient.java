package com.codepath.travelplanner.apis;

import android.os.AsyncTask;
import natemobiles.app.simpleyelpapiforandroid.interfaces.IRequestListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * GooglePlacesClient - Google Places API client
 */
public class GooglePlacesClient extends AsyncTask<String, Void, String> {
	/** api key */
	private static final String API_KEY = "AIzaSyAA4HKtHxbBnTUOgeJEmMls4QzbQY_TQP8";
	/** rest api url */
	private static final String REST_URL = "https://maps.googleapis.com/maps/api/place/search/json?";

	/** query string for places that are subway stations */
	public static final String TRANSIT_STATION_QUERY = "subway_station|bus_station";
	/** distance to search for */
	private static final int DISTANCE_QUERY_IN_METERS = 6000;

	/**
	 * Array of listeners to dispatch when request is received.
	 */
	ArrayList<IRequestListener> listeners;

	/**
	 * constructor - not singleton because the asynctask may need to be ran multiple times,
	 * which can't be done with a singleton asynctask
	 */
	public GooglePlacesClient() {
		listeners = new ArrayList<IRequestListener>();
	}

	/**
	 * Clear all memory and tasks.
	 */
	protected void dispose() {
		listeners.clear();
		listeners = null;
	}

	/**
	 * Request a search request to Google Places API
	 * @param query			Query term (type of place)
	 * @param latitude		Latitude
	 * @param longitude		Longitude
	 * @param handlers		callbacks/handlers when response is received.
	 */
	public void search(String query, double latitude, double longitude, IRequestListener... handlers) {
		if ( handlers != null && handlers.length > 0) {
			for (IRequestListener handler:handlers) {
				if ( handler != null ) {
					listeners.add( handler );
				}
			}
		}
		// make sure the task is finished before executing another one
		execute(query, Double.toString(latitude), Double.toString(longitude));
	}

	@Override
	protected void onPostExecute(String result) {
		// Iterate to each listener
		for (IRequestListener listener:listeners){
			JSONObject object = null;
			try {
				object = new JSONObject(result);
				if (object != null) {
					boolean success = object.isNull("error");
					if ( success ) {
						listener.onSuccess( object );
					} else {
						listener.onFailure( object );
					}
				}
			} catch (JSONException e) {
				object = new JSONObject();
				String errorMessage = e.getMessage();
				try {
					object.put("error", errorMessage);
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				listener.onFailure( object );
			}
		}
		// Clear all listeners
		listeners.clear();
		// null instance so that new AsyncTask can be running
		dispose();
	}

	@Override
	protected String doInBackground(String... params) {
		if (!isCancelled()) {
			return getUrlContents(constructURL(params));
		}
		return "";
	}

	/** gets the url contencts */
	private String getUrlContents(String theUrl) {
		StringBuilder content = new StringBuilder();
		try {
			URL url = new URL(theUrl);
			URLConnection urlConnection = url.openConnection();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(urlConnection.getInputStream()), 8);
			String line;
			while (!isCancelled() && (line = bufferedReader.readLine()) != null) {
				content.append(line + "\n");
			}
			bufferedReader.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return content.toString();
	}

	/** constructs the url used to query for the google place */
	protected String constructURL(String... params) {
		final StringBuffer mBuf = new StringBuffer(REST_URL);
		mBuf.append("types=");
		mBuf.append(params[0]);
		mBuf.append("&location=");
		mBuf.append(params[1]);
		mBuf.append(',');
		mBuf.append(params[2]);
		mBuf.append("&radius=");
		mBuf.append(DISTANCE_QUERY_IN_METERS);
		mBuf.append("&sensor=false&key=");
		mBuf.append(API_KEY);
		return mBuf.toString();
	}
}
