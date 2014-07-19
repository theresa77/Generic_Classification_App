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
	private boolean editTopLeftCorner;
	private boolean editTopRightCorner;
	private boolean editBottomLeftCorner;
	private boolean editBottomRightCorner;

	public enum Shape {
		RECTANGLE, OVAL
	}

	public MinimumBoundingBoxView(Context context) {
		super(context);
		currentShape = Shape.RECTANGLE;	
		mPaint.setStyle(Paint.Style.STROKE);
		editScribble = false;
		editTopLeftCorner = false;
		editTopRightCorner = false;
		editBottomLeftCorner = false;
		editBottomRightCorner = false;
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
	
	/**
	 * TODO
	 * handles touch event on the picture
	 * @param action
	 * @param x
	 * @param y
	 */
	public void handleTouchEvent(int action, float x, float y){
		
		// if user scribble was not drawn before
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
				} else {
					xStart = x;
					yStart = y;
				}
				break;

			case (MotionEvent.ACTION_UP):
				// Log.d(TAG,"Action was UP");

				setShape(Math.min(xStart, x), Math.min(yStart, y),
						Math.max(xStart, x), Math.max(yStart, y));
				xStart = 0;
				yStart = 0;
				editScribble = true;
				Log.d(TAG, "set editScribble to TRUE!!!");
				break;

			case (MotionEvent.ACTION_CANCEL):
				// Log.d(TAG,"Action was CANCEL");
				break;

			case (MotionEvent.ACTION_OUTSIDE):
				// Log.d(TAG,"Movement occurred outside bounds of current screen element");
				break;
			}
			
			// edit existing user scribble
		} else {
			if(currentShape == Shape.RECTANGLE){
				switch (action) {

				case (MotionEvent.ACTION_DOWN):
// 				Log.d(TAG,"Action was DOWN");
					
					// left-top corner
					if (x >= rectf.left-mPaint.getStrokeWidth() && x <= rectf.left+mPaint.getStrokeWidth() 
						&& y >= rectf.top-mPaint.getStrokeWidth() && y <= rectf.top+mPaint.getStrokeWidth()) {
						Log.d(TAG, "TOP-LEFT CORNER TOUCHED DOWN");
						xStart = x;
						yStart = y;
//						setShape(Math.min(x, rectf.right), Math.min(y, rectf.bottom),
//								Math.max(x, rectf.right), Math.max(y, rectf.bottom));
						editTopLeftCorner = true;
					}
					// right-top corner
					else if (x >= rectf.right-mPaint.getStrokeWidth() && x <= rectf.right+mPaint.getStrokeWidth() 
							&& y >= rectf.top-mPaint.getStrokeWidth() && y <= rectf.top+mPaint.getStrokeWidth()) {
						Log.d(TAG, "TOP-RIGHT CORNER TOUCHED DOWN");
						xStart = rectf.right;
						yStart = rectf.top;
						editTopRightCorner = true;
					}
					// left-bottom corner
					else if (x >= rectf.left-mPaint.getStrokeWidth() && x <= rectf.left+mPaint.getStrokeWidth()  
							&& y >= rectf.bottom-mPaint.getStrokeWidth() && y <= rectf.bottom+mPaint.getStrokeWidth()) {
						Log.d(TAG, "BOTTOM-LEFT CORNER TOUCHED DOWN");
						xStart = rectf.left;
						yStart = rectf.bottom;
						editBottomLeftCorner = true;
					}
					// right-bottom corner
					else if (x >= rectf.right-mPaint.getStrokeWidth() && x <= rectf.right+mPaint.getStrokeWidth() 
							&& y >= rectf.bottom-mPaint.getStrokeWidth() && y <= rectf.bottom+mPaint.getStrokeWidth()) {
						Log.d(TAG, "BOTTOM-RIGHT CORNER TOUCHED DOWN");
						xStart = rectf.right;
						yStart = rectf.bottom;
						editBottomRightCorner = true;
					}
					
					break;

				case (MotionEvent.ACTION_MOVE):
					// Log.d(TAG, "Action was MOVE");

					if (xStart != 0 || yStart != 0) {
						Log.d(TAG, "Start values of Touch: xStart: "+xStart+", yStart: "+yStart);
						
						// left-top corner
						if(editTopLeftCorner){
//							setShape(x, y, rectf.right, rectf.bottom);
							
							setShape(Math.min(x, rectf.right), Math.min(y, rectf.bottom),
									Math.max(x, rectf.right), Math.max(y, rectf.bottom));
							
							Log.d(TAG, "TOP-LEFT CORNER TOUCHED MOVED");
						} 
						// right-top corner
						else if (editTopRightCorner){
							setShape(rectf.left, y, x, rectf.bottom);
							Log.d(TAG, "TOP-RIGHT CORNER TOUCHED MOVED");
						}
						// left-bottom corner
						else if (editBottomLeftCorner){
							setShape(x, rectf.top, rectf.right, y);
							Log.d(TAG, "BOTTOM-LEFT CORNER TOUCHED MOVED");
						}
						// right-bottom corner
						else if (editBottomRightCorner){
							setShape(rectf.left, rectf.top, x, y);
							Log.d(TAG, "BOTTOM-RIGHT CORNER TOUCHED MOVED");
						}
						
					} 
//					else {
//						xStart = x;
//						yStart = y;
//					}
					break;

				case (MotionEvent.ACTION_UP):
					// Log.d(TAG,"Action was UP");

					// left-top corner
					if(xStart == rectf.left && yStart == rectf.top){
//						setShape(x, y, rectf.right, rectf.bottom);
						
						setShape(Math.min(x, rectf.right), Math.min(y, rectf.bottom),
								Math.max(x, rectf.right), Math.max(y, rectf.bottom));
						editTopLeftCorner = false;
						Log.d(TAG, "TOP-LEFT CORNER TOUCHED UP");
					} 
					// right-top corner
					else if (xStart == rectf.right && yStart == rectf.top){
						setShape(rectf.left, y, x, rectf.bottom);
						editTopRightCorner = false;
						Log.d(TAG, "TOP-RIGHT CORNER TOUCHED UP");
					}
					// left-bottom corner
					else if (xStart == rectf.left && yStart == rectf.bottom){
						setShape(x, rectf.top, rectf.right, y);
						editBottomLeftCorner = false;
						Log.d(TAG, "BOTTOM-LEFT CORNER TOUCHED UP");
					}
					// right-bottom corner
					else if (xStart == rectf.right && yStart == rectf.bottom){
						setShape(rectf.left, rectf.top, x, y);
						editBottomRightCorner = false;
						Log.d(TAG, "BOTTOM-RIGHT CORNER TOUCHED UP");
					}
				
					xStart = 0;
					yStart = 0;
					break;

				case (MotionEvent.ACTION_CANCEL):
					// Log.d(TAG,"Action was CANCEL");
					break;

				case (MotionEvent.ACTION_OUTSIDE):
					// Log.d(TAG,"Movement occurred outside bounds of current screen element");
					break;
				}
				
			} else {
				
			}
		}
	}
	
	/**
	 * TODO
	 * handles touch on screen outside of the picture
	 * @param action
	 * @param x
	 * @param y
	 */
	public void handleTouchEventOutsidePicture(int action, float x, float y){
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
