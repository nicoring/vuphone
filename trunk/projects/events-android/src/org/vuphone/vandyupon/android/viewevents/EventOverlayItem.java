/**
 * 
 */
package org.vuphone.vandyupon.android.viewevents;

import org.vuphone.vandyupon.android.eventstore.DBAdapter;

import android.database.Cursor;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

/**
 * Used to represent one event.
 * 
 * @author Hamilton Turner
 * 
 */
public class EventOverlayItem extends OverlayItem {
	/** Used for logging */
	// private static final String tag = Constants.tag;
	// private static final String pre = "EventOverlayItem: ";

	// String[] returnValues = { COLUMN_NAME, COLUMN_START_TIME,
	// COLUMN_END_TIME, COLUMN_LOCATION_LAT, COLUMN_LOCATION_LON,
	// COLUMN_IS_OWNER, COLUMN_SERVER_ID };

	String startTime, endTime;
	boolean isOwner;

	private EventOverlayItem(GeoPoint point, String title, String snippet) {
		super(point, title, snippet);
	}

	/**
	 * Used to create an EventOverlayItem
	 * 
	 * @param c
	 *            a Cursor that is assumed to be on a valid database row, which
	 *            will be used to fetch the values used to populate the
	 *            EventOverlayItem
	 * @return
	 */
	public static EventOverlayItem getItemFromRow(Cursor c) {
		int lat = c.getInt(c.getColumnIndex(DBAdapter.COLUMN_LOCATION_LAT));
		int lon = c.getInt(c.getColumnIndex(DBAdapter.COLUMN_LOCATION_LON));
		String name = c.getString(c.getColumnIndex(DBAdapter.COLUMN_NAME));
		String startTime = c.getString(c
				.getColumnIndex(DBAdapter.COLUMN_START_TIME));
		String endTime = c.getString(c
				.getColumnIndex(DBAdapter.COLUMN_END_TIME));
		int isOwnerInt = c.getInt(c
				.getColumnIndex(DBAdapter.COLUMN_IS_OWNER));
		
		boolean isOwner = false;
		if (isOwnerInt == 1)
			isOwner = true;

		final GeoPoint location = new GeoPoint(lat, lon);
		EventOverlayItem eoi = new EventOverlayItem(location, name, "");
		eoi.setProperties(startTime, endTime, isOwner);

		return eoi;
	}

	/** @see com.google.android.maps.OverlayItem#getMarker(int) */
	@Override
	public Drawable getMarker(int bitStateset) {
		return null;
	}

	/** Used to set the properties for this event */
	private void setProperties(String start, String end, boolean owner) {
		startTime = start;
		endTime = end;
		isOwner = owner;
	}
	
	public String getStartTime() {
		return startTime;
	}
	
	public String getEndTime() {
		return endTime;
	}
	
	public boolean getIsOwner() {
		return isOwner;
	}
}