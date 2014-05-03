package com.codepath.travelplanner.activities;

import com.codepath.travelplanner.R;
import com.codepath.travelplanner.R.id;
import com.codepath.travelplanner.R.layout;
import com.codepath.travelplanner.R.menu;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.os.Build;

/**
 * Help Activity
 * @author nkemavaha
 *
 */
public class HelpActivity extends Activity {

	private ExpandableListView elvHelpItems;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		setupViews();
	}
	
	/** Setup views */
	private void setupViews() {
		elvHelpItems = (ExpandableListView) findViewById(R.id.elvHelp);
		// TODO: Finish this
		// Showing credits and redirect to FTUE
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
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
