/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package domain;

import android.graphics.Canvas;
import android.graphics.RectF;

public interface Scribble {

	/**
	 * Draw scribble to canvas.
	 * @param canvas object to draw scribble on
	 */
	public abstract void drawScribble(Canvas canvas);
	
	/**
	 * Get Bounding Box of scribble on the picture.
	 * @return bounding box of the scribble
	 */
	public abstract RectF getBoundingBoxOfScribble();	
	
}
