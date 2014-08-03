/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import domain.ObjectContour;
import domain.Scribble;

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
	
//	/**
//	 * Draw picture and current scribble the canvas object.
//	 */
//	public void onDraw(Canvas canvas){
//		// Log.d(TAG, "onDraw() is called");
//		
//			canvas.drawBitmap(mPictureBitmap, 0, 0, null);
//
//			if (mPicture.getScribbles() != null
//					&& !mPicture.getScribbles().isEmpty()) {
//				for (Scribble s : mPicture.getScribbles()) {
//					s.drawScribble(canvas);
//				}
//			}
//
//			if (!mPath.isEmpty())
//				canvas.drawPath(mPath, mPaint);
//		
//	}
	
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
		mPath.close();
		currentScribble = new ObjectContour(new Path(mPath), new Paint(mPaint));
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
		if (mPicture.getScribbles() != null && !mPicture.getScribbles().isEmpty()) {
			for (Scribble s : mPicture.getScribbles()) {
				s.drawScribble(canvas);
			}
		}
		canvas.drawPath(mPath, mPaint);
	}
	
	@Override
	public void drawUserScribble(Canvas canvas, Rect canvasRect, Rect zoomRect) {
		
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

	@Override
	public void drawFurtherThings(Canvas canvas) {
		// TODO Auto-generated method stub
		
	}

}
