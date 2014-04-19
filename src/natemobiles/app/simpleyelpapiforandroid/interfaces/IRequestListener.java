package natemobiles.app.simpleyelpapiforandroid.interfaces;

import org.json.JSONObject;

/**
 * IRequestListener
 * 
 * Interface for listening for response from any Restful API.
 * 
 * Log:
 * 	- 04/12/2014 Currently, it is used specifically for Yelp API version 2.
 * @author nkemavaha
 *
 */
public interface IRequestListener {
	/**
	 * Callback when successful result is returned.
	 * @param successResult
	 */
	public void onSuccess(JSONObject successResult);
	
	/**
	 * Callback when failure or error result is returned.
	 * @param failureResult
	 */
	public void onFailure(JSONObject failureResult);
}
