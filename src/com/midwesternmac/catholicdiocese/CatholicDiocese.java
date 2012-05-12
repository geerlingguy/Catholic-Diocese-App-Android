package com.midwesternmac.catholicdiocese;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class CatholicDiocese extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, ParishActivity.class);
        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("artists").setIndicator("Parishes",
                          res.getDrawable(R.drawable.ic_tab_parishes))
                      .setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the News tab.
        intent = new Intent().setClass(this, NewsActivity.class);
        spec = tabHost.newTabSpec("albums").setIndicator("News",
                          res.getDrawable(R.drawable.ic_tab_news))
                      .setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the Prayers tab.
        intent = new Intent().setClass(this, PrayerActivity.class);
        spec = tabHost.newTabSpec("songs").setIndicator("Prayers",
                          res.getDrawable(R.drawable.ic_tab_prayers))
                      .setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the About tab.
        intent = new Intent().setClass(this, AboutActivity.class);
        spec = tabHost.newTabSpec("songs").setIndicator("About",
                          res.getDrawable(R.drawable.ic_tab_about))
                      .setContent(intent);
        tabHost.addTab(spec);

        // Start on the Parishes tab (index 0).
        tabHost.setCurrentTab(0);
    }
}