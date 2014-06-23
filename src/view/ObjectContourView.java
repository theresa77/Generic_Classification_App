/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

/**
 * View class for controlling the view for a ObjectContourFragment.
 * 
 * @author Theresa Froeschl
 * @version 1.0
 *
 */
public class ObjectContourView extends UserScribbleView {

	private static final String TAG = ObjectContourView.class.getSimpleName();
	private Path mPath;
	private float mX;
	private float mY;

	public ObjectContourView(Context context){
		super(context);
		mPath = new Path();
		mPaint.setStyle(Paint.Style.STROKE);
	}
	
	/**
	 * Draw picture and current scribble the canvas object.
	 */
	public void onDraw(Canvas canvas){
		//Log.d(TAG, "onDraw() is called");
		invalidate();	
		canvas.drawBitmap(mPictureBitmap, 0, 0, null);
		canvas.drawPath(mPath, mPaint);
	}
	
	/**
	 * Called when user starts to touch the screen.
	 * Start to draw user scribble.
	 * @param x coordinate of touch
	 * @param y coordinate of touch
	 */
	public void startMove(float x, float y){
		Log.d(TAG, "startMove() called");
		
		mPath.reset();
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
		invalidate();
	}
	
	/**
	 * Called when user continues his drawing.
	 * Add coordinates of new touch event to path object and draw it.
	 * @param x coordinate of touch
	 * @param y coordinate of touch
	 */
	public void move(float x, float y){
		if (!mPath.isEmpty()) {
			float dx = Math.abs(x - mX);
			float dy = Math.abs(y - mY);
			if (dx >= 4 || dy >= 4) {
				mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
				mX = x;
				mY = y;
			}	
		} else {
			startMove(x, y);
		}
		invalidate();
	}
	
	/**
	 * Called when user stop his drawing.
	 * Add coordinates of new touch event to path object and draw it.
	 * @param x coordinate of touch
	 * @param y coordinate of touch
	 */
	public void stopMove(float x, float y){
		mPath.lineTo(x, y);
		invalidate();
	}

	/**
	 * Reset the path object.
	 */
	public void resetPath(){
		mPath.reset();
		invalidate();
	}

	/**
	 * Draw path object for scribbles to the canvas.
	 */
	@Override
	public void drawUserScribble(Canvas canvas) {
		canvas.drawPath(mPath, mPaint);
	}

	/**
	 * Reset the current user scribble.
	 * Screen gets cleared.
	 */
	@Override
	public void resetDrawing() {
		mPath.reset();
		invalidate();
	}

}
