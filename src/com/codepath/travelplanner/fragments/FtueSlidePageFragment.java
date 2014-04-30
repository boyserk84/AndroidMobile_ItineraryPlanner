package com.codepath.travelplanner.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.codepath.travelplanner.R;

/**
 * FtueSlidePageFragment
 * @author nkemavaha
 *
 */
public class FtueSlidePageFragment extends Fragment {
	
	private static final String KEY_PAGE = "pageNumber";
	
	private int pageNum;
	
	/**
	 * Static method to create a new instance of FtueSlidePageFragment
	 * @param page
	 * @return
	 */
	public static FtueSlidePageFragment newInstance( int page ) {
		FtueSlidePageFragment fragmentFirst = new FtueSlidePageFragment();
		Bundle args = new Bundle();
		args.putInt(KEY_PAGE, page);
		fragmentFirst.setArguments(args);
		return fragmentFirst;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Retrieve data from bundle object
		pageNum = getArguments().getInt(KEY_PAGE);
	}
	
	private int getImgResourceByPos(int position) {
		int result = R.drawable.f_instruction_01;
		
		switch ( position ) {
		case 1:
			result = R.drawable.f_instruction_02;
			break;
			
		case 2:
			result = R.drawable.f_instruction_03;
			break;
			
		case 3:
			result = R.drawable.f_instruction_04;
			break;
		}
		
		return result;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = (View) inflater.inflate( R.layout.fragment_ftue_slidepage, container, false);
		
		if ( rootView != null ) {
			ImageView ivSlidePage = (ImageView) rootView.findViewById( R.id.ivFtueSlide );
			ivSlidePage.setImageResource( getImgResourceByPos(pageNum));
		}
		return rootView;
	}
}
