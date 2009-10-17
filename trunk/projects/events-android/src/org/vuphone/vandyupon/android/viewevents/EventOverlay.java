/**
 * 
 */
package org.vuphone.vandyupon.android.viewevents;

import org.vuphone.vandyupon.android.Constants;
import org.vuphone.vandyupon.android.eventstore.DBAdapter;
import org.vuphone.vandyupon.android.filters.FilterChangedListener;
import org.vuphone.vandyupon.android.filters.PositionFilter;
import org.vuphone.vandyupon.android.filters.TagsFilter;
import org.vuphone.vandyupon.android.filters.TimeFilter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;

import com.google.android.maps.ItemizedOverlay;

/**
 * Contains the {@link EventOverlayItem}s. Holds a handle to the database, and
 * holds the current display filters. Every time the filters are updated, the
 * database is re-queried
 * 
 * @author Hamilton Turner
 * 
 */
public class EventOverlay extends ItemizedOverlay<EventOverlayItem> implements
		FilterChangedListener {
	/** Used for logging */
	private static final String tag = Constants.tag;
	private static final String pre = "EventOverlay: ";

	/** Used for filtering events */
	private PositionFilter positionFilter_;
	private TimeFilter timeFilter_;
	private TagsFilter tagsFilter_;

	/** Used to get events that match the current filters */
	private DBAdapter database_;

	/** Used to point to the current row in the database */
	private Cursor eventCursor_;

	private static ShapeDrawable defaultDrawable_;

	private Rect touchableBounds = new Rect();

	static {
		defaultDrawable_ = new ShapeDrawable(new RectShape());
		defaultDrawable_.setBounds(10, 10, 10, 10);
		defaultDrawable_.setIntrinsicHeight(50);
		defaultDrawable_.setIntrinsicWidth(20);

	}

	/**
	 * 
	 * @param positionFilter
	 *            Can be NULL.
	 * @param timeFilter
	 *            Can be NULL.
	 * @param tagsFilter
	 *            Can be NULL.
	 * @param context
	 */
	public EventOverlay(PositionFilter positionFilter, TimeFilter timeFilter,
			TagsFilter tagsFilter, Context context) {
		super(boundCenterBottom(defaultDrawable_));

		positionFilter_ = positionFilter;
		timeFilter_ = timeFilter;
		tagsFilter_ = tagsFilter;

		if (positionFilter_ != null)
			positionFilter_.registerListener(this);

		database_ = new DBAdapter(context);
		database_.openReadable();
		eventCursor_ = database_.getAllEntries(positionFilter_, timeFilter_,
				tagsFilter_);

		populate();
	}

	/**
	 * @see com.google.android.maps.ItemizedOverlay#createItem(int)
	 */
	@Override
	protected EventOverlayItem createItem(int arg0) {
		eventCursor_.moveToNext();
		return EventOverlayItem.getItemFromRow(eventCursor_);
	}

	/**
	 * @see org.vuphone.vandyupon.android.filters.FilterChangedListener#filterChanged()
	 */
	public void filterChanged() {
		Log.i(tag, pre + "Filter was updated");
		eventCursor_ = database_.getAllEntries(positionFilter_, timeFilter_,
				tagsFilter_);

		setLastFocusedIndex(-1);
		populate();
	}

	@Override
	protected boolean onTap(int index) {
		Log.d(tag, pre + "onTap called with index " + index);
		return true;
	}

	/**
	 * Used to pass new filters into the overlay. Any of the variables can be
	 * null to keep the current filter. The DB is queried and the overlay list
	 * re-populated every time we call this, so rather than having three
	 * distinct methods we have one where multiple filters can be updated at
	 * once.
	 * 
	 * @param p
	 *            a new PositionFilter, or null
	 * @param t
	 *            a new TimeFilter, or null
	 * @param ts
	 *            a new PositionFilter, or null
	 */
	protected void receiveNewFilters(PositionFilter p, TimeFilter t,
			TagsFilter ts) {

		if (positionFilter_ != null)
			positionFilter_.unregisterListener(this);

		positionFilter_ = p;
		if (positionFilter_ != null)
			positionFilter_.registerListener(this);

		timeFilter_ = t;
		tagsFilter_ = ts;

		eventCursor_ = database_.getAllEntries(positionFilter_, timeFilter_,
				tagsFilter_);

		populate();
	}

	/**
	 * Used to create a more accurate hittest. The default implementation has a
	 * minimum marker size of 100x100.
	 */
	
	private static int buffer = 15;
	
	@Override
	protected boolean hitTest(EventOverlayItem item,
			android.graphics.drawable.Drawable marker, int hitX, int hitY) {

		Rect bounds = marker.getBounds();

		int width = bounds.width();
		int height = bounds.height();
		int centerX = bounds.centerX();
		int centerY = bounds.centerY();

		int touchLeft = centerX - width / 2;
		int touchTop = centerY - height / 2;

		touchableBounds.set(touchLeft - buffer/2, touchTop - buffer/2, touchLeft + width + buffer/2, touchTop
				+ height + buffer/2);

		return touchableBounds.contains(hitX, hitY);
	}

	/**
	 * @see com.google.android.maps.ItemizedOverlay#size()
	 */
	@Override
	public int size() {
		return eventCursor_.getCount();
	}
}