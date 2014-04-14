package com.codepath.travelplanner.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.codepath.travelplanner.R;
import com.codepath.travelplanner.adapters.SegmentsAdapter;
import com.codepath.travelplanner.directions.Segment;

public class DetailsActivity extends Activity {
	private ListView lvSegments;
    private SegmentsAdapter adapterSegments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		
		lvSegments = (ListView) findViewById(R.id.lvSegements);
		
		//Intent i = getIntent();
		//ArrayList<Segment> segments = (ArrayList<Segment>) i.getSerializableExtra(MainActivity.SEGMENTS);
		
		ArrayList<Segment> segments = MainActivity.segments;
		adapterSegments = new SegmentsAdapter(this, segments);
		lvSegments.setAdapter(adapterSegments);
	}
}
