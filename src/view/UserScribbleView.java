/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package view;

import java.util.ArrayList;
import java.util.List;

import domain.Scribble;

import activity.UserScribbleMainActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;


/**
 * Abstract class for controlling the view of user scribble fragments in UserScribbleMainActivity
 * 
 * @author Theresa Froeschl
 * @version 1.0
 *
 */
public abstract class UserScribbleView extends SurfaceView {

	private static final String TAG = UserScribbleView.class.getSimpleName();
	protected UserScribbleMainActivity mActivity;
	protected Paint mPaint;
	protected Bitmap mPictureBitmap;
	protected int displayWidth;
	protected int displayHeight;
	protected boolean drawNewScribble;
//	protected List<Scribble> oldScribbles;
	protected Scribble currentScribble;

	public UserScribbleView(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	    init(context);
	}

	public UserScribbleView(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    init(context);
	}
	
	public UserScribbleView(Context context) {
	    super(context);
	    init(context);
	}
	
	/**
	 * Called when view get initialized.
	 * Sets Paint object for user scribbles.
	 * @param context
	 */
	private void init(Context context) {
		//this.mActivity = ((UserScribbleMainActivity)context);
		Log.d(TAG, "init() called");
		mActivity = (UserScribbleMainActivity) context;
		mPaint = mActivity.getPaint();
		setBackgroundColor(Color.BLACK);
		drawNewScribble = false;
//		oldScribbles = new ArrayList<Scribble>();
	}
	
	/**
	 * Set color for user scribbles.
	 * @param color new color for user scribbles
	 */
	public void setUserScribbleColor(int color){
		mPaint.setColor(color);
		invalidate();
	}
	
	/**
	 * Get color for user scribbles.
	 * @return current color for user scribbles
	 */
	public int getUserScribbleColor(){
		return mPaint.getColor();
	}
	
	/**
	 * Set stroke width for user scribbles.
	 * @param width stroke width for user scribbles
	 */
	public void setStrokeWidth(int width){
		mPaint.setStrokeWidth(width);
		invalidate();
	}
	
	/**
	 * Get stroke width for user scribbles.
	 * @return current stroke width for user scribbles
	 */
	public float getStrokeWidth(){
		return mPaint.getStrokeWidth();
	}
	
	/**
	 * Get Paint object for drawing user scribbles.
	 * @return paint object for user scribbles
	 */
	public Paint getPaint(){
		return mPaint;
	}
	
	/**
	 * Set Paint object for drawing user scribbles.
	 * @param paint object for user scribbles
	 */
	public void setPaint(Paint paint){
		mPaint = paint;
	}

	/**
	 * Set bitmap for the picture.
	 * @param bitmap object of the picture
	 */
	public void setBitmap(Bitmap bitmap){
		mPictureBitmap= bitmap;
	}
	
	/**
	 * bitmap for the picture.
	 * @return bitmap object of the picture
	 */
	public Bitmap getBitmap(){
		return mPictureBitmap;
	}
	
	/**
	 * Set display width variable which contains the width of the display from the device.
	 * @param displayWidth width of the display
	 */
	public void setDisplayWidth(int displayWidth) {
		this.displayWidth = displayWidth;
	}

	/**
	 * Set display height variable which contain the height of the display from the device.
	 * @param displayHeight height of the display
	 */
	public void setDisplayHeight(int displayHeight) {
		this.displayHeight = displayHeight;
	}
	
	/**
	 * TODO
	 * @param drawNew
	 */
	public void setDrawNewScribble(boolean drawNew){
		this.drawNewScribble = drawNew;
	}
	
	/**
	 * Draws current user scribbles to the canvas method parameter.
	 * @param canvas object for drawing user scribbles
	 */
	public abstract void drawUserScribble(Canvas canvas);
	
	/**
	 * Resets drawing. 
	 * Scribbles get deleted.
	 */
	public abstract void resetDrawing();
	
	/**
	 * TODO
	 * @param action
	 * @param x
	 * @param y
	 */
	public abstract void handleTouchEvent(int action, float x, float y);
	
	/**
	 * TODO
	 * @param action
	 */
	public abstract void handleTouchEventOutsidePicture(int action);
	
}
