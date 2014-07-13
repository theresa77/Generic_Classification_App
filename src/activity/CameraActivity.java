/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package activity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import view.CameraPreview;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.genericclassificationapp.R;

import domain.Picture;

/**
 * Activity for making a picture with the camera from the mobile phone
 * with this activity the application starts
 * 
 * @author Theresa Froeschl
 * @version 1.0
 *
 */
public class CameraActivity extends Activity {

	private static final String TAG = CameraActivity.class.getSimpleName();
	private Camera mCamera;
    private CameraPreview mPreview;
    private boolean useBackCamera = true;
    private static int RESULT_LOAD_IMAGE = 1;
    private Bitmap mPictureBitmap;
    private int displayWidth;
	private int displayHeight; 
	private LinearLayout.LayoutParams mPreviewParams;
	
	/**
	 * Local variable for the PictureCallback
	 */
	private PictureCallback mPictureCallback = new PictureCallback() {

		/**
		 * Called when a picture is taken
		 * @param data byte array of the picture
		 * @param camera camera instance with which the picture was taken
		 */
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.d(TAG, "onPictureTaken() called");
			
			recyclePictureBitmap();

			int reqWidth = 0;
			int reqHeight = 0;
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				reqWidth = displayHeight;
				reqHeight = displayWidth;

			} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				reqWidth = displayWidth;
				reqHeight = displayHeight;
			}

			// create a Bitmap from the byte array with the width and height of the screen
			mPictureBitmap = decodeSampledBitmapFromResource(data, reqWidth, reqHeight);

			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				//if the picture is in portrait it has to be rotated 
				Matrix matrix = new Matrix();
				matrix.postRotate(90);
				mPictureBitmap = Bitmap.createBitmap(mPictureBitmap, 0, 0,
						mPictureBitmap.getWidth(), mPictureBitmap.getHeight(),
						matrix, true);
				
				//create picture instance with the bitmap and an flag if the picture is in landsape 
				Picture.createInstance(mPictureBitmap, false);

			} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				Picture.createInstance(mPictureBitmap, true);
			}

			// start PictureActivty to view the Picture
			Intent pictureIntent = new Intent(CameraActivity.this, PictureActivity.class);
			startActivity(pictureIntent);
			CameraActivity.this.finish();
		}
	};
    
	/**
	 * Called when die Activity is created.
	 * Loads the content view for the camera preview
	 * and sets all local variables of the class 
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // get Width and Height of the display from the device
        WindowManager winMan = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = winMan.getDefaultDisplay();
	    DisplayMetrics metrics = new DisplayMetrics();
    	display.getMetrics(metrics);
    	displayHeight = metrics.heightPixels;
        displayWidth = metrics.widthPixels;
        
        if(displayWidth > displayHeight)
        	setContentView(R.layout.camera_preview_landscape);
         else
        	setContentView(R.layout.camera_preview_portrait);
 
        //instantiate camera variable and preview
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(this, mCamera);
        
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        LinearLayout layout = (LinearLayout) findViewById(R.id.camera_preview_layout);
        ImageButton captureButton = (ImageButton) findViewById(R.id.button_capture);
        
	    double width = 0.0; 
	    double height = 0.0;
	    
	    // set height and width of the preview according of the height and width of the display
	    if(displayWidth > displayHeight){ 		// Display is in landscape
	    	Log.d(TAG, "Display in landscape");
	    	// calculate height and width for camera preview to fit on screen
	    	height = displayHeight;
	    	width = displayHeight * 1.33;
	    	
	    	// change layout orientation to show it in landscape format
	    	layout.setOrientation(LinearLayout.HORIZONTAL);
	    	captureButton.setMinimumWidth((int) (displayWidth*0.1));
	    	captureButton.setMinimumHeight(displayHeight);
	    	
	    	// calculate and set left padding to show preview in the middle of the screen
	    	int left = (int)((displayWidth - width - (displayWidth*0.1))/2);
	    	layout.setPadding(left, 0, 0, 0);
	    	mPreviewParams = new LinearLayout.LayoutParams((int)width, (int)height);
	    	mPreviewParams.rightMargin = left;
	    	
	    } else {	// Display is in portrait
	    	Log.d(TAG, "Display in portrait");
	    	// calculate height and width for camera preview to fit on screen
	    	width = displayWidth;
	    	height = displayWidth * 1.33;
	    	
	    	captureButton.setMinimumHeight((int) (displayHeight*0.1));
	    	captureButton.setMinimumWidth(displayWidth);
	    	
	    	// calculate and set top padding to show preview in the middle of the screen
	    	int top = (int) ((displayHeight - height - (displayHeight*0.1))/2);
	    	layout.setPadding(0, top, 0, 0);
	    	mPreviewParams = new LinearLayout.LayoutParams((int)width, (int)height);
	    	mPreviewParams.bottomMargin = top;
	    }
	    
	    // set new layout parameters and add preview to the content view
        preview.setLayoutParams(mPreviewParams);
	    preview.addView(mPreview);
        
    }
    
    /**
     * Gets called when the user clicks on the capture button from the content view
     * triggers an image capture 
     * @param v clicked Button
     */
    public void capturePhoto(View v){
    	Log.d(TAG, "capturePhoto() called"); 
    	if(mCamera != null) {
    		mCamera.takePicture(null, null, mPictureCallback);
    	}  	
    }
    
    /**
     * TODO
     * @param v
     */
    public void getPictureFromGallery(View v){
    	Log.d(TAG, "getPictureFromGallery() called"); 
    	Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);	 
    	startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
    
    /**
     * TODO
     */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		recyclePictureBitmap();

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
			Uri selectedImage = data.getData();
			
			byte[] inputData = null;
			try {
				InputStream inputStream = getContentResolver().openInputStream(selectedImage);

				ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

				// this is storage overwritten on each iteration with bytes
				int bufferSize = 1024;
				byte[] buffer = new byte[bufferSize];

				// we need to know how may bytes were read to write them to the byteBuffer
				int len = 0;
				while ((len = inputStream.read(buffer)) != -1) {
					byteBuffer.write(buffer, 0, len);
				}

				// and then we can return your byte array.
				inputData = byteBuffer.toByteArray();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// create a Bitmap from the byte array with the width and height of the screen
			mPictureBitmap = decodeSampledBitmapFromResource(inputData, displayWidth, displayHeight);

			if (mPictureBitmap.getHeight() > mPictureBitmap.getWidth()) {
				
				//create picture instance with the bitmap and an flag if the picture is in landsape 
				Picture.createInstance(mPictureBitmap, false);

			} else  {
				Picture.createInstance(mPictureBitmap, true);
			}
			
			// start PictureActivty to view the Picture
			Intent pictureIntent = new Intent(CameraActivity.this,PictureActivity.class);
			startActivity(pictureIntent);
			CameraActivity.this.finish();
		}
	}

	/**
	 * TODO
	 * @param v
	 */
    public void changeCamera(View v){
    	Log.d(TAG, "changeCamera() called");
    	if(useBackCamera)
    		useBackCamera = false;
    	else 
    		useBackCamera = true;
    	Bundle bundle = new Bundle();
    	onCreate(bundle);
    }
    
    /**
     * Returns a Camera object of the first back-facing camera on the device
     * @return a new Camera instance
     */
	private Camera getCameraInstance() {
		releaseCamera();
		Camera c = null;
        try {
        	// attempt to get a Camera instance
        	if(useBackCamera){
        		c = Camera.open(CameraInfo.CAMERA_FACING_BACK);
        		Log.d(TAG, "c = Camera.open("+Camera.CameraInfo.CAMERA_FACING_BACK+");");
        	}else {
        		//TODO: Hier wird eine RuntimeException geworfen weil das mit der front camera offenbar,
        		// komplizierter ist als gedacht.
        		// Recherche wird benötigt!!!
//        		c= Camera.open(CameraInfo.CAMERA_FACING_FRONT);
        		Log.d(TAG, "c = Camera.open("+Camera.CameraInfo.CAMERA_FACING_FRONT+");");
        		
        		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        	    int cameraCount = Camera.getNumberOfCameras();
        	    for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
        	        Camera.getCameraInfo(camIdx, cameraInfo);
        	        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
        	            try {
        	                c = Camera.open(camIdx);
        	            } catch (RuntimeException e) {
        	                Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
        	            }
        	        }
        	    }
        	}
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        	e.printStackTrace();
        }
        return c; // returns null if camera is unavailable
	}
	
	/**
	 * Called when the activity get on pause.
	 * Releasing the camera
	 */
	@Override
	protected void onPause() {
	    super.onPause();
	    releaseCamera();              // release the camera immediately on pause event
	}

	/**
	 * Method for releasing the camera
	 * After releas the camera variable is set null
	 */
	private void releaseCamera(){
	    if (mCamera != null){
	        mCamera.release();        // release the camera for other applications
	        mCamera = null;
	    }
	}
	
	/**
	 * Returns the layout-parameter for the camera preview
	 * @return layout-parameters of camera preview
	 */
	public LinearLayout.LayoutParams getPreviewParams(){
		return mPreviewParams;
	}
	
	/**
	 * TODO
	 */
	private void recyclePictureBitmap(){
		Log.d(TAG, "recyclePictureBitmap() called.");
		if(mPictureBitmap != null){
			mPictureBitmap.recycle();
			mPictureBitmap = null;
		}
	}

	/**
	 * Calculates inSampleSize value for scaling down the picture
	 * 
	 * @param options BitmapFactory.Options object for which the return value is calculated
	 * @param reqWidth value for the required width of the picture
	 * @param reqHeight value for the required height of the picture
	 * @return calculated inSampleSize value 
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	/**
	 * Created a scaled down Bitmap of the picture to fit the density of the device
	 * 
	 * @param data byte array of the picture
	 * @param reqWidth required width of the picture
	 * @param reqHeight required height of the picture
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromResource(byte[] data, int reqWidth, int reqHeight) {

		// Decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(data, 0, data.length, options);
	}

}
