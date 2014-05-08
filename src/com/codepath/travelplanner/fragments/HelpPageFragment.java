package com.codepath.travelplanner.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.travelplanner.R;

/**
 * HelpPageFragment
 * 
 * Fragment for showing feedback form, credits and legals.
 * @author nkemavaha
 *
 */
public class HelpPageFragment extends Fragment {
	
	private static final String BUNDLE_KEY_PAGE = "page";
	
	private static final String BUNDLE_KEY_APPVERSION = "appVersion";
	
	/** Current page fragment user is in */
	private int pageNum;
	
	/** Application Version */
	private String appVersion;
	
	/**
	 * Static method to create a new instance of HelpPageFragment
	 * @param page
	 * @param appVersion TODO
	 * @return
	 */
	public static HelpPageFragment newInstance( int page, String appVersion ) {
		HelpPageFragment helpFragment = new HelpPageFragment();
		Bundle args = new Bundle();
		args.putInt( BUNDLE_KEY_PAGE , page);
		args.putString( BUNDLE_KEY_APPVERSION, appVersion);
		helpFragment.setArguments(args);
		return helpFragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Retrieve data from bundle object
		pageNum = getArguments().getInt( BUNDLE_KEY_PAGE );
		appVersion = getArguments().getString( BUNDLE_KEY_APPVERSION );
	}
	
	/**
	 * Get layout resource id by the given page
	 * @return
	 */
	private int getLayoutIdByPage() {
		int result = R.layout.fragment_feedback_helppage;
		switch ( pageNum ){
		case 1:
			result = R.layout.fragment_credits_helppage;
			break;
		case 2:
			result = R.layout.fragment_legals_helppage;
			break;
		}
		
		return result;
	}
	
	private EditText etName;
	private EditText etEmail;
	private EditText etDetails;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = (View) inflater.inflate( getLayoutIdByPage(), container, false);
		
		if ( rootView != null ) {
			switch( getLayoutIdByPage() ) {
			case R.layout.fragment_feedback_helppage:
				setupFeedbackForm( rootView );
				break;
				
			case R.layout.fragment_credits_helppage:
				setupCreditPage( rootView );
				break;
				
			}
		}
		return rootView;
	}
	
	/**
	 * Helper function for setup view specific to credits page
	 * @param rootView
	 */
	private void setupCreditPage(View rootView ) {
		TextView tvAppVersion = (TextView) rootView.findViewById(R.id.tvAppVersion);
		if ( tvAppVersion != null ) {
			tvAppVersion.setText( getString(R.string.appVersion) + appVersion );
		}
	}
	
	/**
	 * Helper function for setup view for feedback form
	 * @param rootView
	 */
	private void setupFeedbackForm(View rootView) {
		Button submit = (Button) rootView.findViewById(R.id.btnHelpSubmit);
		etName = (EditText) rootView.findViewById( R.id.etNameField );
		etEmail = (EditText)rootView.findViewById( R.id.etEmailField );
		etDetails = (EditText) rootView.findViewById( R.id.etDetailsField );
		
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Check if there is any feedback in details field
				if ( etDetails.getText().toString().isEmpty() == false ) {
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("plain/text");
					intent.putExtra(Intent.EXTRA_EMAIL, new String[] { getString( R.string.contact_emails ) });
					intent.putExtra(Intent.EXTRA_SUBJECT, "RouteAbout App | Feedback/Issue (" + appVersion + ")");
					intent.putExtra(Intent.EXTRA_TEXT, etDetails.getText().toString() 
							+ "\n From " + etName.getText().toString() + "(" + etEmail.getText().toString() + ")" 
							+ "\n App version: " + appVersion);
					startActivity( Intent.createChooser(intent, "") );
					
					// Empty all string, preventing spam click on submit
					etName.setText("");
					etEmail.setText("");
					etDetails.setText("");
				} 
			}
		});	
	}
}
