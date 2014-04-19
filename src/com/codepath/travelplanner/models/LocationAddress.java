package com.codepath.travelplanner.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * LocationAddress.java
 * Strongly typed data class for storing address info.
 * @author nkemavaha
 *
 */
public class LocationAddress {
	
	private String mainAddress;
	
	private String city;
	
	private String state;
	
	private String zip;
	
	public String getMainAddress() {
		return mainAddress;
	}


	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZip() {
		return zip;
	}


	
	public static LocationAddress fromJSON( JSONObject object ) {
		LocationAddress loc = new LocationAddress();
		
		JSONArray arr = null;
		try {
			arr = object.getJSONArray("address");
			loc.mainAddress = arr.getString(0);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			loc.city = object.getString("city");
			loc.zip = object.getString("postal_code");
			loc.state = object.getString("state");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return loc;
	}
	
	
}
