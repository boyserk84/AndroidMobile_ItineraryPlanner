package com.codepath.travelplanner.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
	
	/**
	 * Helper method to get String Resource Id based on the given page position.
	 * @param position
	 * @return Id corresponding to string resource id.
	 */
	private int getStringResourceByPos(int position) {
		int result = R.string.ftue_page_1;
		switch ( position ) {

		case 1:
			result = R.string.ftue_page_2;
			break;

		case 2:
			result = R.string.ftue_page_3;
			break;

		case 3:
			result = R.string.ftue_page_4;
			break;
		case 4:
			result = R.string.ftue_page_5;
			break;
		case 5:
			result = R.string.ftue_page_6;
			break; 

		}
		return result;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = (View) inflater.inflate( R.layout.fragment_ftue_slidepage, container, false);
		
		if ( rootView != null ) {
			TextView sliderText = (TextView) rootView.findViewById(R.id.tvFtueTitle );
			ImageView ivSlidePage = (ImageView) rootView.findViewById( R.id.ivFtueSlide );
			
			sliderText.setText( getResources().getString( getStringResourceByPos( pageNum )  ) );
		}
		return rootView;
	}
}
