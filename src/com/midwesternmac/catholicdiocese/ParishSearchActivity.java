package com.midwesternmac.catholicdiocese;

import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
	ArrayAdapter<String> adapter = null;
	List<Parish> parishes;
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

		// Load the list of parishes.
		parishData = new ParishData(getApplicationContext());
		parishes = parishData.getAllParishes();

		// Build array adapter with parish names and ids for parish lookup.
		adapter = new ParishAdapter(this, R.layout.parish_search_row, getParishNameArrayList());
		setListAdapter(adapter);
		
	}

	// Called when an item in the list is tapped.
	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);

		// Disable the soft keyboard.
		hideKeyboard();

		// Get the TextView.
		TextView name = (TextView) view.findViewById(R.id.parish_name);

		// Get the parish by parish ID.
		// TODO
		Parish parish = parishData.getParishByID((String) name.getTag());
		// Parish parish = parishes.get(position);

		// Show parish details in new parish detail view.
		Intent showParish = new Intent(getApplicationContext(), ParishDetailActivity.class);
		showParish.putExtra("Parish", parish);
		startActivity(showParish);
	}

	// Get a list of all the parish names.
	private String[] getParishNameArrayList() {
		String[] parishTitles = new String[parishes.size()];
		for (int i = 0; i < parishes.size(); i++) {
			Parish parish = parishes.get(i);
			parishTitles[i] = parish.getName();
		}
		return parishTitles;
	}

	// Define a custom array adapter.
	public class ParishAdapter extends ArrayAdapter<String> {
		public ParishAdapter(Context context, int textViewResourceId, String[] objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO: If the view is already in place, just return it. For some
			// reason, the searching isn't working with this method of building
			// the list/arrayadapter...

			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.parish_search_row, parent, false);

			// Set the parish name.
			TextView name = (TextView) row.findViewById(R.id.parish_name);
			name.setText(parishes.get(position).getName());

			// Set the parish id using setTag.
			name.setTag(parishes.get(position).getParishID());

			// Set the parish addrss.
			TextView address = (TextView) row.findViewById(R.id.parish_city);
			address.setText(parishes.get(position).getCity() + ", " + parishes.get(position).getState());

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
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(filterText.getWindowToken(), 0);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		filterText.removeTextChangedListener(filterTextWatcher);
	}
}
