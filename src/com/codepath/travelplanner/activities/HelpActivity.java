package com.codepath.travelplanner.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.travelplanner.R;
import com.codepath.travelplanner.fragments.HelpPageFragment;
import com.codepath.travelplanner.helpers.Util;

/**
 * Help Activity
 * @author nkemavaha
 *
 */
public class HelpActivity extends FragmentActivity {
	
	private HelpPageFragment helpFragment;
	
	private FragmentManager manager;
	
	private String applicationVersionString = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		applicationVersionString = Util.getApplicationVersion( this );
		showPage( 0 );
	}
	
	/**
	 * Helper function to show the given page.
	 * @param page
	 */
	private void showPage(int page){
		switch ( page ) {
		case 1:
			helpFragment = HelpPageFragment.newInstance( 1, applicationVersionString );
			break;
		case 2:
			helpFragment = HelpPageFragment.newInstance( 2, applicationVersionString );
			break;
		default:
			helpFragment = HelpPageFragment.newInstance( 0, applicationVersionString );
			break;
		}
		
		manager = getSupportFragmentManager();

		// use appropriate transaction for backward compatibility
		FragmentTransaction fts = manager.beginTransaction();
		
		fts.replace( R.id.flHelpFragment, helpFragment);
		// commit and update changes to fragment
		fts.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
			case R.id.miLegals:
				showPage(2);
				return true;
				
			case R.id.miCredits:
				showPage(1);
				return true;
			default:
				showPage(0);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

}
