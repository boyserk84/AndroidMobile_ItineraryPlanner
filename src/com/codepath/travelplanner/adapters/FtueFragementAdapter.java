package com.codepath.travelplanner.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.travelplanner.fragments.FtueSlidePageFragment;

/**
 * FtueFragmentAdapter
 * 
 * Adapter for Ftue Fragment (First Time user experience)
 * 
 * @author nkemavaha
 *
 */
public class FtueFragementAdapter extends FragmentPagerAdapter {

	private final int MAX_COUNT = 6;
	
	/** Default Constructor */
	public FtueFragementAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int pos) {
		return FtueSlidePageFragment.newInstance( pos );
	}

	@Override
	public int getCount() {
		return MAX_COUNT;
	}

}
