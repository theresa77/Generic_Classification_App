/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package view;

import com.genericclassificationapp.R;

import activity.UserScribbleMainActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceView;
import android.widget.Toast;
import domain.Picture;
import domain.Scribble;


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
	protected Picture mPicture;
	private Bitmap mPictureBitmap;
	protected int displayWidth;
	protected int displayHeight;
	protected boolean drawNewScribble;
	protected Scribble currentScribble;
	protected boolean zoomEnabled;
	private final int INVALID_POINTER_ID = -1;
	protected float mPosX;
	protected float mPosY;
	private float mTouchStartX;
	private float mTouchStartY;
	private int mActivePointerId = INVALID_POINTER_ID;
	private ScaleGestureDetector mScaleDetector;
	protected float mScaleFactor = 1.0f;
	protected float focusX;
	protected float focusY;
	private float lastFocusX = -1;
	private float lastFocusY = -1;
	private Rect boundsBeforeScale = new Rect();
	private Rect boundsAfterScale = new Rect();
	private Rect boundsAfterTranslate = new Rect();
	private Rect zoomBounds = new Rect();

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
		Log.d(TAG, "init() called");
		mActivity = (UserScribbleMainActivity) context;
		mPaint = mActivity.getPaint();
		setBackgroundColor(Color.BLACK);
		drawNewScribble = false;
		mPicture = Picture.getInstance();
		mScaleDetector = new ScaleGestureDetector(mActivity, new ScaleListener());
	}
	
	/**
	 * Set color for user scribbles.
	 * @param color new color for user scribbles
	 */
	public void setUserScribbleColor(int color){
		if(currentScribble != null){
			mPicture.addScribbleToList(currentScribble);
			currentScribble = null;
		}
		resetLastDrawing();
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
		if(currentScribble != null){
			mPicture.addScribbleToList(currentScribble);
			currentScribble = null;
		}
		resetLastDrawing();
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
		mPictureBitmap = bitmap;
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
	
	@Override
	public void onDraw(Canvas canvas){
		canvas.save();
		canvas.getClipBounds(boundsBeforeScale);

		canvas.scale(mScaleFactor, mScaleFactor, focusX, focusY);
		canvas.getClipBounds(boundsAfterScale);

		canvas.translate(mPosX, mPosY);
		canvas.getClipBounds(boundsAfterTranslate);

		if(boundsAfterTranslate.left < 0){
			canvas.translate(boundsAfterTranslate.left, 0);
			mPosX = boundsAfterScale.left;
		}
		if(boundsAfterTranslate.right > boundsBeforeScale.right){
			canvas.translate((boundsBeforeScale.right-boundsAfterTranslate.right)*(-1), 0);
			mPosX = boundsAfterScale.right-boundsBeforeScale.right;
		}
		if(boundsAfterTranslate.top < 0){
			canvas.translate(0, boundsAfterTranslate.top);
			mPosY = boundsAfterScale.top;
		}
		if (boundsAfterTranslate.bottom > boundsBeforeScale.bottom) {
			canvas.translate(0, (boundsBeforeScale.bottom-boundsAfterTranslate.bottom)*(-1));
			mPosY = boundsAfterScale.bottom-boundsBeforeScale.bottom;
		}
		
		canvas.getClipBounds(zoomBounds);
		canvas.drawBitmap(mPictureBitmap, 0,0, null);
		
		if (mPicture.getScribbles() != null && !mPicture.getScribbles().isEmpty()) {
			for (Scribble s : mPicture.getScribbles()) {
				s.drawScribble(canvas);
			}
		}
		
		drawCurrentScribble(canvas);
		canvas.restore();
	}
	
	/**
	 * Set boolean flag for drawing new scribble and save old scribble. 
	 * @param drawNew boolean flag, true if user wants to draw a new scribble.
	 */
	public void setDrawNewScribble(boolean drawNew){
		drawNewScribble = drawNew;
		if(currentScribble != null){
			mPicture.addScribbleToList(currentScribble);
			Toast.makeText(mActivity, R.string.draw_new_scribble, Toast.LENGTH_SHORT).show();
			currentScribble = null;
		}
		resetLastDrawing();
		invalidate();
	}
	
	/**
	 * Returns true if user wants to draw a new scribble.
	 * @return true if user wants to draw a new scribble
	 */
	public boolean drawNewScribble(){
		return drawNewScribble;
	}
	
	/**
	 * Returns the last drawn scribble.
	 * @return current and last drawn scribble
	 */
	public Scribble getCurrentScribble() {
		return currentScribble;
	}
	
	/**
	 * Get bounds of zoomed area.
	 * @return bounds of zoom
	 */
	public Rect getZoomBounds(){
		return zoomBounds;
	}
	
	/**
	 * Returns scale factor of zoom.
	 * @return scale factor of zoom
	 */
	public float getZoomFactor(){
		return mScaleFactor;
	}
	
	/**
	 * Draws current user scribbles to the canvas method parameter.
	 * @param canvas object for drawing user scribbles
	 */
	public abstract void drawCurrentScribble(Canvas canvas);
	
	/**
	 * Resets last drawing. 
	 * Scribble get deleted.
	 */
	public abstract void resetLastDrawing();
	
	/**
	 * TODO
	 */
	public void removeLastScribble(){
		Log.d(TAG, "resetLastDrawing() called");
		
		if(currentScribble != null){
			Log.d(TAG, "current Scribble: " + currentScribble.toString());
			currentScribble = null;
			resetLastDrawing();
		} else {
			Log.d(TAG, "current Scribble: NULL");
			mPicture.removeLastScribble();
		}
		
		invalidate();
	}
	
	/**
	 * Called when user touches screen inside the picture.
	 * @param action value of touch
	 * @param x x-position of touch
	 * @param y y-position of touch
	 */
	public abstract void handleTouchEvent(int action, float x, float y);
	
	/**
	 * Called when user touches screen outside of the picture.
	 * @param action value of the touch
	 */
	public abstract void handleTouchEventOutsidePicture(int action);
	
	/**
	 * Called when zoom is enabled. Handle multitouch for zooming.
	 * @param event 
	 */
	public void handleTouchZoomEvent(MotionEvent event){
		// Let the ScaleGestureDetector inspect all events.
		mScaleDetector.onTouchEvent(event);
		int action = event.getAction();
		
		switch (action & MotionEvent.ACTION_MASK) {
		
		case MotionEvent.ACTION_DOWN: {

			mTouchStartX = event.getX() / mScaleFactor;
			mTouchStartY = event.getY() / mScaleFactor;
			mActivePointerId = event.getPointerId(0);

			break;
		}

		case MotionEvent.ACTION_MOVE: {
			int pointerIndex = event.findPointerIndex(mActivePointerId);
			float x = (event.getX(pointerIndex)) / mScaleFactor;
			float y = (event.getY(pointerIndex)) / mScaleFactor;

			// Only move if the ScaleGestureDetector isn't processing a gesture.
			if (!mScaleDetector.isInProgress()) {

				if (mScaleFactor > 1.0f) {
					mPosX += (x - mTouchStartX);
					mPosY += (y - mTouchStartY);
					
					invalidate();
				}
			}

			mTouchStartX = x;
			mTouchStartY = y;

			break;
		}

		case MotionEvent.ACTION_UP: {
			mActivePointerId = INVALID_POINTER_ID;
			break;
		}

		case MotionEvent.ACTION_CANCEL: {
			mActivePointerId = INVALID_POINTER_ID;
			break;
		}

		case MotionEvent.ACTION_POINTER_UP: {

			int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
			int pointerId = event.getPointerId(pointerIndex);
			if (pointerId == mActivePointerId) {
				// This was our active pointer going up. Choose a new active pointer and adjust accordingly.
				int newPointerIndex = pointerIndex == 0 ? 1 : 0;
				mTouchStartX = event.getX(newPointerIndex) / mScaleFactor;
				mTouchStartY = event.getY(newPointerIndex) / mScaleFactor;
				mActivePointerId = event.getPointerId(newPointerIndex);
			}
			break;
		}
		}
		
	}
	
	
	/**
	 * Private class for zooming in and out on the taken picture.
	 * 
	 * @author Theresa Froeschl
	 *
	 */
	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		
		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			lastFocusX = -1;
			lastFocusY = -1;
			return true;
		}

		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			mScaleFactor *= detector.getScaleFactor();

			focusX = detector.getFocusX();
			focusY = detector.getFocusY();
			
			if (lastFocusX == -1)
				lastFocusX = focusX;
			if (lastFocusY == -1)
				lastFocusY = focusY;

			mPosX += (focusX - lastFocusX);
			mPosY += (focusY - lastFocusY);

			// Don't let the object get too small or too large.
			mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, 2.0f));

			lastFocusX = focusX;
			lastFocusY = focusY;
		
			invalidate();
			return true;
		}

	}

}
