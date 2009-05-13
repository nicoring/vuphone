package edu.vanderbilt.isis.vuphone.tools;

import android.graphics.Point;

public class PrecisionPoint extends Point {
	public float x;
	public float y;
	
	public PrecisionPoint(Point p) {
		x = p.x;
		y = p.y;
	}

}
