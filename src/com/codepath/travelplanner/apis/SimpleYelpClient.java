package com.codepath.travelplanner.apis;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import natemobiles.app.simpleyelpapiforandroid.configs.SimpleYelpClientConfig;
import natemobiles.app.simpleyelpapiforandroid.configs.YelpApiV2;
import natemobiles.app.simpleyelpapiforandroid.interfaces.IRequestListener;
import android.os.AsyncTask;

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
		execute( query, Double.toString( latitude ), Double.toString( longitude ) );
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

		// Create a GET request
		OAuthRequest request = new OAuthRequest(Verb.GET, SimpleYelpClientConfig.REST_URL_SEARCH_API_BASE );
		
		// TODO: make this more robust and support multiple parameters
		// TODO: Create a request object
		
		// Add parameters
		request.addQuerystringParameter("term", searchTerm);
		request.addQuerystringParameter("ll", latitude + "," + longitude);
		request.addQuerystringParameter("sort", "1");	// sort by distance
		
		// Sign a request with access token.
		this.service.signRequest( accessToken, request);
		Response response = request.send();
		return response.getBody();
	}
}
