/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package domain;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public abstract class Scribble {

	protected ScribbleType type;
	protected Paint paint;
	
	//TODO: delete 
	// replace with classes
	public enum ScribbleType {
		MINIMUM_BOUNDING_BOX, OBJECT_CONTOUR, FOREGROUND, BACKGROUND
	}
	
	public Paint getPaint() {
		return paint;
	}
	
	public void setPaint(Paint paint){
		this.paint = paint;
	}
	
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
