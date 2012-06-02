package com.midwesternmac.catholicdiocese;

import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.midwesternmac.catholicdiocese.JJGWebViewActivity;
import com.midwesternmac.catholicdiocese.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;

public class ParishActivity extends MapActivity {
	List<Overlay> mapoverlays;
	Drawable drawable;
	ParishMapOverlay itemizedoverlay;
	LinearLayout linearlayout;
	MapView map;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.map);

		// Get the mapview, and add zoom controls.
		map = (MapView) findViewById(R.id.map);
		map.setBuiltInZoomControls(true);

		// Set the Map's center location and default zoom level.
		MapController mapcontroller = map.getController();
		mapcontroller.setCenter(new GeoPoint(38631355, -95396881));
		mapcontroller.setZoom(4);

		mapoverlays = map.getOverlays();
		drawable = map.getResources().getDrawable(R.drawable.map_marker);
		itemizedoverlay = new ParishMapOverlay(drawable, this);

		// Get the parishes and add them to the map.
//		for (Message msg : newsfeed.messages) {
//			double latitude = Double.parseDouble(msg.getGeolatitude());
//			double longitude = Double.parseDouble(msg.getGeolongitude());
//
//			GeoPoint point = new GeoPoint((int)(latitude * 1e6), (int)(longitude * 1e6));
//
//			// Prepare the description by shortening it.
//			StringBuilder description = new StringBuilder(msg.getDescription());
//			description.setLength(140);
//			description.append("...");
//
//			ParishMapItem parishmapitem = new ParishMapItem(point, msg.getTitle(), description.toString(), msg.getLink().toString());
//			itemizedoverlay.addOverlay(parishmapitem);
//		}

		// At the end of adding all points, add the itemizedoverlay.
		mapoverlays.add(itemizedoverlay);
	}

	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}

	private class ParishMapOverlay extends ItemizedOverlay<ParishMapItem> {
		private Context mContext = null;
		private ArrayList<ParishMapItem> overlays = new ArrayList<ParishMapItem>();

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

		@Override
		protected boolean onTap(int index) {
			ParishMapItem item = overlays.get(index);
			AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
			dialog.setTitle(item.getTitle());
			dialog.setMessage(item.getSnippet());
			final String url = item.getLink();
			dialog.setCancelable(true);
			dialog.setPositiveButton(R.string.mapview_open, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					Intent showContent = new Intent(getApplicationContext(), JJGWebViewActivity.class);
					showContent.setData(Uri.parse(url));
					startActivity(showContent);
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
	}

	private class ParishMapItem extends OverlayItem {
		private String mLink;
		public ParishMapItem(GeoPoint point, String title, String snippet, String mLink) {
			super(point, title, snippet);
			this.mLink = mLink;
		}
		public String getLink() {
			return mLink;
		}
	}
}