package com.midwesternmac.catholicdiocese;

import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import com.midwesternmac.catholicdiocese.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

public class ParishActivity extends MapActivity {
	List<Overlay> mapoverlays;
	List<Parish> parishes;
	Drawable drawable;
	ParishMapOverlay itemizedoverlay;
	MyLocationOverlay myLocationOverlay;
	LinearLayout linearlayout;
	MapView map;
	MapController mapController;
	LocationManager locationManager;
	LocationListener locationListener;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.map);

		// Get the mapview, and add zoom controls.
		map = (MapView) findViewById(R.id.map);
		map.setBuiltInZoomControls(true);

		// @config - Set the Map's center location and default zoom level.
		// TODO: Move these defaults into res/values/other.xml.
		mapController = map.getController();
		mapController.setCenter(new GeoPoint(38631355, -95396881));
		mapController.setZoom(4);

		mapoverlays = map.getOverlays();
		drawable = map.getResources().getDrawable(R.drawable.map_marker);
		itemizedoverlay = new ParishMapOverlay(drawable, this);

		// Get all the parishes from the database.
		ParishData parishData = new ParishData(getApplicationContext());
		parishes = parishData.getAllParishes();
		for (Parish parish : parishes) {
			// Convert latitude and longitude to a point on the map.
			double latitude = (double)parish.getLatitude();
			double longitude = (double)parish.getLongitude();
			GeoPoint point = new GeoPoint((int)(latitude * 1e6), (int)(longitude * 1e6));

			// Build the map item and add it to the overlay.
			ParishMapItem mapItem = new ParishMapItem(point, parish.getName(), parish.getFullAddress(), parish);
			itemizedoverlay.addOverlay(mapItem);
		}

		// At the end of adding all points, add the itemizedoverlay.
		mapoverlays.add(itemizedoverlay);

		// Put a blue dot on the user's location.
		myLocationOverlay = new MyLocationOverlay(this, map);
		myLocationOverlay.enableMyLocation();
		myLocationOverlay.runOnFirstFix(new Runnable() {
			public void run() {
				setMapCenterOnUserLocation();
			}
		});
		map.getOverlays().add(myLocationOverlay);
		map.postInvalidate();
	}

	@Override
	public void onPause() {
		super.onPause();
		myLocationOverlay.disableMyLocation();
	}

	@Override
	public void onResume() {
		super.onResume();
		myLocationOverlay.enableMyLocation();
	}

	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}

	// Attempt to retrieve the user's location and center the map on it.
	private void setMapCenterOnUserLocation() {
		// Get the user's current location if possible.
		if (myLocationOverlay.getMyLocation() != null) {
			mapController.setZoom(11);
			mapController.animateTo(myLocationOverlay.getMyLocation());
		} else {
			ParishActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					AlertDialog dialog = new AlertDialog.Builder(ParishActivity.this).create();
					dialog.setTitle(getString(R.string.mapview_location_not_found));
					dialog.setMessage(getString(R.string.mapview_location_not_found_message));
					dialog.setButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							return;
						}
					});
					dialog.show();
				}
			});
		}
	}

	private class ParishMapOverlay extends ItemizedOverlay<ParishMapItem> {
		private Context mContext = null;
		private ArrayList<ParishMapItem> overlays = new ArrayList<ParishMapItem>();
		private long systemTime = System.currentTimeMillis();

		public ParishMapOverlay(Drawable defaultMarker, Context context) {
			super(boundCenterBottom(defaultMarker));
			mContext = context;
		}

		@Override
		protected ParishMapItem createItem(int i) {
			return overlays.get(i);
		}

		public void addOverlay(ParishMapItem overlay) {
			overlays.add(overlay);
			populate();
		}

		@Override
		public int size() {
			return overlays.size();
		}

		// Open dialog with parish information when a user taps on a marker.
		@Override
		protected boolean onTap(int index) {
			ParishMapItem item = overlays.get(index);
			AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
			dialog.setTitle(item.getTitle());
			dialog.setMessage(item.getSnippet());
			final Parish parish = item.getParish();
			dialog.setCancelable(true);
			dialog.setPositiveButton(R.string.mapview_open, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// Show parish details in new parish detail view.
					Intent showParish = new Intent(getApplicationContext(), ParishDetailActivity.class);
					showParish.putExtra("Parish", parish);
					startActivity(showParish);
				}
			});
			dialog.setNegativeButton(R.string.mapview_dismiss, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			dialog.show();
			return true;
		}

		// Allow double-tap to zoom.
		@Override
		public boolean onTouchEvent(MotionEvent event, MapView mapView) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if ((System.currentTimeMillis() - systemTime) < ViewConfiguration.getDoubleTapTimeout()) {
					mapController.zoomInFixing((int) event.getX(), (int) event.getY());
				}
				break;
			case MotionEvent.ACTION_UP:
				systemTime = System.currentTimeMillis();
				break;
			}

			return false;
		}
	}

	private class ParishMapItem extends OverlayItem {
		private Parish mParish;
		public ParishMapItem(GeoPoint point, String title, String snippet, Parish mParish) {
			super(point, title, snippet);
			this.mParish = mParish;
		}
		public Parish getParish() {
			return mParish;
		}
	}

	// Create a menu for this activity.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.addSubMenu(0, 0, 0, getString(R.string.mapview_menu_search));
		menu.addSubMenu(0, 1, 0, getString(R.string.mapview_menu_locate));
		return true;
	}

	// Respond to menu item taps.
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case 0: // Search Parishes.
			Intent openParishSearch = new Intent(this, ParishSearchActivity.class);
			startActivity(openParishSearch);
			break;
		case 1: // Locate Me.
			setMapCenterOnUserLocation();
			break;
		}
		return true;
	}
}
