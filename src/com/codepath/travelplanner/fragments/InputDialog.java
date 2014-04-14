package com.codepath.travelplanner.fragments;

import com.codepath.travelplanner.R;
import com.codepath.travelplanner.R.id;
import com.codepath.travelplanner.R.layout;
import com.codepath.travelplanner.R.string;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class InputDialog extends DialogFragment {
	private EditText etDest;
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View v = inflater.inflate(R.layout.trip_dialog_1, null);
		setupViews(v);
		
		builder.setView(v);
		builder.setPositiveButton(R.string.next, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					
				}
			});
		
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					InputDialog.this.getDialog().cancel();
				}
			});
		
		return builder.create();
	}
	
	private void setupViews(View v) {
		etDest = (EditText) v.findViewById(R.id.etDest);
	}
}
