/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package domain;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Class for one Minimum Bounding Box drawing.
 * 
 * @author Theresa Froeschl
 * @version 1.0
 *
 */
public class MinBoundingBox implements Scribble {

	private RectF rectf;
	private Shape shape;
	private Paint paint;
	
	public enum Shape {
		RECTANGLE, OVAL
	}
	
	public MinBoundingBox(RectF rectf, Shape shape, Paint paint){
		this.rectf = rectf;
		this.shape = shape;
		this.paint = paint;
	}

	@Override
	public void drawScribble(Canvas canvas) {
		if (shape == Shape.RECTANGLE) {
			canvas.drawRect(rectf, paint);
		} else {
			canvas.drawOval(rectf, paint);
			
		}
	}
	
	@Override
	public RectF getBoundingBoxOfScribble(){
		return rectf;
	}
	
	
}
