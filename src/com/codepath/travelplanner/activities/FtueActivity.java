package com.codepath.travelplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.codepath.travelplanner.R;
import com.codepath.travelplanner.adapters.FtueFragementAdapter;


/**
 * FtueActivity
 * First-Time User Experience Activity
 * 
 * Only shows for first time user.
 * 
 * TODO: Need persistent or session data to save Ftue Flag.
 * @author nkemavaha
 *
 */
public class FtueActivity extends FragmentActivity {
	
	private FtueFragementAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ftue);
		setupViews();
	}
	
	/**
	 * Setup views and setup event listeners for this activity
	 */
	private void setupViews() {
		ViewPager pager = (ViewPager) findViewById(R.id.vpPager);

		adapter = new FtueFragementAdapter( getSupportFragmentManager() );
		if ( pager != null ) {
			pager.setAdapter( adapter );
			pager.setOnPageChangeListener( new OnPageChangeListener() {
				
				@Override
				public void onPageSelected(int page) {}
				
				@Override
				public void onPageScrolled(int pos, float arg1, int arg2) {
					Fragment ftueFragment = adapter.getItem( pos );
					FragmentManager manager = getSupportFragmentManager();
					
					// use appropriate transaction for backward compatibility
					FragmentTransaction fts = manager.beginTransaction();
					
					fts.replace( R.id.flFtueContainer , ftueFragment);
					
					// commit and update changes to fragment
					fts.commit();
					
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
		Intent i = new Intent( getBaseContext(), MainActivity.class);
		startActivity( i );
		this.finish();
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
	
}