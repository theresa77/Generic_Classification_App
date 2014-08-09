/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;
import domain.ForeBackGround;
import domain.Scribble;

/**
 * View class for controlling the view for a ForegroundBackgroundFragment.
 * 
 * @author Theresa Froeschl
 * @version 1.0
 *
 */
public class ForegroundBackgroundView extends UserScribbleView {

	private static final String TAG = ForegroundBackgroundView.class.getSimpleName();
	private Path mForePath;
	private Path mBackPath;
	private float mX;
	private float mY;
	private Selection mCurrentSelection;
	private boolean drawCircle;
	private boolean drawForeground;
	
	public enum Selection {
		FOREGROUND, BACKGROUND
	}
	
	public ForegroundBackgroundView(Context context){
		super(context);
		mForePath = new Path();
		mBackPath = new Path();
		mCurrentSelection = Selection.FOREGROUND;
		drawCircle = true;
		drawForeground = true;
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
			if(mBackPath != null && !mBackPath.isEmpty())
				mPicture.addScribbleToList(currentScribble);
			drawNewScribble = false;
		}

		drawCircle = true;
		
		if (drawForeground) {
			mForePath.reset();
			mForePath.moveTo(x, y);
		} else {
			mBackPath.reset();
			mBackPath.moveTo(x, y);
		}
		
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
		
		if(drawForeground){
			if (!mForePath.isEmpty()) {
				float dx = Math.abs(x - mX);
				float dy = Math.abs(y - mY);
				if (dx >= 4 || dy >= 4) {
					mForePath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
					mX = x;
					mY = y;
				}
			} else {
				startMove(x, y);
			}
		} else {
			if (!mBackPath.isEmpty()) {
				float dx = Math.abs(x - mX);
				float dy = Math.abs(y - mY);
				if (dx >= 4 || dy >= 4) {
					mBackPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
					mX = x;
					mY = y;
				}
			} else {
				startMove(x, y);
			}
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
		if(drawForeground){
			mForePath.lineTo(x, y);
			drawForeground = false;
			Toast.makeText(mActivity, "Draw Background", Toast.LENGTH_SHORT).show();
		} else {
			mBackPath.lineTo(x, y);
			currentScribble = new ForeBackGround(new Path(mForePath), new Path(mBackPath), new Paint(mPaint), Color.YELLOW, Color.MAGENTA);
		}
	//	drawCircle = true;
		invalidate();
	}
	
	/**
	 * Resets the path from the previous touch
	 * Called when user starts new touch
	 * TODO
	 */
	public void resetPath(){
		mForePath.reset();
		mBackPath.reset();
		mX = 0;
		mY = 0;
		drawForeground = true;
		drawCircle = true;
		invalidate();
	}
	
	@Override
	public void drawUserScribble(Canvas canvas) {
		int color = mPaint.getColor();
		if (mPicture.getScribbles() != null && !mPicture.getScribbles().isEmpty()) {
			for (Scribble s : mPicture.getScribbles()) {
				s.drawScribble(canvas);
			}
		}
		
		if(mForePath != null && !mForePath.isEmpty()){
//		if (drawCircle){
//			mPaint.setStyle(Paint.Style.FILL);
//			canvas.drawCircle(mX, mY, mPaint.getStrokeWidth()/2, mPaint);
//		}else{
			mPaint.setColor(Color.YELLOW);
			mPaint.setStyle(Paint.Style.STROKE);
			canvas.drawPath(mForePath, mPaint);
//		}
		} 
		
		if (mBackPath != null && !mBackPath.isEmpty()) {
			mPaint.setColor(Color.MAGENTA);
			canvas.drawPath(mBackPath, mPaint);
		}
		
		mPaint.setColor(color);
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

	@Override
	public void drawFurtherThings(Canvas canvas) {
		// TODO Auto-generated method stub
		
	}

}
