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
 *
 */
public class MinBoundingBox extends Scribble {

	private RectF rectf;
	private Shape shape;
	private Paint paint;
	
	public enum Shape {
		RECTANGLE, OVAL
	}
	
	public MinBoundingBox(RectF rectf, Shape shape, Paint paint){
		super();
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
	
	public String toString() {
		return "MinBoundingBox(rectf(left: " + rectf.left + ", top: "
				+ rectf.top + ", right: " + rectf.right + ", bottom: "
				+ rectf.bottom + ")" + ", shape(" + shape.name() + "), "
				+ "+paint( color:" + paint.getColor() + ", stroke width: "
				+ paint.getStrokeWidth() + ") )";
	}
	
	@Override
	public RectF getBoundingBoxOfScribble(){
		return rectf;
	}
	
	
}
