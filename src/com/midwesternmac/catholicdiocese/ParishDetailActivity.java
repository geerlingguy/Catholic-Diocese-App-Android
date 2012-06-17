package com.midwesternmac.catholicdiocese;

import java.util.LinkedList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ParishDetailActivity extends ListActivity {
	ArrayAdapter<String> listAdapter;
	String[] parishDetails;
	String address;
	Boolean hasFaxNumber;
	Boolean hasWebsiteURL;
	Parish parish;

	// Called when the activity is first created.
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get the Parish that we'll load into the detail list.
		Intent launchingIntent = getIntent();
		parish = launchingIntent.getParcelableExtra("Parish");

		// Set the activity title to the parish name.
		this.setTitle(parish.getName());

		// Check whether this parish has a fax number.
		if (parish.getFaxNumber() == null) {
			hasFaxNumber = false;
		} else if (parish.getFaxNumber().trim().length() == 0) {
			hasFaxNumber = false;
		} else {
			hasFaxNumber = true;
		}

		// Check whether this parish has a website URL.
		if (parish.getWebsiteURL() == null) {
			hasWebsiteURL = false;
		} else {
			hasWebsiteURL = true;
		}

		// Define row text.
		final List<String[]> parishDataList = new LinkedList<String[]>();
		parishDataList.add(new String[] { parish.getFullAddress(), "Show in Maps app." });
		parishDataList.add(new String[] { "Phone: " + parish.getPhoneNumber(), "Tap to call." });
		if (hasFaxNumber) {
			parishDataList.add(new String[] { "Fax: " + parish.getFaxNumber(), null });
		}
		if (hasWebsiteURL) {
			parishDataList.add(new String[] { "More Information", "Tap to view website." });
		}

		// Set the list adapter to allow for use of simple_list_item_2 layout.
		setListAdapter(new ArrayAdapter<String[]>(this, android.R.layout.simple_list_item_2, android.R.id.text1, parishDataList) {
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					// Must always return just a View.
					View view = super.getView(position, convertView, parent);
					// android.R.layout.simple_list_item_2's source defines a
					// TwoLineListItem with two TextViews - text1 and text2.
					// TwoLineListItem listItem = (TwoLineListItem) view;
					String[] entry = parishDataList.get(position);
					TextView text1 = (TextView) view.findViewById(android.R.id.text1);
					TextView text2 = (TextView) view.findViewById(android.R.id.text2);
					text1.setText(entry[0]);
					text2.setText(entry[1]);
					return view;
				}
		});
	}

	// Called when an item in the list is tapped.
	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
		// Address.
		if (position == 0) {
			showDirectionsToParish();
		}
		// Phone number.
		if (position == 1) {
			callParishPhone();
		}
		// Fax number (may or may not be present).
		if (position == 2 && hasFaxNumber) {
			// Nobody likes calling fax machines, so don't do anything here.
		}
		// Website (could be in position 2 if no fax number shown).
		if ((position == 2 && !hasFaxNumber) || (position == 3)) {
			showParishWebsite();
		}
	}

	/**
	 * Tap handling methods for onListItemClick().
	 */

	/** Open the parish coordinates in Android's Maps app. */
	private void showDirectionsToParish() {
		// Use the geo URI format with lat, lon, zoom level and name for marker.
		String uri = String.format("geo:0,0?z=15&q=%s,%s (%s)", parish.getLatitude(), parish.getLongitude(), parish.getName());
		Intent showMap = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		startActivity(showMap);
	}

	/** Call the parish's phone number. */
	private void callParishPhone() {
		String phoneNumber = parish.getPhoneNumber().replaceAll( "[^\\d]", "" );
		String phoneURL = "tel:" + phoneNumber;
		Intent callParish = new Intent(Intent.ACTION_CALL, Uri.parse(phoneURL));
		startActivity(callParish);
	}

	/** Open the parish website in a webview. */
	private void showParishWebsite() {
		Intent showWebsite = new Intent(getApplicationContext(), JJGWebViewActivity.class);
		showWebsite.setData(Uri.parse(parish.getWebsiteURL()));
		startActivity(showWebsite);
	}
}
