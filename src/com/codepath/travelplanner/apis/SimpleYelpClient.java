package com.codepath.travelplanner.apis;

import android.os.AsyncTask;
import com.codepath.travelplanner.models.YelpFilterRequest;
import natemobiles.app.simpleyelpapiforandroid.configs.SimpleYelpClientConfig;
import natemobiles.app.simpleyelpapiforandroid.configs.YelpApiV2;
import natemobiles.app.simpleyelpapiforandroid.interfaces.IRequestListener;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.util.ArrayList;

/**
 * SimpleYelpClient
 * 
 * This is a singleton and running on a separate thread.
 * Simple Yelp Client using Yelp API version 2
 * 
 * @author nkemavaha
 *
 */
public class SimpleYelpClient extends AsyncTask<String, Void, String> {

    /** Singleton instance */
    private static SimpleYelpClient instance = null;
    
    /** Access Token */
    private static Token accessToken;
    
    /** OAuthService for setup configuration and request object */
    OAuthService service;
    
    /**
     * Array of listeners to dispatch when request is received. 
     */
    ArrayList<IRequestListener> listeners;
    
    public SimpleYelpClient() {
    	this.service = new ServiceBuilder()
    			.provider( YelpApiV2.class )
    			.apiKey( SimpleYelpClientConfig.REST_CONSUMER_KEY )
    			.apiSecret( SimpleYelpClientConfig.REST_CONSUMER_SECRET )
    			.build();
    	
    	// We only need to create this once. Better memory management.
    	if ( accessToken == null ) {
    		accessToken = new Token(SimpleYelpClientConfig.TOKEN, SimpleYelpClientConfig.TOKEN_SECRET);
    	}
    	
    	listeners = new ArrayList<IRequestListener>();
    }
    
    /**
     * Get a singleton instance of Yelp Rest client
     * @return SimpleYelpClient instance
     */
    public static SimpleYelpClient getRestClient() {
    	if ( instance == null ) {
    		instance = new SimpleYelpClient();
    	}

		return instance;
    }
    
	/**
	 * Clear all memory and tasks.
	 */
	protected void dispose() {
		service = null;
		listeners.clear();
		listeners = null;
		instance = null;
	}
	
	/**
	 * Request a search request to Yelp API
	 * @param query			Query term
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
		YelpFilterRequest yelpRequest = new YelpFilterRequest();
		yelpRequest.term = query;
		yelpRequest.latitude = latitude;
		yelpRequest.longitude = longitude;
		runSearch( yelpRequest );
	}
	
	/**
	 * Request a search request to Yelp API by Filter Object
	 * @param filter		Yelp filter object
	 * @param handlers		callbacks/handler when response is received.
	 */
	public void search(YelpFilterRequest filter, IRequestListener... handlers) {
		if ( handlers != null && handlers.length > 0) {
			for (IRequestListener handler:handlers) {
				if ( handler != null ) {
					listeners.add( handler );
				}
			}
		}
		runSearch( filter );
	}
	
	/**
	 * Run a search query based on the given YelpFilterRequest object
	 * @param filter
	 */
	private void runSearch( YelpFilterRequest filter ) {	
		if ( filter != null ) {
			execute( filter.term, 
					Double.toString( filter.latitude ), 
					Double.toString( filter.longitude), 
					Integer.toString( (filter.limit <= 20) ? filter.limit: 20 ), 
					Integer.toString( filter.sortType ), 
					Double.toString( filter.radius ),
					filter.categoryFilter
					);
		} else {
			throw new NullPointerException("YelpFilterRequest is null before search is run!");
		}
	}

    ///////////////////////////////
    /// Override implementations
    ///////////////////////////////
	
	
	@Override
	protected void onPostExecute(String result) {
		// Iterate to each listener
		for (IRequestListener listener:listeners){
			JSONObject object = null;
			try {
				object = new JSONObject( result );
				if ( object != null ) {
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
					// TODO Auto-generated catch block
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
		String searchTerm = params[0];
		String latitude = params[1];
		String longitude = params[2];
		String limit = params[3];
		String sortType = params[4];
		String radius = params[5];
		String categoryFilter = params[6];

		// Create a GET request
		OAuthRequest request = new OAuthRequest(Verb.GET, SimpleYelpClientConfig.REST_URL_SEARCH_API_BASE );
		
		request.addQuerystringParameter("term", formatQueryString( searchTerm ) );
		request.addQuerystringParameter("ll", latitude + "," + longitude);
		request.addQuerystringParameter("sort", sortType);
		request.addQuerystringParameter("radius_filter", radius);
		request.addQuerystringParameter("limit", formatQueryString( limit ) ); 
		request.addQuerystringParameter("category_filter", formatQueryString( categoryFilter ) ); 
		
		// Sign a request with access token.
		this.service.signRequest( accessToken, request);
		Response response = request.send();
		return response.getBody();
	}
	
	/**
	 * Helper function to format the given string to appropriate query string
	 * NOTE: This is for sanity check because OAuthRequest.addQueryStringParameter() will crash if string value is null.
	 * @param str		String
	 * @return String. Otherwise, if pass in null, it will return empty string.
	 */
	private String formatQueryString(String str) {
		if ( str != null ) {
			return str;
		}
		return "";
	}
}
