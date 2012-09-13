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
		openUrlInBrowser("http://www.example.com/privacy-policy");
	}

	public void loadMobile(View view) {
		openUrlInBrowser("http://www.catholicdioceseapp.com/");
	}

	public void showDeveloperInfo(View view) {
		showSomeInformation(getString(R.string.about_button_developer_info), getString(R.string.about_button_developer_details));
	}

	public void showCredits(View view) {
		showSomeInformation(getString(R.string.about_button_credits), "Jeff Geerling");
	}

	/**
	 * Simple method to show an alert with a given title and message.
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
