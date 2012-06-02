package com.midwesternmac.catholicdiocese;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
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

        // Get the the selected prayer, convert it to a local URL, and load it.
        String HTMLFile = prayerHTMLFiles[position].toString();
        String file = "file:///android_asset/prayers/" + HTMLFile + ".html";
        Intent showContent = new Intent(getApplicationContext(),
        		JJGWebViewActivity.class);
        showContent.setData(Uri.parse(file));
        startActivity(showContent);
    }
}
