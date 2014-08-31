/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package view;

import java.io.IOException;

import activity.CameraActivity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;


/**
 * View class for camera preview
 * extends SurfaceView
 * implements SurfaceHolder.Callback 
 * 
 * @author Theresa Froeschl
 * @version 1.0
 *
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = CameraPreview.class.getSimpleName();
	private SurfaceHolder mHolder;
	private Camera mCamera;
    private CameraActivity mActivity;
    private Paint mPaint;
	
	@SuppressWarnings("deprecation")
	public CameraPreview(Context context, Camera camera) {
		super(context);
		mCamera = camera;
		mActivity = (CameraActivity)context;
		
		// Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mHolder.setSizeFromLayout();
        
        // create new Paint object for drawing the grid over camera preview
        mPaint = new Paint();
        mPaint.setAntiAlias(true);  
        mPaint.setStrokeWidth(3);  
        mPaint.setStyle(Paint.Style.STROKE);  
        mPaint.setColor(Color.argb(255, 255, 255, 255)); 
        
        setFocusable(true);
        setFocusableInTouchMode(true);
        setWillNotDraw(false);
	}
	
	public CameraPreview(Context context){
		super(context);
	}

	/**
	 * Called when the surface changes
	 * Starts camera preview again
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Log.d(TAG, "surfaceChanged(+"+holder.toString()+", "+format+", "+width+", "+height+") called");
		
        if (mHolder.getSurface() == null){
          return;
        }

        try {
            mCamera.stopPreview();
        } catch (Exception e){
          // ignore: tried to stop a non-existent preview
        }

        try {
        	// following code removed to CameraActivity when camera gets opened
//			Camera.Parameters p = mCamera.getParameters();
//			//TODO: Auto focus only possible for back camera, use it for front camera too
////			 p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
//			try {
//				// TODO: hier wird immer einer RuntimeException geworfen!
//				// hat vielleicht etwas mit der PreviewSize zu tun, da diese bei
//				// der
//				// front camera anders ist
//				// das ganze ist auch leider vom gerät abhängig.
//				mCamera.setParameters(p);
//			} catch (Exception e) {
//				p.set("camera-id", 2);
//				p.setPreviewSize(640, 480);
//				mCamera.setParameters(p);
//			}

        	// set camera orientation corresponding to display orientation
        	setCameraOrientation(getResources().getConfiguration().orientation);
        	
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            mCamera.autoFocus(null);
            
		} catch (Exception e) {
			e.printStackTrace();
//			Log.e(TAG, "Error setting camera preview: " + e.getMessage());
		}
        
        
	}

	/**
	 * Called when the surface gets created
	 * Restart camera preview
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "surfaceCreated( "+holder.toString()+" )"+" called");
		
		// following code removed to CameraActivity when camera gets opened
		try {
//			Camera.Parameters p = mCamera.getParameters();
//			// p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
//			try {
//				// TODO: hier wird immer einer RuntimeException geworfen!
//				// hat vielleicht etwas mit der PreviewSize zu tun, da diese bei
//				// der
//				// front camera anders ist
//				// das ganze ist auch leider von gerät abhängig.
//				mCamera.setParameters(p);
//			} catch (Exception e) {
//				p.set("camera-id", 2);
//				p.setPreviewSize(640, 480);
//				mCamera.setParameters(p);
//			}
//
			// set camera orientation corresponding to display orientation
        	setCameraOrientation(getResources().getConfiguration().orientation);
        	
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            mCamera.autoFocus(null);
            
		} catch (IOException e) {
			Log.e(TAG, "Error setting camera preview: " + e.getMessage());
		}
	}

	/**
	 * Called when the surface get destroyed.
	 * Camera gets released.
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "surfaceDestroyed( "+holder.toString()+" ) called");
		mCamera.release();
	}
	
	/**
	 * Called when user touches the camera preview.
	 * Activate auto focus.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event){
	    if(event.getAction() == MotionEvent.ACTION_DOWN){
	        Log.d("down", "focusing now");
	        mCamera.autoFocus(null); 
	    }
	    return true;
	}
	
	/**
	 * Draws grid over camera preview
	 */
	@Override  
    public void onDraw(Canvas canvas)  { 
		Log.d(TAG, "CameraPreview.onDraw() called");
		LinearLayout.LayoutParams params = mActivity.getPreviewParams();
		
		canvas.drawLine(0,0,0,params.height,mPaint);
		canvas.drawLine((params.width/5),0,(params.width/5),params.height,mPaint);
        canvas.drawLine((params.width/5)*2,0,(params.width/5)*2,params.height,mPaint);
		canvas.drawLine((params.width/5)*3,0,(params.width/5)*3,params.height,mPaint);
        canvas.drawLine((params.width/5)*4,0,(params.width/5)*4,params.height,mPaint);
        canvas.drawLine(params.width,0,params.width,params.height,mPaint);
        
        canvas.drawLine(0,0,params.width,0,mPaint);
        canvas.drawLine(0,(params.height/5),params.width,(params.height/5),mPaint);
        canvas.drawLine(0,(params.height/5)*2,params.width,(params.height/5)*2,mPaint);
        canvas.drawLine(0,(params.height/5)*3,params.width,(params.height/5)*3,mPaint);
        canvas.drawLine(0,(params.height/5)*4,params.width,(params.height/5)*4,mPaint);
        canvas.drawLine(0,params.height,params.width,params.height,mPaint);
    }
	
	public void setCameraInstance(Camera camera){
		mCamera = camera;
	}
	
	/**
	 * Sets the camera orientation corresponding to the orientation of the display
	 * @param orientation of the display
	 */
	private void setCameraOrientation(int orientation){
		if (orientation == Configuration.ORIENTATION_PORTRAIT) {
    		mCamera.setDisplayOrientation(90);
    		Log.d(TAG, "setDisplayOrientation(90)");
    	}
    	if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
    		mCamera.setDisplayOrientation(0);
    		Log.d(TAG, "setDisplayOrientation(0)");
    	}
	}

}
