package com.codepath.travelplanner.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * GooglePlace - model containing info about a google place (obtained for the Google Places API)
 */
public class GooglePlace {
	private String id;

	private String icon;

	private String name;

	private String vicinity;

	private Double latitude;

	private Double longitude;

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

	public String getVicinity() {
		return vicinity;
	}

	public void setVicinity(String vicinity) {
		this.vicinity = vicinity;
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
			result.setVicinity(object.getString("vicinity"));
			result.setId(object.getString("id"));
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
