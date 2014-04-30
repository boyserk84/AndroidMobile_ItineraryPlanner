package com.codepath.travelplanner.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * GooglePlace - model containing info about a google place (obtained for the Google Places API)
 */
public class GooglePlace {
	/** bus station type */
	public static final String TYPE_BUS_STATION = "bus_station";
	/** subway station type */
	public static final String TYPE_SUBWAY_STATION = "subway_station";
	/** train station type */
	public static final String TYPE_TRAIN_STATION = "train_station";

	private String id;

	private String icon;

	private String name;

	private Double latitude;

	private Double longitude;

	private ArrayList<String> types = new ArrayList<String>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTypes(JSONArray types) {
		if (types != null) {
			for (int i = 0; i < types.length(); i++){
				try {
					this.types.add(types.get(i).toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/** @return true if the place's types contains the given type */
	public boolean hasType(String type) {
		return types.indexOf(type) >= 0;
	}

	public static GooglePlace fromJSONObject(JSONObject object) {
		try {
			GooglePlace result = new GooglePlace();
			JSONObject geometry = (JSONObject) object.get("geometry");
			JSONObject location = (JSONObject) geometry.get("location");
			result.setLatitude((Double) location.get("lat"));
			result.setLongitude((Double) location.get("lng"));
			result.setIcon(object.getString("icon"));
			result.setName(object.getString("name"));
			result.setId(object.getString("id"));
			result.setTypes(object.getJSONArray("types"));
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String toString() {
		return "Place{" + "id=" + id + ", icon=" + icon + ", name=" + name + ", latitude=" + latitude + ", longitude=" + longitude + '}';
	}

}
