package com.midwesternmac.catholicdiocese;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class PrayerDetailActivity extends Activity {
	TextView prayerView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prayer);

		// Get and set the title for this prayer.
		String title = getIntent().getExtras().getString("title");
		setTitle(title);

		// Get and set the prayer content.
		String prayerHTML = getIntent().getExtras().getString("prayer");
		prayerView = (TextView)findViewById(R.id.prayerdetail);
		prayerView.setText(Html.fromHtml(prayerHTML));
	}
}
