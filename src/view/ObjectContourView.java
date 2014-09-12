/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import domain.ObjectContour;

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
	private List<Float> mPoints = new ArrayList<Float>();
	private float mLastX = Float.NaN;
	private float mLastY = Float.NaN;
	
	public ObjectContourView(Context context, UserScribbleView oldView){
		super(context);
		mPath = new Path();
		mPaint.setStyle(Paint.Style.STROKE);
		mScaleFactor = oldView.mScaleFactor;
		focusX = oldView.focusX;
		focusY = oldView.focusY;
		mPosX = oldView.mPosX;
		mPosY = oldView.mPosY;
	}
	
	public ObjectContourView(Context context){
		super(context);
		mPath = new Path();
		mPaint.setStyle(Paint.Style.STROKE);
	}
	
//	public void addPoint(float x, float y) {
//	    if (mLastX == Float.NaN || mLastY == Float.NaN) {
//	        mLastX = x;
//	        mLastY = y;
//	    } else {
//	        mPoints.add(mLastX);
//	        mPoints.add(mLastY);
//	        mPoints.add(x);
//	        mPoints.add(y);
//
//	        mLastX = x;
//	        mLastY = y;
//	    }
//	}
	
	@Override
	public void handleTouchEvent(int action, float x, float y){
		switch (action) {

		// user start touch
		// drawing of scribbles starts
		case (MotionEvent.ACTION_DOWN):
			// Log.d(TAG,"Action was DOWN");
			startMove(x, y);
			break;

		// user moves finger on screen
		case (MotionEvent.ACTION_MOVE):
			// Log.d(TAG, "Action was MOVE");
			move(x, y);
			break;

		// user stop touching the screen
		case (MotionEvent.ACTION_UP):
			// Log.d(TAG,"Action was UP");
			stopMove(x, y);
			break;
		}
	}

	@Override
	public void handleTouchEventOutsidePicture(int action){
		if(action == MotionEvent.ACTION_DOWN){
			resetPath();
		}
	}
	
	/**
	 * Called when user starts to touch the screen.
	 * Start to draw user scribble.
	 * @param x coordinate of touch
	 * @param y coordinate of touch
	 */
	public void startMove(float x, float y){
		Log.d(TAG, "startMove() called");
		if(drawNewScribble){
			if(mPath != null && !mPath.isEmpty())
				mPicture.addScribbleToList(currentScribble);
			drawNewScribble = false;
		}
		
		resetPath();
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
		
//		addPoint(x, y);
			
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
		
//		addPoint(x, y);
		
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
		mPath.close();
		
//		addPoint(x, y);
//		addPoint(x, y);
//		addPoint(mPoints.get(0), mPoints.get(1));
//		float[] pts = new float[mPoints.size()];
//		for(int i=0; i<mPoints.size(); i++){
//			pts[i] = mPoints.get(i);
//		}
//		currentScribble = new ObjectContour(pts, new Paint(mPaint));
		currentScribble = new ObjectContour(new Path(mPath), new Paint(mPaint));
		
		invalidate();
	}

	/**
	 * Reset the path object.
	 */
	public void resetPath(){
		mPath.reset();
		mPoints = new ArrayList<Float>();
		invalidate();
	}

	/**
	 * Draw path object for scribbles to the canvas.
	 */
	@Override
	public void drawCurrentScribble(Canvas canvas) {
		canvas.drawPath(mPath, mPaint);
		Log.d(TAG, "IS CANVAS HARDWARE ACCELERATED?:"+ canvas.isHardwareAccelerated());
		
//		float[] pts = new float[mPoints.size()];
//		for(int i=0; i<mPoints.size(); i++){
//			pts[i] = mPoints.get(i);
//		}
//		canvas.drawLines(pts, mPaint);
	}
	
	/**
	 * Reset the current user scribble.
	 * Screen gets cleared.
	 */
	@Override
	public void resetLastDrawing() {
		mPath.reset();
		invalidate();
	}

}
