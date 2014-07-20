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
	private List<Path> oldScribbles;
	
	public enum Selection {
		FOREGROUND, BACKGROUND
	}
	
	public ForegroundBackgroundView(Context context){
		super(context);
		mPath = new Path();
		mCurrentSelection = Selection.FOREGROUND;
		drawCircle = true;
		oldScribbles = new ArrayList<Path>();
	}
	
	/**
	 * Draws bitmap of the picture and the current user scribbles
	 */
	@Override
	public void onDraw(Canvas canvas){
		// Log.d(TAG, "onDraw() is called");
		canvas.drawBitmap(mPictureBitmap, 0, 0, null);

		if (oldScribbles != null && !oldScribbles.isEmpty()) {
			for (Path p : oldScribbles) {
				mPaint.setStyle(Paint.Style.STROKE);
				canvas.drawPath(p, mPaint);
				Log.d(TAG, "Draw Path: " + p.toString());
			}
		}

		if (!mPath.isEmpty()) {

			if (drawCircle) {
				mPaint.setStyle(Paint.Style.FILL);
				Log.d(TAG, "mPaint.setStyle(Paint.Style.FILL)");
				canvas.drawCircle(mX, mY, mPaint.getStrokeWidth() / 2, mPaint);
			} else {
				mPaint.setStyle(Paint.Style.STROKE);
				canvas.drawPath(mPath, mPaint);
			}
		}
	}
	
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
	
	public void handleTouchEventOutsidePicture(int action){
		if(action == MotionEvent.ACTION_DOWN){
			resetPath();
		}
	}

	/**
	 * called when user touches the screen and starts to draw
	 * @param x coordinate of the touch
	 * @param y coordinate of the touch
	 */
	public void startMove(float x, float y){
		//Log.d(TAG, "startMove() called");
		Log.d(TAG, "Draw New Scribble - Boolean: "+super.drawNewScribble);
		if(drawNewScribble){
			if(mPath != null && !mPath.isEmpty())
				oldScribbles.add(new Path(mPath));
			drawNewScribble = false;
		}

		Log.d(TAG, "Draw New Scribble - Boolean: "+super.drawNewScribble);
		
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
		//Log.d(TAG, "move() called");
//		Log.d(TAG, "Draw New Scribble - Boolean: "+super.drawNewScribble);
		
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
//		Log.d(TAG, "Draw New Scribble - Boolean: "+super.drawNewScribble);
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
		//TODO: check is all scribbles are drawn correct
		if (oldScribbles != null && !oldScribbles.isEmpty()) {
			for (Path p : oldScribbles) {
				mPaint.setStyle(Paint.Style.STROKE);
				canvas.drawPath(p, mPaint);
				Log.d(TAG, "Draw Path: " + p.toString());
			}
		}
		
		if (drawCircle){
			mPaint.setStyle(Paint.Style.FILL);
			canvas.drawCircle(mX, mY, mPaint.getStrokeWidth()/2, mPaint);
		}else{
			mPaint.setStyle(Paint.Style.STROKE);
			canvas.drawPath(mPath, mPaint);
		}
	}

	@Override
	public void resetLastDrawing() {
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
