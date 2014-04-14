package com.codepath.travelplanner.dialogs;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import com.codepath.travelplanner.R;

/**
 * FiltersDialog - dialog containing the filters (eg. activity, distance, price, etc)
 */
public class FiltersDialog extends BaseWizardDialog {
	/** views */
	private EditText etActivity;
	private Spinner spDistances;
	private Spinner spPrices;

	/** static function that creates a new filters dialog */
	public static FiltersDialog newInstance() {
		FiltersDialog dialog = new FiltersDialog();
		return dialog;
	}

	@Override
	protected int getNegativeBtnTextId() {
		return R.string.cancel;
	};

	@Override
	protected int getDialogResourceId() {
		return R.layout.dialog_filters;
	}

	@Override
	protected void setupViews(View v) {
		etActivity = (EditText) v.findViewById(R.id.etActivity);
		spDistances = (Spinner) v.findViewById(R.id.spDistances);
		spPrices = (Spinner) v.findViewById(R.id.spPrices);
	}

	@Override
	protected void onPositiveClick() {
		listener.onFilterPositive(); // TODO: pass data back
	}
}
