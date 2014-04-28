package com.codepath.travelplanner.helpers;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Util - general util class
 */
public class Util {

	/** Convert an address into a LatLng object, ****if you have an exact address use that, if not send the name of the place along with city and state**** */
	public static LatLng getLatLngFromAddress(String address, Activity activity) {
		double latitude = 0;
		double longtitude = 0;
		Geocoder geoCoder = new Geocoder(activity);
		try {
			List<Address> addresses = geoCoder.getFromLocationName(address, 1);
			if (addresses.size() >  0) {
				latitude = addresses.get(0).getLatitude();
				longtitude = addresses.get(0).getLongitude();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return new LatLng(latitude, longtitude);
	}

	/**
	 * Gets the formatted string of a duration
	 * @param durationSecs		duration in secs
	 * @return the formatted time text for the matching duration
	 */
	public static String getFormattedDuration(int durationSecs) {
		String formatted = "";
		int hours   = (int)Math.floor(durationSecs / 3600);
		int minutes = (int)Math.floor((durationSecs - (hours * 3600)) / 60);

		if (hours > 0) {
			formatted += hours + " hours";
			if (minutes > 0) {
				formatted += " and ";
			}
		}
		if (minutes > 0) {
			formatted += minutes + " minutes";
		}
		return formatted;
	}
}
