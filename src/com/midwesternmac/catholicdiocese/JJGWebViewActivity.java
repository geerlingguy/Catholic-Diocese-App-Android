package com.midwesternmac.catholicdiocese;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @file JJGWebViewActivity
 * @author Jeff Geerling
 *
 * This class uses two strings for the title bar. Please add strings
 * with the following ids to your strings.xml file:
 *   - webview_loading (Shows while a page is being loaded).
 *   - webview_sharetext (Shows above a link in shared content).
 *
 * This class can be called from any other Activity using Intent. Example:
 *   String url = "http://www.example.com";
 *   Intent showContent = new Intent(getApplicationContext(), JJGWebViewActivity.class);
 *   showContent.setData(Uri.parse(url));
 *   startActivity(showContent);
 */

public class JJGWebViewActivity extends Activity {
	private WebView viewer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Request progress bar feature. (Must be before setContentView).
		getWindow().requestFeature(Window.FEATURE_PROGRESS);

		// Set content view to webview.xml
		setContentView(R.layout.webview);

		// Get the URL that we'll be loading later.
		Intent launchingIntent = getIntent();
		String content = launchingIntent.getData().toString();

		// Get the WebView.
		viewer = (WebView) findViewById(R.id.webview);

		// Set WebView options.
		viewer.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR); // Set default zoom level.
		viewer.getSettings().setJavaScriptEnabled(true); // Turn on JavaScript.
		viewer.getSettings().setUseWideViewPort(true); // Allow double-tap to zoom.
		viewer.getSettings().setBuiltInZoomControls(true); // Allow pinch-to-zoom.
		viewer.setWebViewClient(new NewsWebViewClient()); // Use our overridden webview client.

		// Progress bar.
		final Activity activity = this;
		viewer.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				// Add space before loading text and title to put it inside progress bar.
				activity.setTitle("\u0020\u0020" + getText(R.string.webview_loading));
				activity.setProgress(progress * 100);
				if (progress == 100) {
					activity.setTitle("\u0020\u0020" + viewer.getTitle());
				}
			}
		});

		// Load the URL.
		viewer.loadUrl(content);
	}

	// Allow back button to go back.
	public boolean onKeyDown(int keycode, KeyEvent event) {
		// If we have a history, go back one page.
		if ((keycode == KeyEvent.KEYCODE_BACK) && viewer.canGoBack()) {
			viewer.goBack();
			return true;
		}
		// Otherwise, pass the back button to the system.
		return super.onKeyDown(keycode, event);
	}

	// Override the WebViewClient to allow control over browser.
	private class NewsWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// Returning false here means all links will open in-app.
			return false;
		}
	}

	// Add a menu that allows user to go back, forward, or refresh.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu); // Add menu items, second value is the id, use this in the onCreateOptionsMenu
		menu.add(0, 1, 0, "Share").setIcon(R.drawable.ic_menu_share);
		menu.add(0, 2, 0, "Refresh").setIcon(R.drawable.ic_menu_refresh);
		menu.add(0, 3, 0, "Back").setIcon(R.drawable.ic_menu_back);
		menu.add(0, 4, 0, "Forward").setIcon(R.drawable.ic_menu_forward);
		return true;
	}

	// Define what to do if a given menu item is selected.
	public boolean onOptionsItemSelected(MenuItem item){ // Called when you tap a menu item
		switch (item.getItemId()){
			case 1: // Share button.
				Intent sharingIntent = new Intent(Intent.ACTION_SEND);
				sharingIntent.setType("text/html");
				String url = viewer.getUrl();
				String title = viewer.getTitle();
				String sharablehtml = "<p>" + getString(R.string.webview_sharetext) + "</p>"
						+ "<a href=\"" + url + "\">" + title + "</a>";
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(sharablehtml));
				startActivity(Intent.createChooser(sharingIntent,"Share using"));
				return true;
			case 2: // Refresh button.
				viewer.reload();
				return true;
			case 3: // Back button.
				if (viewer.canGoBack()) {
					viewer.goBack();
					return true;
				}
			case 4: // Forward button.
				if (viewer.canGoForward()) {
					viewer.goForward();
					return true;
				}
		}
		return false;
	}
}
