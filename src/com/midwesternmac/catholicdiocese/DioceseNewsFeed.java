package com.midwesternmac.catholicdiocese;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class DioceseNewsFeed {
	private static DioceseNewsFeed instance = null;
	public boolean feedChanged = false;

	private DioceseNewsFeed() {}

	// Messages list that can be accessed anywhere in the app.
	public List<Message> messages;

	// Called by "CnlNewsFeed newsfeed = CnlNewsFeed.getInstance(context);"
	public static synchronized DioceseNewsFeed getInstance(Context context) {
		if (null == instance) {
			// Create a new instance.
			instance = new DioceseNewsFeed();

			// Load the parser and try parsing the messages.
			try {
				BaseFeedParser parser = new BaseFeedParser();
				instance.messages = parser.parse();
			}
			catch (Throwable t) {
				// Sadly, something must've gone wrong.
			}
		}
		return instance;
	}

	/**
	 * Allow callers to refresh the news feed.
	 */
	public void refreshFeed(Context context) {
		// Get app preferences.
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		long lastNewsRefresh = preferences.getLong("LastNewsRefresh", 0);

		// Only refresh the feed if the last refresh was more than 5 min ago.
		long fiveMinutesPast = System.currentTimeMillis() - 300;
		if (lastNewsRefresh == 0 || (fiveMinutesPast > lastNewsRefresh)) {
			try {
				// Update the messages list.
				BaseFeedParser parser = new BaseFeedParser();
				instance.messages = parser.parse();
			}
			catch (Throwable t){
				Log.e("CnlNewsFeed", t.getMessage());
				feedChanged = false;
				return;
			}

			// If more than ten messages in the feed, it was successful.
			if (instance.messages.size() > 10) {
				// Update the timestamp.
				long currentTime = System.currentTimeMillis();
				SharedPreferences.Editor editor = preferences.edit();
				editor.putLong("LastNewsRefresh", currentTime);
				editor.commit(); // Write the preference.
			}

			// Tell the caller the feed has been changed.
			feedChanged = true;
		}
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("Clone is not allowed.");
	}
}
