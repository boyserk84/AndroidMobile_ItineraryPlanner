package com.codepath.travelplanner.helpers;

/**
 * OnPositiveListener - listener for when different positive buttons are clicked
 */
public interface OnPositiveListener {
	/** callback for when the filter positive button is clicked */
	public void onFilterPositive();

	/** callback for when the suggested places positive button is clicked */
	public void onSuggestedPlacesPositive();

	/** callback for when the summary positive button is clicked */
	public void onSummaryPositive();
}
