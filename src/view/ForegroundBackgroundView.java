/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package view;

import java.util.ArrayList;
import java.util.List;

import com.genericclassificationapp.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageButton;
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
//	private Path mForePath = new Path();
//	private Path mBackPath = new Path();
	private Path mPath = new Path();
	private List<Path> mForePathList = new ArrayList<Path>();
	private List<Path> mBackPathList = new ArrayList<Path>();
	private Paint mForePaint = new Paint(mPaint);
	private Paint mBackPaint = new Paint(mPaint);
	private float mX;
	private float mY;
	private Selection mCurrentSelection = Selection.FOREGROUND;
//	private boolean drawCircle = true;
	private boolean drawForeground = true;
	ImageButton foreBackButton;
	
	public enum Selection {
		FOREGROUND, BACKGROUND
	}
	
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
		mForePaint.setColor(Color.YELLOW);
		mBackPaint.setColor(Color.MAGENTA);
		this.foreBackButton = foreBackButton;
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
//		Log.d(TAG, "Draw New Scribble - Boolean: "+super.drawNewScribble);
		if(drawNewScribble){
//			if(mBackPath != null && !mBackPath.isEmpty())
//				mPicture.addScribbleToList(currentScribble);
			currentScribble = new ForeBackGround(mForePathList, mBackPathList, new Paint(mForePaint), new Paint(mBackPaint));
			mPicture.addScribbleToList(currentScribble);
			drawNewScribble = false;
		}

//		drawCircle = true;
		
//		if (drawForeground) {
//			mForePath.reset();
//			mForePath.moveTo(x, y);
//		} else {
//			mBackPath.reset();
//			mBackPath.moveTo(x, y);
//		}
		
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
		
//		if(drawForeground){
//			if (!mForePath.isEmpty()) {
//				float dx = Math.abs(x - mX);
//				float dy = Math.abs(y - mY);
//				if (dx >= 4 || dy >= 4) {
//					mForePath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
//					mX = x;
//					mY = y;
//				}
//			} else {
//				startMove(x, y);
//			}
//		} else {
//			if (!mBackPath.isEmpty()) {
//				float dx = Math.abs(x - mX);
//				float dy = Math.abs(y - mY);
//				if (dx >= 4 || dy >= 4) {
//					mBackPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
//					mX = x;
//					mY = y;
//				}
//			} else {
//				startMove(x, y);
//			}
//		}
		
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
	
		
//		if(drawCircle)
//			drawCircle = false;
		
		invalidate();
	}
	
	/**
	 * Called when user stops his touch
	 * @param x coordinate of the current touch event
	 * @param y coordinate of the current touch event
	 */
	public void stopMove(float x, float y){
//		Log.d(TAG, "Draw New Scribble - Boolean: "+super.drawNewScribble);
//		if(drawForeground){
//			mForePath.lineTo(x, y);
//			drawForeground = false;
//			Toast.makeText(mActivity, "Draw Background", Toast.LENGTH_SHORT).show();
//		} else {
//			mBackPath.lineTo(x, y);
//			currentScribble = new ForeBackGround(mForePathList, mBackPathList, new Paint(mForePaint), new Paint(mBackPaint));
//		}
		
//		if (drawForeground) {
//			mForePathList.add(new Path(mForePath));
//		} else {
//			mBackPathList.add(mBackPath);
//		}
		
		mPath.lineTo(x, y);
		
		foreBackButton.setImageDrawable(getResources().getDrawable(R.drawable.check));
		
		if (drawForeground) {
			mForePathList.add(new Path(mPath));
		} else {
			mBackPathList.add(new Path(mPath));
		}
		
	//	drawCircle = true;
		invalidate();
	}
	
	/**
	 * Resets the last drawn path.
	 */
	public void resetPath(){
//		mForePath.reset();
//		mBackPath.reset();
		mPath.reset();
		mX = 0;
		mY = 0;
//		drawForeground = true;
//		drawCircle = true;
		invalidate();
	}
	
	@Override
	public void drawCurrentScribble(Canvas canvas) {
		
//		int color = mPaint.getColor();
		
//		if (mPicture.getScribbles() != null && !mPicture.getScribbles().isEmpty()) {
//			for (Scribble s : mPicture.getScribbles()) {
//				s.drawScribble(canvas);
//			}
//		}
		
//		if(mForePath != null && !mForePath.isEmpty()){
		
////		if (drawCircle){
////			mPaint.setStyle(Paint.Style.FILL);
////			canvas.drawCircle(mX, mY, mPaint.getStrokeWidth()/2, mPaint);
////		}else{
		
			
//			mPaint.setColor(Color.YELLOW);
//			mPaint.setStyle(Paint.Style.STROKE);
//			canvas.drawPath(mForePath, mPaint);
			
			
//		}
//		} 
		
//		if (mBackPath != null && !mBackPath.isEmpty()) {
//			mPaint.setColor(Color.MAGENTA);
//			canvas.drawPath(mBackPath, mPaint);
//		}
		
		for(Path p : mForePathList) {
			canvas.drawPath(p, mForePaint);
		}
		
		for(Path p : mBackPathList) {
			canvas.drawPath(p, mBackPaint);
		}
		
		if(drawForeground) {
			canvas.drawPath(mPath, mForePaint);
		} else {
			canvas.drawPath(mPath, mBackPaint);
		}
		
//		mPaint.setColor(color);
	}

	@Override
	public void resetLastDrawing() {
		if(drawForeground) {
			if(mForePathList.size()>0)
				mForePathList.remove(mForePathList.size()-1);
		} else {
			if(mBackPathList.size()>0)
				mBackPathList.remove(mBackPathList.size()-1);
		}
		resetPath();
		invalidate();
	}
	
	public void resetAllDrawings(){
		mPath = new Path();
		mForePathList = new ArrayList<Path>();
		mBackPathList = new ArrayList<Path>();
		invalidate();
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
	public void setForeBackgroundButton(){
		if (drawForeground) {
			foreBackButton.setImageDrawable(getResources().getDrawable(R.drawable.b_icon));
			drawForeground = false;
			
		} else {
			foreBackButton.setImageDrawable(getResources().getDrawable(R.drawable.f_icon));
			drawForeground = true;
			setDrawNewScribble(true);
		}
	}
	
	@Override
	public void setStrokeWidth(int width){
		if(drawForeground) {
			mForePaint.setStrokeWidth(width);
		} else {
			mBackPaint.setStrokeWidth(width);
		}
		invalidate();
	}

}
