package com.codepath.travelplanner.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.codepath.travelplanner.R;
import com.codepath.travelplanner.adapters.FtueFragementAdapter;
import com.codepath.travelplanner.interfaces.IActivityListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;


/**
 * FtueActivity
 * First-Time User Experience Activity
 * 
 * Only shows for first time user.
 * 
 * @author nkemavaha
 *
 */
public class FtueActivity extends FragmentActivity implements IActivityListener {
	
	private final String SHARE_PREFERENCE_FTUE_SETTING = "hasDoneFtueSetting";
	
	private final String DONE_FTUE_SETTING_KEY = "doneFtue";
	
	private final int REQUEST_CODE_GOOGLE_SERVICE_RELATED = 1299;
	
	/** FTUE Adapter */
	private FtueFragementAdapter adapter;
	
	/** Flag data for determining whether user has done FTUE or not.*/
	private SharedPreferences ftueSharePref;
	
	/** Skip button */
	private Button btnSkip; 
	
	/** Flag whether user is currently in a help mode. */
	private boolean isInHelpMode = false;
	
	/** Google Service Result code (i.e. SERVICE OUT OF DATE, SUCCESS or etc)  */
	private int checkGoogleServiceResult;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ftue);
		setupViews();
		
		checkGoogleServiceResult = GooglePlayServicesUtil.isGooglePlayServicesAvailable( getBaseContext() );
		
		// Checking for Google Service dependencies
		if ( isGoogleServiceDependencyReady() ) {

			ftueSharePref = getSharedPreferences(SHARE_PREFERENCE_FTUE_SETTING, 0);

			// If already done FTUE or currently NOT in the help mode, just skip it.
			if ( isInHelpMode == false && ftueSharePref.getBoolean( DONE_FTUE_SETTING_KEY , false) == true ) {
				onSkipPressed( null );
			}
		} else {
			// If google service is not available or out of date, showing error dialog
			showGoogleServiceErrorDialog();
		}
	}
	
	/**
	 * Check if Google Service dependency is ready, 
	 * @return True if we have google service on the device and it's up-to-date.
	 */
	private boolean isGoogleServiceDependencyReady() {
		return checkGoogleServiceResult == ConnectionResult.SUCCESS;
	}
	
	/** Show Google service error dialog. */
	private void showGoogleServiceErrorDialog() {
		GooglePlayServicesUtil.getErrorDialog( checkGoogleServiceResult, this, REQUEST_CODE_GOOGLE_SERVICE_RELATED ).show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ( requestCode == REQUEST_CODE_GOOGLE_SERVICE_RELATED ) {
			if ( resultCode != RESULT_OK ){
				Log.d("DEBUG_DEVICE", "Please update your google service.");
			}
		}
	}
	
	/** Method to save the Ftue state of the current user. */
	private void finishFtue() {
		if ( ftueSharePref != null ) {
			SharedPreferences.Editor editor = ftueSharePref.edit();
			editor.putBoolean( DONE_FTUE_SETTING_KEY , true);
			editor.commit();	
		}
	}
	
	/**
	 * Setup views and setup event listeners for this activity
	 */
	private void setupViews() {
		ViewPager pager = (ViewPager) findViewById(R.id.vpPager);
		btnSkip = (Button) findViewById( R.id.btnSkipFtue );

		adapter = new FtueFragementAdapter( getSupportFragmentManager() );
		if ( pager != null ) {
			pager.setAdapter( adapter );
			pager.setOnPageChangeListener( new OnPageChangeListener() {
				
				@Override
				public void onPageSelected(int page) {}
				
				@Override
				public void onPageScrolled(int pos, float arg1, int arg2) {
					Fragment ftueFragment = adapter.getItem( pos );
					
					// Check if run out of ftue page
					if ( pos < adapter.getCount() -1 ) {

						// If the last page, modify skip button text
						if ( pos == (adapter.getCount() - 1) ) {				
							btnSkip.setText(R.string.getStarted);
						} else if ( pos == 0 ) {
							btnSkip.setText(R.string.ftue_page_1);
						} else {
							btnSkip.setText(R.string.skip);
						}

						FragmentManager manager = getSupportFragmentManager();

						// use appropriate transaction for backward compatibility
						FragmentTransaction fts = manager.beginTransaction();

						fts.replace( R.id.flFtueContainer , ftueFragment);

						// commit and update changes to fragment
						fts.commit();
						
					} else {
						// NOTE: We have an option for user to just tap
						// to open the main activity right away if it's taking awhile after swipe to this page.
						btnSkip.setText(R.string.getStarted);
						
						// If run out of ftue page, start the main activity
						onSkipPressed(null);
					}

				}
				
				@Override
				public void onPageScrollStateChanged(int arg0) {}
			});
		}	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ftue, menu);
		return true;
	}
	
	/**
	 * Callback to skip FTUE and go straight to main activity
	 * @param v
	 */
	public void onSkipPressed(View v) {
		if ( isGoogleServiceDependencyReady() ) {
			finishFtue();
			Intent i = new Intent( getBaseContext(), MainActivity.class);
			startActivity( i );
			overridePendingTransition(R.anim.bottom_out, R.anim.bottom_in);
			this.finish();
		} else {
			showGoogleServiceErrorDialog();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/////////////////////////////
	// Implement methods
	/////////////////////////////
	
	@Override
	public void onListenForAction() {
		// skip FTUE
		onSkipPressed(null);
	}

	@Override
	public void onInvalidateData() {}
	
}