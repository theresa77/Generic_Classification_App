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
 * View class for controlling the view for a ForegroundBackgroundFragment.
 * 
 * @author Theresa Froeschl
 * @version 1.0
 *
 */
public class ForegroundBackgroundView extends UserScribbleView {

	private static final String TAG = ForegroundBackgroundView.class.getSimpleName();
	private Path mPath;
	private float mX;
	private float mY;
	private Selection mCurrentSelection;
	private boolean drawCircle;
	
	public enum Selection {
		FOREGROUND, BACKGROUND
	}
	
	public ForegroundBackgroundView(Context context){
		super(context);
		mPath = new Path();
		mCurrentSelection = Selection.FOREGROUND;
		drawCircle = true;
	}
	
	/**
	 * Draws bitmap of the picture and the current user scribbles
	 */
	@Override
	public void onDraw(Canvas canvas){
		//Log.d(TAG, "onDraw() is called");
		canvas.drawBitmap(mPictureBitmap, 0, 0, null);
		if(!mPath.isEmpty()){
		
		if (drawCircle){
			mPaint.setStyle(Paint.Style.FILL);
			Log.d(TAG, "mPaint.setStyle(Paint.Style.FILL)");
			canvas.drawCircle(mX, mY, mPaint.getStrokeWidth()/2, mPaint);
		}else{
			mPaint.setStyle(Paint.Style.STROKE);
			canvas.drawPath(mPath, mPaint);
		}
	}
	}

	/**
	 * called when user touches the screen and starts to draw
	 * @param x coordinate of the touch
	 * @param y coordinate of the touch
	 */
	public void startMove(float x, float y){
		Log.d(TAG, "startMove() called");

		drawCircle = true;
		mPath.reset();
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
		invalidate();
	}
	
	/**
	 * called when user continues drawing and moves finger over the screen
	 * @param x coordinate of the current touch event
	 * @param y coordinate of the current touch event
	 */
	public void move(float x, float y){
		Log.d(TAG, "move() called");
		
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
		if(drawCircle)
			drawCircle = false;
		invalidate();
	}
	
	/**
	 * Called when user stops his touch
	 * @param x coordinate of the current touch event
	 * @param y coordinate of the current touch event
	 */
	public void stopMove(float x, float y){
		mPath.lineTo(x, y);
	//	drawCircle = true;
		invalidate();
	}
	
	/**
	 * Resets the path from the previous touch
	 * Called when user starts new touch
	 */
	public void resetPath(){
		mPath.reset();
		mX = 0;
		mY = 0;
		drawCircle = true;
		invalidate();
	}
	
	@Override
	public void drawUserScribble(Canvas canvas) {
		if (drawCircle){
			mPaint.setStyle(Paint.Style.FILL);
			canvas.drawCircle(mX, mY, mPaint.getStrokeWidth()/2, mPaint);
		}else{
			mPaint.setStyle(Paint.Style.STROKE);
			canvas.drawPath(mPath, mPaint);
		}
	}

	@Override
	public void resetDrawing() {
		resetPath();
	}

	/**
	 * Get current selection of foreground or background
	 * @param selection
	 */
	public Selection getCurrentSelection() {
		return mCurrentSelection;
	}
	
	/**
	 * Set current selection of foreground or background
	 * @param selection
	 */
	public void setCurrentSelection(Selection selection) {
		this.mCurrentSelection = selection;
	}

}
