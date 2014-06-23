/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

/**
 * View class which controls the view for a MinimumBoundingBoxFragment.
 * 
 * @author Theresa Froeschl
 * @version 1.0
 *
 */
public class MinimumBoundingBoxView extends UserScribbleView {

	private static final String TAG = MinimumBoundingBoxView.class.getSimpleName();
	private RectF rectf;
	private Shape currentShape;

	public enum Shape {
		RECTANGLE, OVAL
	}

	public MinimumBoundingBoxView(Context context) {
		super(context);
		currentShape = Shape.RECTANGLE;	
		mPaint.setStyle(Paint.Style.STROKE);
	}

	/**
	 * Draw picture and user scribble to canvas object.
	 */
	@Override
	public void onDraw(Canvas canvas) {
		// Log.d(TAG, "onDraw() is called");
		canvas.drawBitmap(mPictureBitmap, 0, 0, null);
		//mPaint.setStyle(Paint.Style.STROKE);
		if (rectf != null) {
			if (currentShape == Shape.RECTANGLE) {
				canvas.drawRect(rectf, mPaint);
			} else {
				canvas.drawOval(rectf, mPaint);
			}
		}
	}

	/**
	 * Set boundaries for the RectF object and draw it.
	 * @param left boundary for user scribble
	 * @param top boundary for user scribble
	 * @param right boundary for user scribble
	 * @param bottom boundary for user scribble
	 */
	public void setShape(float left, float top, float right, float bottom) {
		Log.d(TAG, "setShape() is called");
		rectf = new RectF(left, top, right, bottom);
		invalidate();
	}
	
	/**
	 * Set the current shape and draw scribbles.
	 * @param shape
	 */
	public void setCurrentShape(Shape shape){
		currentShape = shape;
		invalidate();
	}
	
	/**
	 * Draw user scribble to canvas object.
	 */
	@Override
	public void drawUserScribble(Canvas canvas) {
		if (rectf != null) {
			if (currentShape == Shape.RECTANGLE) {
				canvas.drawRect(rectf, mPaint);
			} else {
				canvas.drawOval(rectf, mPaint);
			}
		}
	}

	/**
	 * Set RectF object null.
	 * Delete drawing.
	 */
	@Override
	public void resetDrawing() {
		rectf = null;
		invalidate();
	}

	
}
