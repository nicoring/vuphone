package edu.vanderbilt.vuphone.android.campusmaps;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class Main extends MapActivity {

	private static final int MENU_DROP_PIN = 3;
	private static final int MENU_SHOW_BUILDINGS = 2;
	private static final int MENU_BUILDING_LIST = 1;
	private static final int MENU_MAP_MODE = 0;
	LinearLayout linearLayout_;
	MapView mapView_;
	ZoomControls mZoom_;
	MapController mc_;
	GeoPoint p_;
	PathOverlay poLayer_ = null;

	/*
	 * Called when the activity is first created. Enables user to zoom in/out of
	 * the center of the screen. Also sets the map to open while viewing
	 * Vanderbilt Campus.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mapView_ = (MapView) findViewById(R.id.mapview);
		mapView_.setBuiltInZoomControls(true);

		poLayer_ = new PathOverlay(mapView_);

		// Just some demo paths to test for now
		poLayer_.StartNewPath(new GeoPoint(36144875, -86806723));
		poLayer_.AddPoint(new GeoPoint(36146071, -86804298));
		poLayer_.StartNewPath(new GeoPoint(36143411, -86806401));
		poLayer_.AddPoint(new GeoPoint(36143238, -86804727));
		poLayer_.AddPoint(new GeoPoint(36143143, -86803257));
		poLayer_.AddPoint(new GeoPoint(36143429, -86802624));
		poLayer_.AddPoint(new GeoPoint(36143935, -86802587));

		// Attempt to draw Wesley Place from GML data in EPSG900913 format from
		// vu.gml, just testing / demoing.
		poLayer_.StartNewPath(EPSG900913ToGeoPoint(-9662429.695230,
				4320719.417812));
		poLayer_
				.AddPoint(EPSG900913ToGeoPoint(-9662420.185221, 4320683.476196));
		poLayer_
				.AddPoint(EPSG900913ToGeoPoint(-9662417.200911, 4320672.193037));
		poLayer_
				.AddPoint(EPSG900913ToGeoPoint(-9662417.071184, 4320672.178321));
		poLayer_
				.AddPoint(EPSG900913ToGeoPoint(-9662395.440964, 4320669.572643));
		poLayer_
				.AddPoint(EPSG900913ToGeoPoint(-9662395.711297, 4320667.316003));
		poLayer_
				.AddPoint(EPSG900913ToGeoPoint(-9662386.352760, 4320666.189571));
		poLayer_
				.AddPoint(EPSG900913ToGeoPoint(-9662386.082410, 4320668.444238));
		poLayer_
				.AddPoint(EPSG900913ToGeoPoint(-9662346.924362, 4320663.727702));
		poLayer_
				.AddPoint(EPSG900913ToGeoPoint(-9662359.954998, 4320711.017158));
		poLayer_
				.AddPoint(EPSG900913ToGeoPoint(-9662381.825093, 4320713.650537));
		poLayer_
				.AddPoint(EPSG900913ToGeoPoint(-9662389.499083, 4320714.573825));
		poLayer_
				.AddPoint(EPSG900913ToGeoPoint(-9662429.695230, 4320719.417812));

		mc_ = mapView_.getController();

		// Vanderbilt GPS coordinates, used to start the map at a Vanderbilt
		// Location.
		double lat = 36.142830;
		double lng = -86.804437;

		p_ = new GeoPoint((int) (lat * 1000000), (int) (lng * 1000000));

		mc_.animateTo(p_);
		mc_.setZoom(17);
		mapView_.invalidate();

		// Set the GPS Listener
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		try {
			onPositionChange(lm
					.getLastKnownLocation(LocationManager.GPS_PROVIDER));
		} catch (Exception e) {
			// TODO(corespace): Handle specific errors.
		}

		LocationListener ll = new LocationListener() {
			public void onLocationChanged(Location location) {
				onPositionChange(location);
			}

			public void onProviderDisabled(String provider) {
				Log.d("vandy", "GPS Disabled");
			}

			public void onProviderEnabled(String provider) {
				Log.d("vandy", "GPS Enabled");
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				if (extras != null) {
					Log.d("vandy", "# of satellites:"
							+ extras.getInt("satellites"));
				}
			}
		};

		// Request to be notified whenever the user moves
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 2, ll);
	}

	/*
	 * Called by the GPS service to inform us of the current position
	 */
	private void onPositionChange(Location l) {
		echo("GPS: " + l.getLatitude() + "," + l.getLongitude() + " -> "
				+ l.getAccuracy() + "m");
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	/**
	 * Creates a "Map Mode" option when menu is clicked.
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, MENU_MAP_MODE, "Map Mode");
		menu.add(0, 1, MENU_BUILDING_LIST, "List Buildings");
		menu.add(0, 2, MENU_SHOW_BUILDINGS, "Show Buildings");
		menu.add(0, 3, MENU_DROP_PIN, "Drop Pin");
		return true;
	}

	/**
	 * Called when an Menu item is clicked
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		super.onMenuItemSelected(featureId, item);

		switch (item.getItemId()) {

		case (MENU_MAP_MODE):
			echo("Map Mode");
			break;

		case (MENU_BUILDING_LIST):
			echo("Building list");
			break;

		case (MENU_SHOW_BUILDINGS):
			echo("Show Buildings");
			break;

		case (MENU_DROP_PIN):
			drop_pin(mapView_.getMapCenter());
			break;
		}
		return true;
	}

	/**
	 * Used to set a marker image on the map
	 */
	public void drop_pin(GeoPoint p) {
		MapMarker m = new MapMarker(getBaseContext(), getResources(), mapView_,
				p);
		m.drop_pin();
	}

	/**
	 * Prints a message to the screen for a few seconds
	 */
	public void echo(String s) {
		Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Method for converting from EPSG900913 format used by vu.gml to latitude
	 * and longitude. Based on reversing a C# function from
	 * http://www.cadmaps.com/gisblog/?cat=10
	 * 
	 * @param x
	 *            - 1st EPSG900913 coordinate
	 * @param y
	 *            - 2nd EPSG900913 coordinate
	 * @return GeoPoint at input location
	 */
	public GeoPoint EPSG900913ToGeoPoint(double x, double y) {
		double longitude = x / (6378137.0 * Math.PI / 180);
		double latitude = ((Math.atan(Math.pow(Math.E, (y / 6378137.0))))
				/ (Math.PI / 180) - 45) * 2.0;
		Log.d("LatLong", "Lat = " + latitude + " Long = " + longitude);
		return new GeoPoint((int) (latitude * 1E6),
				(int) (longitude * 1E6));
	}

}