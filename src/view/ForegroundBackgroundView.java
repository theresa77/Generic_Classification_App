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
import android.widget.ImageButton;
import android.widget.Toast;

import com.genericclassificationapp.R;

import domain.ForeBackGround;

/**
 * View class for controlling the view for a ForegroundBackgroundFragment.
 * 
 * @author Theresa Froeschl
 * @version 1.0
 *
 */
public class ForegroundBackgroundView extends UserScribbleView {

	private static final String TAG = ForegroundBackgroundView.class.getSimpleName();
	private Path mPath = new Path();
	private int mForeColor = Color.YELLOW;
	private int mBackColor = Color.MAGENTA;
	private float mX;
	private float mY;
	private boolean drawForeground = true;
	private ImageButton foreBackButton;
	
	public ForegroundBackgroundView(Context context, UserScribbleView oldView, ImageButton foreBackButton){
		super(context);
		init(foreBackButton);
		mScaleFactor = oldView.mScaleFactor;
		focusX = oldView.focusX;
		focusY = oldView.focusY;
		mPosX = oldView.mPosX;
		mPosY = oldView.mPosY;
	}
	
	public ForegroundBackgroundView(Context context, ImageButton foreBackButton){
		super(context);
		init(foreBackButton);
	}
	
	public ForegroundBackgroundView(Context context){
		super(context);
	}
	
	private void init(ImageButton foreBackButton) {
		Log.d(TAG, "init ForegroundBackgroundView");
		this.foreBackButton = foreBackButton;
	}
	
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
	 * called when user touches the screen and starts to draw
	 * @param x coordinate of the touch
	 * @param y coordinate of the touch
	 */
	public void startMove(float x, float y){
		//Log.d(TAG, "startMove() called");
		if(drawNewScribble){
			drawNewScribble = false;
		}
		mPicture.addScribbleToList(currentScribble);
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
	 * Called when user stops his touch
	 * @param x coordinate of the current touch event
	 * @param y coordinate of the current touch event
	 */
	public void stopMove(float x, float y){
		mPath.lineTo(x, y);
		
		foreBackButton.setImageDrawable(getResources().getDrawable(R.drawable.check));
		
		if (drawForeground) {
			Paint forePaint = new Paint(mPaint);
			forePaint.setColor(mForeColor);
			currentScribble = new ForeBackGround(new Path(mPath), new Paint(forePaint));
		} else {
			Paint backPaint = new Paint(mPaint);
			backPaint.setColor(mBackColor);
			currentScribble = new ForeBackGround(new Path(mPath), new Paint(backPaint));
		}
		
		invalidate();
	}
	
	/**
	 * Resets the last drawn path.
	 */
	public void resetPath(){
		mPath.reset();
		mX = 0;
		mY = 0;
		invalidate();
	}
	
	@Override
	public void drawCurrentScribble(Canvas canvas) {
		int currColor = mPaint.getColor();
		
		if (drawForeground) {
			mPaint.setColor(mForeColor);
		} else {
			mPaint.setColor(mBackColor);
		}
		
		canvas.drawPath(mPath, mPaint);
		
		mPaint.setColor(currColor);
	}

	@Override
	public void resetLastDrawing() {
		if(drawNewScribble){
			drawForeground = true;
		}
		setForeBackgroundButton();
		resetPath();
		invalidate();
	}
	

	/**
	 * Returns if user is currently drawing on the foreground of the picture.
	 * @return true is user is markig the foreground
	 */
	public boolean isDrawingForeground(){
		return drawForeground;
	}
	
	/**
	 * Set boolean flag for drawing on the foreground or the background of the picture.
	 * @param drawForeground true for drawing on the foreground
	 */
	public void setDrawForeground(boolean drawForeground){
		this.drawForeground = drawForeground;
	}
	
	/**
	 * Set drawable for foreground-background-button at the very left in the button bar.
	 */
	public void changeForeBackgroundButton(){
		if (drawForeground) {
			drawForeground = false;
			Toast.makeText(mActivity, R.string.instruction_drawing_background, Toast.LENGTH_LONG).show();
		} else {
			drawForeground = true;
			setDrawNewScribble(true);
			resetPath();
			Toast.makeText(mActivity, R.string.instruction_drawing_foreground, Toast.LENGTH_LONG).show();
		}
		setForeBackgroundButton();
	}
	
	/**
	 * Sets the icon for the button on the very left in button bar.
	 */
	public void setForeBackgroundButton(){
		if (drawForeground) {
			foreBackButton.setImageDrawable(getResources().getDrawable(R.drawable.f_icon));
		} else {
			foreBackButton.setImageDrawable(getResources().getDrawable(R.drawable.b_icon));
		}
	}
	
}
