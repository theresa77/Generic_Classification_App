/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

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
	private float xStart;
	private float yStart;
	private boolean editScribble;

	public enum Shape {
		RECTANGLE, OVAL
	}

	public MinimumBoundingBoxView(Context context) {
		super(context);
		currentShape = Shape.RECTANGLE;	
		mPaint.setStyle(Paint.Style.STROKE);
		editScribble = false;
	}

	/**
	 * Draw picture and user scribble to canvas object.
	 */
	@Override
	public void onDraw(Canvas canvas) {
		// Log.d(TAG, "onDraw() is called");
		canvas.drawBitmap(mPictureBitmap, 0, 0, null);
		mPaint.setStyle(Paint.Style.STROKE);
		if (rectf != null) {
			if (currentShape == Shape.RECTANGLE) {
				canvas.drawRect(rectf, mPaint);
				mPaint.setStyle(Paint.Style.FILL);
				canvas.drawCircle(rectf.left, rectf.top, mPaint.getStrokeWidth(), mPaint);
				canvas.drawCircle(rectf.left, rectf.bottom, mPaint.getStrokeWidth(), mPaint);
				canvas.drawCircle(rectf.right, rectf.top, mPaint.getStrokeWidth(), mPaint);
				canvas.drawCircle(rectf.right, rectf.bottom, mPaint.getStrokeWidth(), mPaint);
			} else {
				canvas.drawOval(rectf, mPaint);
				mPaint.setStyle(Paint.Style.FILL);
				canvas.drawCircle(rectf.centerX(), rectf.top, mPaint.getStrokeWidth(), mPaint);
				canvas.drawCircle(rectf.centerX(), rectf.bottom, mPaint.getStrokeWidth(), mPaint);
				canvas.drawCircle(rectf.left, rectf.centerY(), mPaint.getStrokeWidth(), mPaint);
				canvas.drawCircle(rectf.right, rectf.centerY(), mPaint.getStrokeWidth(), mPaint);
			}
		}
	}
	
	public void handleTouchEvent(int action, float x, float y){
		
		// draw new user scribble
		if (!editScribble) {
			switch (action) {

			case (MotionEvent.ACTION_DOWN):
				// Log.d(TAG,"Action was DOWN");
				xStart = x;
				yStart = y;
				break;

			case (MotionEvent.ACTION_MOVE):
				// Log.d(TAG, "Action was MOVE");
				if (xStart != 0 || yStart != 0) {
					setShape(Math.min(xStart, x), Math.min(yStart, y),
							Math.max(xStart, x), Math.max(yStart, y));
				} 
				break;

			case (MotionEvent.ACTION_UP):
				// Log.d(TAG,"Action was UP");
				setShape(Math.min(xStart, x), Math.min(yStart, y),
						Math.max(xStart, x), Math.max(yStart, y));
				xStart = 0;
				yStart = 0;
				editScribble = true;
				break;
			}
			
		// edit existing user scribble
		} else {
			if(currentShape == Shape.RECTANGLE){
				switch (action) {

				case (MotionEvent.ACTION_DOWN):
// 				Log.d(TAG,"Action was DOWN");
					
					// left-top corner touched
					if (x >= rectf.left-mPaint.getStrokeWidth() && x <= rectf.left+mPaint.getStrokeWidth() 
						&& y >= rectf.top-mPaint.getStrokeWidth() && y <= rectf.top+mPaint.getStrokeWidth()) {
						xStart = rectf.right;
						yStart = rectf.bottom;
					}
					// right-top corner touched
					else if (x >= rectf.right-mPaint.getStrokeWidth() && x <= rectf.right+mPaint.getStrokeWidth() 
							&& y >= rectf.top-mPaint.getStrokeWidth() && y <= rectf.top+mPaint.getStrokeWidth()) {
						xStart = rectf.left;
						yStart = rectf.bottom;
					}
					// left-bottom corner touched
					else if (x >= rectf.left-mPaint.getStrokeWidth() && x <= rectf.left+mPaint.getStrokeWidth()  
							&& y >= rectf.bottom-mPaint.getStrokeWidth() && y <= rectf.bottom+mPaint.getStrokeWidth()) {
						xStart = rectf.right;
						yStart = rectf.top;
					}
					// right-bottom corner touched
					else if (x >= rectf.right-mPaint.getStrokeWidth() && x <= rectf.right+mPaint.getStrokeWidth() 
							&& y >= rectf.bottom-mPaint.getStrokeWidth() && y <= rectf.bottom+mPaint.getStrokeWidth()) {
						xStart = rectf.left;
						yStart = rectf.top;
					}
					break;

				case (MotionEvent.ACTION_MOVE):
					// Log.d(TAG, "Action was MOVE");
					if (xStart != 0 || yStart != 0) {
						Log.d(TAG, "Start values of Touch: xStart: "+xStart+", yStart: "+yStart);
						setShape(Math.min(x, xStart), Math.min(y, yStart),
								Math.max(x, xStart), Math.max(y, yStart));
					} 
					break;

				case (MotionEvent.ACTION_UP):
					// Log.d(TAG,"Action was UP");
					setShape(Math.min(x, xStart), Math.min(y, yStart),
							Math.max(x, xStart), Math.max(y, yStart));
				
					xStart = 0;
					yStart = 0;
					break;
				}
				
			} else {
				
			}
		}
	}
	
	public void handleTouchEventOutsidePicture(int action){
		// if user scribble was not drawn before
		if (rectf == null || rectf.isEmpty()) {
			if (action == MotionEvent.ACTION_UP) {
				xStart = 0;
				yStart = 0;
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
		Log.d(TAG, "setShape( left: "+left+", top: "+top+", right: "+right+", bottom: "+bottom+" ) is called");
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
			mPaint.setStyle(Paint.Style.STROKE);
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
		editScribble = false;
		invalidate();
	}

	
}
