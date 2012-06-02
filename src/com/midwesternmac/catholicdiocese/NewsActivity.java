package com.midwesternmac.catholicdiocese;

import java.net.URL;
import java.util.ArrayList;

import com.midwesternmac.catholicdiocese.DioceseNewsFeed;
import com.midwesternmac.catholicdiocese.JJGWebViewActivity;
import com.midwesternmac.catholicdiocese.Message;
import com.midwesternmac.catholicdiocese.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class NewsActivity extends ListActivity {
	private NewsListener listener = null;
    private Boolean NewsListenerIsRegistered = false;
    private NewsItemAdapter newsAdapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);

		// Load the feed into the list, or refresh it.
		// TODO - Use AsyncTask to download news on separate thread, and show
		// a ProgressDialog while downloading.
		// @see - http://developer.android.com/reference/android/os/AsyncTask.html
		if (newsAdapter == null) {
			initializeFeed();
		} else {
			refreshFeed();
		}

		// If we aren't yet listening, instantiate class listener.
		if (listener == null) {
			listener = new NewsListener();
		}
	}

	// Load the news feed into the ListView.
	private void initializeFeed() {
		// Get the application context.
		Context context = this.getApplicationContext();

		// Get the news feed, or load it if this is the first time fetching.
		DioceseNewsFeed newsfeed = DioceseNewsFeed.getInstance(context);

		// Create an array of for all the titles and descriptions.
		ArrayList<NewsItem> items = new ArrayList<NewsItem>();

		try {
			// Add the news stories to the array from the messages list.
			for (Message msg : newsfeed.messages) {
				NewsItem item = new NewsItem(msg.getTitle(), Html.fromHtml(
						msg.getDescription()).toString(),
						msg.getDateString());
				items.add(item);
			}
		}
		catch (Throwable t) {
			// Display an alert letting the user know the feed download failed.
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Download Error");
			alertDialog.setMessage("There was an error downloading the latest news. Please try again later, when you have a reliable internet connection.");
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Don't do anything.
				}
			});
			alertDialog.setIcon(R.drawable.icon);
			alertDialog.show();
		}

		// Build the rows by adding them to our custom ArrayAdapter.
		newsAdapter = new NewsItemAdapter(this, R.layout.row, items);
		this.setListAdapter(newsAdapter);
	}

	/**
	 * When refreshing the feed, to preserve the actual array adapter, and to
	 * allow the preservation of the user's scroll location in the ListView, we
	 * rebuild the feed from the messages list.
	 */
	public void refreshFeed() {
		// Get the application context.
		Context context = this.getApplicationContext();

		// Get and refresh the news feed (if it has been changed).
		DioceseNewsFeed newsfeed = DioceseNewsFeed.getInstance(context);
		newsfeed.refreshFeed(context);

		// If the feed needed to be refreshed, clear and rejigger the news.
		if (newsfeed.feedChanged) {
			// Clear the current NewsListAdapter.
			newsAdapter.clear();

			// Add the news stories to the array from the messages list.
			for (Message msg : newsfeed.messages) {
				NewsItem item = new NewsItem(msg.getTitle(), Html.fromHtml(
						msg.getDescription()).toString(),
						msg.getDateString());
				newsAdapter.add(item);
			}

			// This will refresh the list appropriately.
			newsAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Register NewsListener if it's not instantiated.
		if (!NewsListenerIsRegistered) {
			registerReceiver(listener, new IntentFilter("com.midwesternmac.catholicdiocese.UPDATELIST"));
			NewsListenerIsRegistered = true;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Unregister NewsListener if it's instantiated.
		if (NewsListenerIsRegistered) {
			unregisterReceiver(listener);
			NewsListenerIsRegistered = false;
		}
	}

	// News Listener - waits to get actions from other classes.
	protected class NewsListener extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("com.midwesternmac.catholicdiocese.UPDATELIST")) {
				// Refresh the feed with new data.
				refreshFeed();
			}
		}
	}

	// Add a menu that allows user to go refresh the news.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 1, 0, "Refresh").setIcon(R.drawable.ic_menu_refresh);
		return true;
	}

	// Define what to do if a given menu item is selected.
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1: // Refresh button.
			// Get the application context.
			Context context = this.getApplicationContext();

			// Get the news feed, then refresh it.
			DioceseNewsFeed newsfeed = DioceseNewsFeed.getInstance(context);
			newsfeed.refreshFeed(context);

			// Refresh the news list using an intent.
			Intent i = new Intent();
			i.setAction("com.midwesternmac.catholicdiocese.UPDATELIST");
			sendBroadcast(i);

			return true;
		}
		return false;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// Get the application context.
		Context context = this.getApplicationContext();

		// Get/load the news feed.
		DioceseNewsFeed newsfeed = DioceseNewsFeed.getInstance(context);

		// Get the URL of the selected news item, convert it to a string, and
		// load it.
		URL url = newsfeed.messages.get(position).getLink();
		String content = String.valueOf(url);
		Intent showContent = new Intent(getApplicationContext(),
				JJGWebViewActivity.class);
		showContent.setData(Uri.parse(content));
		startActivity(showContent);
	}

	// Custom ArrayAdapter to allow multiple fields per row in the ListView.
	public class NewsItemAdapter extends ArrayAdapter<NewsItem> {
		private ArrayList<NewsItem> items;

		public NewsItemAdapter(Context context, int textViewResourceId,
				ArrayList<NewsItem> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) this.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row, null);
			}

			NewsItem item = items.get(position);
			if (item != null) {
				TextView title = (TextView) v.findViewById(R.id.title);
				TextView description = (TextView) v
						.findViewById(R.id.description);
				TextView date = (TextView) v.findViewById(R.id.date);
				if (title != null) {
					title.setText(item.title);
				}
				if (description != null) {
					description.setText(item.description);
				}
				if (date != null) {
					date.setText(item.date);
				}
			}
			return v;
		}
	}

	// An individual news article (item), for use in our custom ArrayAdapter.
	public class NewsItem {
		public String title;
		public String description;
		public String date;

		public NewsItem(String title, String description, String date) {
			this.title = title;
			this.description = description;
			this.date = date;
		}
	}
}