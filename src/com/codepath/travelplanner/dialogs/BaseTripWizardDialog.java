package com.codepath.travelplanner.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.codepath.travelplanner.R;
import com.codepath.travelplanner.models.Trip;

/**
 * BaseTripWizardDialog - base dialog for the create-a-new-trip "wizard" dialogs
 */
public abstract class BaseTripWizardDialog extends DialogFragment {
	/** start place extra */
	protected static final String START_EXTRA = "start";
	/** name of trip in the bundle */
	protected static final String TRIP_EXTRA = "trip";

	/** trip object that the wizard is making */
	protected Trip newTrip = new Trip();

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View v = inflater.inflate(getDialogResourceId(), null);
		setupViews(v);

		builder.setView(v);
		builder.setPositiveButton(getPositiveBtnTextId(), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				onPositiveClick();
			}
		});

		builder.setNegativeButton(getNegativeBtnTextId(), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				getDialog().cancel();
			}
		});

		return builder.create();
	}

	/** get the resource id of the layout associated with this dialog */
	protected abstract int getDialogResourceId();

	/** get text id for the positive button */
	protected int getPositiveBtnTextId() {
		return R.string.next;
	};

	/** get text id for the negative button */
	protected int getNegativeBtnTextId() {
		return R.string.back;
	};

	/** setup the views */
	protected abstract void setupViews(View v);

	/** callback for when the positive button is clicked */
	protected abstract void onPositiveClick();
}
