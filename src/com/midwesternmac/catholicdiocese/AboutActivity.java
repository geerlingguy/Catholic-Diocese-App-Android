package com.midwesternmac.catholicdiocese;

import com.midwesternmac.catholicdiocese.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class AboutActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.about);
	}

	/**
	 * The following three methods react to buttons defined in about.xml.
	 */
	public void loadPrivacy(View view) {
		openUrlInBrowser("http://www.jesuit.org/blog/index.php/privacypolicy/");
	}

	public void loadMobile(View view) {
		openUrlInBrowser("http://www.jesuit.org/");
	}

	public void showDeveloperInfo(View view) {
		showSomeInformation("Developer Information", "App based on Catholic Diocese App, an open source project from Open Source Catholic, sponsored by Midwestern Mac, LLC.");
	}

	public void showCredits(View view) {
		showSomeInformation("Credits", "Jeff Geerling, Kaitlyn McCarthy Schnieders, Marcus Bleech, Kathreja Sarfati & Derrick Portillo.");
	}

	/**
	 * Simple function to show an alert with a given title and message.
	 * 
	 * @param title
	 * @param message
	 */
	private void showSomeInformation(String title, String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(AboutActivity.this).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		alertDialog.show();
	}

	/**
	 * Simple function to open a given URL in the Android browser.
	 * 
	 * @param url
	 */
	private void openUrlInBrowser(String url) {
		Intent browser = new Intent(Intent.ACTION_VIEW);
		browser.setData(Uri.parse(url));
		startActivity(browser);
	}
}
