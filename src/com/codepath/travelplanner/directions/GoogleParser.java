package com.codepath.travelplanner.directions;

import android.util.Log;
import com.codepath.travelplanner.R;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GoogleParser extends XMLParser implements Parser {
	/** Distance covered. **/
	private int distance;
	
	public GoogleParser(String feedUrl) {
	        super(feedUrl);
	}

	/**
	 * Parses a url pointing to a Google JSON object to a Route object.
	 * @return a Route object based on the JSON object by Haseem Saheed
	 */
	public Route parse() {
		// turn the stream into a string
		final String result = convertStreamToString(this.getInputStream());
		if (result == null) return null;
		
		//Create an empty route
		final Route route = new Route();
		//Create an empty segment
		final Segment segment = new Segment();
		try {
			//Tranform the string into a json object
			final JSONObject json = new JSONObject(result);
			//Get the route object
			final JSONObject jsonRoute = json.getJSONArray("routes").getJSONObject(0);
			//Get the leg, only one leg as we don't support waypoints
			final JSONObject leg = jsonRoute.getJSONArray("legs").getJSONObject(0);
			//Get the steps for this leg
			final JSONArray steps = leg.getJSONArray("steps");
			//Set the name of this route using the start & end addresses
			route.setName(leg.getString("start_address") + " to " + leg.getString("end_address"));
			//Set the duration of the route
			route.setDuration(leg.getJSONObject("duration").getInt("value"));
			//Get google's copyright notice (tos requirement)
			route.setCopyright(jsonRoute.getString("copyrights"));
			//Get the total length of the route.
			route.setLength(leg.getJSONObject("distance").getInt("value"));
			//Get any warnings provided (tos requirement)
			if (!jsonRoute.getJSONArray("warnings").isNull(0)) {
				route.setWarning(jsonRoute.getJSONArray("warnings").getString(0));
			}
			
			/* Loop through the steps, creating a segment for each one and
			 * decoding any polylines found as we go to add to the route object's
			 * map array. Using an explicit for loop because it is faster!
			 */
			for (int i = 0; i < steps.length(); i++) {
				//Get the individual step
				final JSONObject step = steps.getJSONObject(i);
				if(step.isNull("steps")) {
					parseStep(step, segment, i == 0, i == steps.length() - 1);
					route.addSegment(segment.copy());
				}
				else {
					final JSONArray subSteps = step.getJSONArray("steps");
					for(int j = 0; j < subSteps.length(); j++) {
						final JSONObject subStep = subSteps.getJSONObject(j);
						parseStep(subStep, segment, i == 0 && j == 0, i == steps.length() - 1 && j == subSteps.length() - 1);
						if(i != steps.length() - 1 && j == subSteps.length() - 1) {
							String dest = segment.getInstruction();
							if(dest.contains("Destination")) {
								dest = dest.substring(0, dest.indexOf("Destination"));
							}
							if(dest.equals("")) {
								dest = step.getString("html_instructions").replaceAll("<(.*?)*>", "");
							}
							segment.setInstruction(dest);
						}
						route.addSegment(segment.copy());
					}
				}
				
				//Retrieve & decode this segment's polyline and add it to the route.
				route.addPoints(decodePolyLine(step.getJSONObject("polyline").getString("points")));
			}
		}
		catch (JSONException e) {
			Log.e("Routing Error",e.getMessage());
			return null;
		}
		return route;
    }
	
	private void parseStep(JSONObject step, Segment segment, boolean isFirst, boolean isLast) {
		segment.clear();
		try {
			//Get the start position for this step and set it on the segment
			final JSONObject start = step.getJSONObject("start_location");
			segment.setLat(start.getDouble("lat"));
			segment.setLng(start.getDouble("lng"));
			//Set the length of this segment in meters
			final int length = step.getJSONObject("distance").getInt("value");
			distance += length;
			final String lengthAsText = step.getJSONObject("distance").getString("text");
			segment.setLength(lengthAsText);
			segment.setDistance(distance/1000);

			if(!step.isNull("html_instructions")) {
				//Strip html from google directions and set as turn instruction
				segment.setInstruction(step.getString("html_instructions").replaceAll("<(.*?)*>", ""));
			}
			
			if(isFirst) {
				segment.setIcon(R.drawable.ic_start);
			} else if(isLast) {
				segment.setIcon(R.drawable.ic_stop);
			} else if (segment.getInstruction().indexOf("Turn right") == 0) {
				segment.setIcon(R.drawable.ic_walk_right);
			} else if (segment.getInstruction().indexOf("Turn left") == 0) {
				segment.setIcon(R.drawable.ic_walk_left);
			} else {
				segment.setIcon(R.drawable.ic_walk);
			}
			
			if(!step.isNull("transit_details")) {
				//Get the transit info
				final JSONObject transit = step.getJSONObject("transit_details");
				
				if(!transit.isNull("line")) {
					final JSONObject line = transit.getJSONObject("line");
					
					//transit_details.line.vehicle.icon has a url to an icon we can use for the segment icon
					String type = "";
					if(!line.isNull("vehicle")) {
						final JSONObject vehicle = line.getJSONObject("vehicle");
						type = vehicle.getString("name");
						segment.setIconURL("http:" + vehicle.getString("icon"));
					}
					
					//transit_details.line.name or transit_deatils.line.short_name is the bus, cat "Take the ..." to the front of segment.instruction
					if(!line.isNull("name")) {
						segment.setInstruction("Take " + segment.getInstruction().replace(type, line.getString("name") + " " + type));
					}
					else if(!line.isNull("short_name")) {
						segment.setInstruction("Take " + segment.getInstruction().replace(type, line.getString("short_name") + " " + type));
					}
				}
				
				//transit_details.arrival_stop has the latlng and location of the exit < (we can add a dot or something at these coords) < we will need to create a segment using this info
				if(!transit.isNull("arrival_stop")) {
					final JSONObject arrival_stop = transit.getJSONObject("arrival_stop");
					String name = arrival_stop.getString("name");
					segment.setInstruction(segment.getInstruction() + ". Get off at " + name);
				}
			}
		}
		catch (JSONException e) {
			Log.e("Step Error",e.getMessage());
		}
	}

	/**
	 * Convert an inputstream to a string.
	 * @param input inputstream to convert.
	 * @return a String of the inputstream.
	 */
	private static String convertStreamToString(final InputStream input) {
		if (input == null) {
			return null;
		}
	
		final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		final StringBuilder sBuf = new StringBuilder();
		
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sBuf.append(line);
			}
		}
		catch (IOException e) {
			Log.e("Routing Error",e.getMessage());
		}
		finally {
			try {
				input.close();
			}
			catch (IOException e) {
				Log.e("Routing Error",e.getMessage());
			}
		}
		return sBuf.toString();
	}

	/**
	 * Decode a polyline string into a list of GeoPoints.
	 * @param poly polyline encoded string to decode.
	 * @return the list of GeoPoints represented by this polystring.
	 */
	private List<LatLng> decodePolyLine(final String poly) {
		int len = poly.length();
		int index = 0;
		List<LatLng> decoded = new ArrayList<LatLng>();
		int lat = 0;
		int lng = 0;
		
		while (index < len) {
			int b;
			int shift = 0;
			int result = 0;
			do {
				b = poly.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;
			
			shift = 0;
			result = 0;
			do {
				b = poly.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;
	
			decoded.add(new LatLng(lat/100000d, lng/100000d));
		}

		return decoded;
	}
}