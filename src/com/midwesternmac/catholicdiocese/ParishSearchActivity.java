package com.midwesternmac.catholicdiocese;

import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ParishSearchActivity extends ListActivity {
	private EditText filterText = null;
	ArrayAdapter<Parish> adapter = null;
	ParishData parishData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.parish_search);

		// Attach a text filter to the view (for search).
		filterText = (EditText) findViewById(R.id.search_parishes);
		filterText.addTextChangedListener(filterTextWatcher);
		filterText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if (!hasFocus) {
					hideKeyboard();
				}
			}
		});

		// Build array adapter with parish names and ids for parish lookup.
		parishData = new ParishData(getApplicationContext());
		adapter = new ParishAdapter(this, R.layout.parish_search_row, parishData.getAllParishes());
		setListAdapter(adapter);

	}

	// Called when an item in the list is tapped.
	@Override
	protected void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);

		// Disable the soft keyboard.
		hideKeyboard();

		// Get the TextView.
		TextView name = (TextView) view.findViewById(R.id.parish_name);

		// Get the parish by parish ID.
		Parish parish = parishData.getParishByID((String) name.getTag());

		// Show parish details in new parish detail view.
		Intent showParish = new Intent(getApplicationContext(),
				ParishDetailActivity.class);
		showParish.putExtra("Parish", parish);
		startActivity(showParish);
	}

	// Define a custom array adapter.
	public class ParishAdapter extends ArrayAdapter<Parish> {
		private List<Parish> parishes;
		private Parish parish;

		// Note: The passed in 'objects' are just the parish names (these are
		// then used by the built in getFilter() filtering to filter results.
		public ParishAdapter(Context context, int textViewResourceId, List<Parish> parishList) {
			super(context, textViewResourceId, parishList);
			parishes = parishList;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// For performance, only inflate view if it's not already loaded.
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = getLayoutInflater();
				row = inflater.inflate(R.layout.parish_search_row, parent, false);
			}

			// If there's a parish at this location already (e.g. for filtering
			// purposes), just use the existing parish.
			if (getItem(position) != null) {
				parish = getItem(position);
			} else {
				parish = parishes.get(position);
			}

			// Set the parish name.
			TextView name = (TextView) row.findViewById(R.id.parish_name);
			name.setText(parish.getName());

			// Set the parish id using setTag.
			name.setTag(parish.getParishID());

			// Set the parish addrss.
			TextView address = (TextView) row.findViewById(R.id.parish_city);
			address.setText(parish.getCity() + ", " + parish.getState());

			return row;
		}
	}

	// Define text watcher for filtering/searching parishes.
	private TextWatcher filterTextWatcher = new TextWatcher() {
		public void afterTextChanged(Editable s) {
			// Nothing to see here.
		}

		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// Nothing to see here.
		}

		public void onTextChanged(CharSequence s, int start, int before, int count) {
			adapter.getFilter().filter(s);
		}

	};

	// Hide the soft keyboard.
	private void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(filterText.getWindowToken(), 0);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		filterText.removeTextChangedListener(filterTextWatcher);
	}
}
