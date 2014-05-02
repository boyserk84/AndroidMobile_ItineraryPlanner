package com.codepath.travelplanner.interfaces;

/**
 * IActivityListener
 * 
 * Generic interface for activity listener.
 * 
 * This is used for fragment to communicate back to activity.
 * @author nkemavaha
 *
 */
public interface IActivityListener {
	
	/**
	 * Notify activity when action in the fragment has been executed or called
	 */
	public void onListenForAction();
	
	/**
	 * Notifying activity when data has changed
	 */
	public void onInvalidateData();

}
