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
import domain.MinBoundingBox;
import domain.MinBoundingBox.Shape;
import domain.Scribble;

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

		if (mActivity.isZoomEnabled()) {
			super.onDraw(canvas);
		} else {

			canvas.drawBitmap(mPictureBitmap, 0, 0, null);
			mPaint.setStyle(Paint.Style.STROKE);

			if (mPicture.getScribbles() != null
					&& !mPicture.getScribbles().isEmpty()) {
				for (Scribble s : mPicture.getScribbles()) {
					s.drawScribble(canvas);
				}
			}

			if (rectf != null) {
				if (currentShape == Shape.RECTANGLE) {
					canvas.drawRect(rectf, mPaint);

					if (!drawNewScribble) {
						mPaint.setStyle(Paint.Style.FILL);
						canvas.drawCircle(rectf.left, rectf.top,
								mPaint.getStrokeWidth(), mPaint);
						canvas.drawCircle(rectf.left, rectf.bottom,
								mPaint.getStrokeWidth(), mPaint);
						canvas.drawCircle(rectf.right, rectf.top,
								mPaint.getStrokeWidth(), mPaint);
						canvas.drawCircle(rectf.right, rectf.bottom,
								mPaint.getStrokeWidth(), mPaint);
					}
				} else {
					canvas.drawOval(rectf, mPaint);

					if (!drawNewScribble) {
						mPaint.setStyle(Paint.Style.FILL);
						canvas.drawCircle(rectf.centerX(), rectf.top,
								mPaint.getStrokeWidth(), mPaint);
						canvas.drawCircle(rectf.centerX(), rectf.bottom,
								mPaint.getStrokeWidth(), mPaint);
						canvas.drawCircle(rectf.left, rectf.centerY(),
								mPaint.getStrokeWidth(), mPaint);
						canvas.drawCircle(rectf.right, rectf.centerY(),
								mPaint.getStrokeWidth(), mPaint);
					}
				}
			}
			mPaint.setStyle(Paint.Style.STROKE);
		}
	}
	
	public void handleTouchEvent(int action, float x, float y) {
		// if user wants to draw new scribble, save old one
		if(drawNewScribble){
			if(rectf != null && !rectf.isEmpty())
//				mActivity.addScribbleToList(currentScribble);
			drawNewScribble = false;
			editScribble = false;
		}
		
		// edit Rectangle
		if (currentShape == Shape.RECTANGLE) {
			switch (action) {

			case (MotionEvent.ACTION_DOWN):
				// Log.d(TAG,"Action was DOWN");
			// draw new Scribble
				if (!editScribble) {
					xStart = x;
					yStart = y;
					
					//edit existing one
				} else {
					// left-top corner touched
					if (touchOnTopLeftCorner(x, y)) {
						xStart = rectf.right;
						yStart = rectf.bottom;
					}
					// right-top corner touched
					else if (touchOnTopRightCorner(x, y)) {
						xStart = rectf.left;
						yStart = rectf.bottom;
					}
					// left-bottom corner touched
					else if (touchOnBottomLeftCorner(x, y)) {
						xStart = rectf.right;
						yStart = rectf.top;
					}
					// right-bottom corner touched
					else if (touchOnBottomRightCorner(x, y)) {
						xStart = rectf.left;
						yStart = rectf.top;
					}
				}
				break;

			case (MotionEvent.ACTION_MOVE):
				// Log.d(TAG, "Action was MOVE");
				if (!editScribble) {
					if (xStart != 0 || yStart != 0) {
						Log.d(TAG, "Start values of Touch: xStart: " + xStart
								+ ", yStart: " + yStart);
						setShape(Math.min(x, xStart), Math.min(y, yStart),
								Math.max(x, xStart), Math.max(y, yStart));
					}
				} else {
					if (touchOnTopLeftCorner(x, y) || touchOnTopRightCorner(x, y)
							|| touchOnBottomLeftCorner(x, y) || touchOnBottomRightCorner(x, y)) {
						setShape(Math.min(x, xStart), Math.min(y, yStart),
								Math.max(x, xStart), Math.max(y, yStart));
					}
					
				}
				break;

			case (MotionEvent.ACTION_UP):
				// Log.d(TAG,"Action was UP");
				if (!editScribble) {
					setShape(Math.min(x, xStart), Math.min(y, yStart),
							Math.max(x, xStart), Math.max(y, yStart));
					editScribble = true;
				} else {
					if (touchOnTopLeftCorner(x, y) || touchOnTopRightCorner(x, y)
						|| touchOnBottomLeftCorner(x, y) || touchOnBottomRightCorner(x, y)) {
						setShape(Math.min(x, xStart), Math.min(y, yStart),
								Math.max(x, xStart), Math.max(y, yStart));
					}
				}
				xStart = 0;
				yStart = 0;
				
				break;
			}

		// edit Oval
		} else {

			switch (action) {

			case (MotionEvent.ACTION_DOWN):
				// Log.d(TAG,"Action was DOWN");
				if (!editScribble) {
					xStart = x;
					yStart = y;
				} else {
					// top touched
					if (touchOnTop(x, y)) {
						xStart = rectf.centerX();
						yStart = rectf.bottom;
					}
					// bottom touched
					else if (touchOnBottom(x, y)) {
						xStart = rectf.centerX();
						yStart = rectf.top;
					}
					// left side touched
					else if (touchOnLeftSide(x, y)) {
						xStart = rectf.right;
						yStart = rectf.centerY();
					}
					// right side touched
					else if (touchOnRightSide(x, y)) {
						xStart = rectf.left;
						yStart = rectf.centerY();
					}
				}
				break;

			case (MotionEvent.ACTION_MOVE):
				// Log.d(TAG, "Action was MOVE");
				if (!editScribble) {
					if (xStart != 0 || yStart != 0) {
						Log.d(TAG, "Start values of Touch: xStart: " + xStart
								+ ", yStart: " + yStart);
						setShape(Math.min(x, xStart), Math.min(y, yStart),
								Math.max(x, xStart), Math.max(y, yStart));
					}
				} else {
					if (touchOnTop(x, y) || touchOnBottom(x, y)) {
						setShape(Math.min(rectf.left, x), Math.min(yStart, y),
								Math.max(rectf.right, x), Math.max(yStart, y));
					}  else if (touchOnLeftSide(x, y) || touchOnRightSide(x, y)) {
						setShape(Math.min(xStart, x), Math.min(rectf.top, y),
								Math.max(xStart, x), Math.max(rectf.bottom, y));
					}
				}
				break;

			case (MotionEvent.ACTION_UP):
				// Log.d(TAG,"Action was UP");
				if (!editScribble) {
					setShape(Math.min(x, xStart), Math.min(y, yStart),
							Math.max(x, xStart), Math.max(y, yStart));
					editScribble = true;
				} else {
					if (touchOnTop(x, y) || touchOnBottom(x, y)) {
						setShape(Math.min(rectf.left, x), Math.min(yStart, y),
								Math.max(rectf.right, x), Math.max(yStart, y));
					}  else if (touchOnLeftSide(x, y) || touchOnRightSide(x, y)) {
						setShape(Math.min(xStart, x), Math.min(rectf.top, y),
								Math.max(xStart, x), Math.max(rectf.bottom, y));
					}
				}
				xStart = 0;
				yStart = 0;
				
				break;
			}		
		}

	}
	
	public void handleTouchEventOutsidePicture(int action) {
		// if user scribble was not drawn before
		if (rectf == null || rectf.isEmpty()) {
			if (action == MotionEvent.ACTION_UP) {
				xStart = 0;
				yStart = 0;
			}
		}
	}
	
	/**
	 * TODO
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean touchOnTopLeftCorner(float x, float y){
		if (x >= rectf.left - mPaint.getStrokeWidth()
				&& x <= rectf.left + mPaint.getStrokeWidth()
				&& y >= rectf.top - mPaint.getStrokeWidth()
				&& y <= rectf.top + mPaint.getStrokeWidth())
			return true;
		return false;
	}

	/**
	 * TODO
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean touchOnTopRightCorner(float x, float y){
		if (x >= rectf.right - mPaint.getStrokeWidth()
				&& x <= rectf.right + mPaint.getStrokeWidth()
				&& y >= rectf.top - mPaint.getStrokeWidth()
				&& y <= rectf.top + mPaint.getStrokeWidth())
			return true;
		return false;
	}
	
	/**
	 * TODO
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean touchOnBottomLeftCorner(float x, float y){
		if (x >= rectf.left - mPaint.getStrokeWidth()
				&& x <= rectf.left + mPaint.getStrokeWidth()
				&& y >= rectf.bottom - mPaint.getStrokeWidth()
				&& y <= rectf.bottom + mPaint.getStrokeWidth())
			return true;
		return false;
	}
	
	/**
	 * TODO
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean touchOnBottomRightCorner(float x, float y){
		if (x >= rectf.right - mPaint.getStrokeWidth()
				&& x <= rectf.right + mPaint.getStrokeWidth()
				&& y >= rectf.bottom - mPaint.getStrokeWidth()
				&& y <= rectf.bottom + mPaint.getStrokeWidth())
			return true;
		return false;
	}
	
	/**
	 * TODO
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean touchOnTop(float x, float y){
		if(x >= rectf.centerX() - mPaint.getStrokeWidth()
				&& x <= rectf.centerX() + mPaint.getStrokeWidth()
				&& y >= rectf.top - mPaint.getStrokeWidth()
				&& y <= rectf.top + mPaint.getStrokeWidth())
			return true;
		return false;
	}
	
	/**
	 * TODO
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean touchOnBottom(float x, float y){
		if(x >= rectf.centerX() - mPaint.getStrokeWidth()
				&& x <= rectf.centerX() + mPaint.getStrokeWidth()
				&& y >= rectf.bottom - mPaint.getStrokeWidth()
				&& y <= rectf.bottom + mPaint.getStrokeWidth())
			return true;
		return false;
	}
	
	/**
	 * TODO
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean touchOnLeftSide(float x, float y){
		if(x >= rectf.left - mPaint.getStrokeWidth()
				&& x <= rectf.left + mPaint.getStrokeWidth()
				&& y >= rectf.centerY() - mPaint.getStrokeWidth()
				&& y <= rectf.centerY() + mPaint.getStrokeWidth())
			return true;
		return false;
	}
	
	/**
	 * TODO
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean touchOnRightSide(float x, float y){
		if(x >= rectf.right - mPaint.getStrokeWidth()
				&& x <= rectf.right + mPaint.getStrokeWidth()
				&& y >= rectf.centerY() - mPaint.getStrokeWidth()
				&& y <= rectf.centerY() + mPaint.getStrokeWidth())
			return true;
		return false;
	}
	
	/**
	 * Set boundaries for the RectF object and draw it.
	 * 
	 * @param left boundary for user scribble
	 * @param top boundary for user scribble
	 * @param right boundary for user scribble
	 * @param bottom boundary for user scribble
	 */
	public void setShape(float left, float top, float right, float bottom) {
		Log.d(TAG, "setShape( left: "+left+", top: "+top+", right: "+right+", bottom: "+bottom+" ) is called");
		rectf = new RectF(left, top, right, bottom);
		currentScribble = new MinBoundingBox(new RectF(rectf), currentShape, new Paint(mPaint));
		invalidate();
	}
	
	/**
	 * Set the current shape and draw scribbles.
	 * @param shape
	 */
	public void setCurrentShape(Shape shape){
		currentShape = shape;
		currentScribble = new MinBoundingBox(new RectF(rectf), shape, new Paint(mPaint));
		invalidate();
	}
	
	/**
	 * Draw user scribble to canvas object.
	 */
	@Override
	public void drawUserScribble(Canvas canvas) {
		if (mPicture.getScribbles() != null && !mPicture.getScribbles().isEmpty()) {
			for (Scribble s : mPicture.getScribbles()) {
				s.drawScribble(canvas);
			}
		}
		
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
	public void resetLastDrawing() {
		rectf = null;
		editScribble = false;
		invalidate();
	}

	
}
