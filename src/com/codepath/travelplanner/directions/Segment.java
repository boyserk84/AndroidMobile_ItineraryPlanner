package com.codepath.travelplanner.directions;

import java.io.Serializable;

/**
 * Segment - segment of a path in a route
 */
public class Segment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8583502942809850300L;
	
	/** Points in this segment. **/
	private double lat;
	/** Points in this segment. **/
	private double lng;
	/** Turn instruction to reach next segment. **/
	private String instruction = "";
	/** Length of segment in string form. **/
	private String length;
	/** Distance covered. **/
	private double distance;
	/** Icon **/
	private int icon;
	/** Icon **/
	private String iconURL;
	
	/**
	 * Create an empty segment.
	 */
	public Segment() {
	}
	
	
	/**
	 * Set the turn instruction.
	 * @param turn Turn instruction string.
	 */
	public void setInstruction(final String turn) {
		this.instruction = turn;
	}

	/**
	 * Get the turn instruction to reach next segment.
	 * @return a String of the turn instruction.
	 */
	public String getInstruction() {
		return instruction;
	}
	
	/**
	 * Add the starting latitude to this segment.
	 * @param point lat to add.
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}
	
	/** Get the starting latitude of this 
	 * segment.
	 * @return lat
	 */
	public double getLat() {
		return lat;
	}
	
	/**
	 * Add the starting longitude to this segment.
	 * @param lng to add.
	 */
	public void setLng(double lng) {
		this.lng = lng;
	}
	
	/** Get the starting longitude of this 
	 * segment.
	 * @return lng
	 */
	public double getLng() {
		return lng;
	}
	
	/**
	 * Add the icon used for this segment.
	 * @param resource id of icon.
	 */
	public void setIcon(int icon) {
		this.icon = icon;
	}
	
	/** Get the icon used by this 
	 * segment.
	 * @return resource id of icon
	 */
	public int getIcon() {
		return icon;
	}
	
	/**
	 * Add the icon used for this segment.
	 * @param resource id of icon.
	 */
	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}
	
	/** Get the icon used by this 
	 * segment.
	 * @return resource id of icon
	 */
	public String getIconURL() {
		return iconURL;
	}

	/** Creates a segment which is a copy of this one.
	 * @return a Segment that is a copy of this one.
	 */
	public Segment copy() {
		final Segment copy = new Segment();
		copy.lat = lat;
		copy.lng = lng;
		copy.instruction = instruction;
		copy.length = length;
		copy.distance = distance;
		copy.icon = icon;
		copy.iconURL = iconURL;
		return copy;
	}
	
	public void clear() {
		lat = 0;
		lng = 0;
		instruction = "";
		length = "";
		distance = 0;
		icon = 0;
		iconURL = null;
	}
	
	/**
	 * @param length the length to set
	 */
	public void setLength(final String length) {
		this.length = length;
	}
	
	/**
	 * @return the length
	 */
	public String getLength() {
		return length;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(double distance) {
	this.distance = distance;
	}
	
	/**
	 * @return the distance
	 */
	public double getDistance() {
		return distance;
	}
}