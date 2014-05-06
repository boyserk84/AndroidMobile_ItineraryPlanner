package com.codepath.travelplanner.helpers;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.gms.maps.model.LatLng;

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
			formatted += hours + " hour";
			if (hours > 1) {
				formatted += "s";
			}
			if (minutes > 0) {
				formatted += " and ";
			}
		}
		if (minutes > 0) {
			formatted += minutes + " minute";
			if (minutes > 1) {
				formatted += "s";
			}
		}
		return formatted;
	}
	
	/** @return true if there is network connection */
	public static boolean isNetworkAvailable(Activity activity) {
		ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	/** @return true if there is GPS available */
	public static boolean isGPSAvailable(Activity activity) {
		LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	
	/**
	 * @return Application version and its code version.
	 */
	public static String getApplicationVersion(Activity activity) {
		String applicationCode = "";
		String versionName = "versionUnknown";
		int versionCode = 0;
		/* Get android:versionName */
		try {
			versionName = activity.getPackageManager().getPackageInfo( activity.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/* Get android:versionCode */
		try {
			versionCode = activity.getPackageManager().getPackageInfo( activity.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		applicationCode = versionName + ".vc." + versionCode;
		
		return applicationCode;
	}
}
