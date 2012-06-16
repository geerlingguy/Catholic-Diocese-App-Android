package com.midwesternmac.catholicdiocese;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PrayerActivity extends ListActivity {
	ArrayAdapter<String> listAdapter;
	String[] prayerTitles;
	String[] prayerHTMLFiles;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Define titles (these will be rows in List).
		prayerTitles = new String[] {
				"Examen",
				"Falling In Love",
				"Ignatian Spirituality - English",
				"Espiritualidad Ignaciana - Espa–ol",
				"The Life of Ignatius - English",
				"La Vida de Ignacio - Espa–ol",
				"Prayer for Generosity",
				"Suscipe"
		};

		// Define files in which prayers are located (to be opened on tap).
		prayerHTMLFiles = new String[] {
				"examen",
				"falling-in-love",
				"ignatian-spirituality-en",
				"ignatian-spirituality-es",
				"life-of-ignatius-en",
				"life-of-ignatius-es",
				"prayer-for-generosity",
				"suscipe",
		};

		listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, prayerTitles);
		setListAdapter(listAdapter);
	}

	/** Called when an item in the list is tapped. */
	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);

		// Get the selected prayer, send title/HTML to prayer detail activity.
		AssetManager assetManager = this.getAssets();
		try {
			InputStream inputStream = assetManager.open("prayers/" + prayerHTMLFiles[position].toString() + ".html");
			String prayerHTML = convertStreamToString(inputStream);
			String title = prayerTitles[position].toString();
			// Use PrayerDetailActivity rather than a proper WebView, even
			// though the data is HTML, because Android's WebView stinks in
			// terms of CSS/width/layout.
			Intent showPrayer = new Intent(getApplicationContext(), PrayerDetailActivity.class);
			showPrayer.putExtra("title", title);
			showPrayer.putExtra("prayer", prayerHTML);
			startActivity(showPrayer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Convert an InputStream to a String.
	String convertStreamToString(InputStream inputStream) {
	    try {
	        return new Scanner(inputStream).useDelimiter("\\A").next();
	    } catch (NoSuchElementException e) {
	        return "";
	    }
	}
}
