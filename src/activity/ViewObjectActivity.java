/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package activity;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.genericclassificationapp.R;

import domain.Picture;
import domain.Scribble;

/**
 * Activity to show the marked object with the scribbles.
 * 
 * @author Theresa Froeschl
 * @version 1.0
 *
 */
public class ViewObjectActivity extends Activity {
	
	private static final String TAG = ViewObjectActivity.class.getSimpleName();
	private Picture mPicture;
	private int displayWidth;
	private int displayHeight;
	private Bitmap mPictureBitmap;
	private List<Scribble> scribbles;
	private RectF bounds;
	private LinearLayout.LayoutParams params;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// get Picture instance
		mPicture = Picture.getInstance();
		
		// get Height and Width of display
		WindowManager winMan = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = winMan.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		displayHeight = metrics.heightPixels;
		displayWidth = metrics.widthPixels;
		
		double width = 0.0; 
	    double height = 0.0; 
		
		if (!mPicture.isLandscape()) {// Picture on Portrait

			// set orientation of activity to portrait
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			setContentView(R.layout.view_object_activity_portrait);

			ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
			backButton.setMinimumHeight((int) (displayHeight * 0.1));
			
			width = displayWidth;
	    	height = displayWidth * 1.33;
	    	
	    	// calculate and set top and bottom padding to show preview in the middle of the screen
	    	int topBottom = (int) ((displayHeight - height - (displayHeight*0.1))/2);
	    	params = new LinearLayout.LayoutParams((int)width, (int)height);
	    	params.bottomMargin = topBottom;
	    	params.topMargin = topBottom;

		} else { // Picture in landscape

			// set orientation of activity to landscape
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			setContentView(R.layout.view_object_activity_landscape);

			ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
			backButton.setMinimumWidth((int) (displayWidth * 0.1));
			
			height = displayHeight;
	    	width = displayHeight * 1.33;
	    	
	    	// calculate and set left and right padding to show preview in the middle of the screen
	    	int leftRight = (int)((displayWidth - width - (displayWidth*0.1))/2);
	    	params = new LinearLayout.LayoutParams((int)width, (int)height);
	    	params.rightMargin = leftRight;
	    	params.leftMargin = leftRight;
		}
		
		scribbles = mPicture.getScribbles();
		mPictureBitmap = Bitmap.createScaledBitmap(mPicture.getBitmap(), params.width, params.height, true);
		mPictureBitmap = createScaledBitmap(drawScribblesToBitmap(mPictureBitmap));
		
		ImageView image = (ImageView) findViewById(R.id.picture);
		image.setImageBitmap(mPictureBitmap);
	}
	
	/**
	 * Called when user presses the back button.
	 * Starts the main activity for drawing user scribbles.
	 * @param v pressed button
	 */
	public void backToUserScribbleActivity(View v){
		Intent newIntent = new Intent(this, UserScribbleMainActivity.class);
		startActivity(newIntent);
		this.finish();
	}
	
	/**
	 * Draws all scribbles to the bitmap.
	 * @param bitmap bitmap object to draw scribbles to.
	 * @return bitmap with scribbles
	 */
	private Bitmap drawScribblesToBitmap(Bitmap bitmap){
		Canvas canvas = new Canvas(bitmap);
		
		if (scribbles != null && !scribbles.isEmpty()) {
			for (Scribble s : scribbles) {
				if(s != null)
					s.drawScribble(canvas);
			}
		}
		return bitmap;
	}
	
	/**
	 * Scales and clips the bitmap to only show the object and the scribbles.
	 * @param bitmap to scale and clip
	 * @return the scaled and clipped bitmap
	 */
	private Bitmap createScaledBitmap(Bitmap bitmap){
		bitmap = drawScribblesToBitmap(bitmap);
		bounds = calculateBoundingBoxForScribbles();
		
		float width = bounds.right - bounds.left;
		bounds.left = (float) (bounds.left - width*0.15);
		bounds.right = (float) (bounds.right + width*0.15);
		
		if(bounds.left < 0){
			bounds.left = 0;
		}
		
		if(bounds.right > bitmap.getWidth()) {
			bounds.right = bitmap.getWidth();
		}
		
		width = bounds.right - bounds.left;
		
		float height = bounds.bottom - bounds.top;
		bounds.top = (float) (bounds.top - height*0.15);
		bounds.bottom = (float) (bounds.bottom + height *0.15);
		
		if(bounds.top < 0){
			bounds.top = 0;
		}
			
		if(bounds.bottom > bitmap.getHeight()) {
			Log.d(TAG, "set bounds.bottom = bitmap.getHeight()  (1)");
			bounds.bottom = bitmap.getHeight();
		}
		height = bounds.bottom - bounds.top;
		
		return Bitmap.createBitmap(bitmap, (int)bounds.left, (int)bounds.top, (int)width, (int)height);
	}

	/**
	 * Calculate the bounding box for all scribbles.
	 * @return bounds for all scribbles
	 */
	private RectF calculateBoundingBoxForScribbles(){
		RectF bounds = null;
		RectF currScri = new RectF();
		
		for(Scribble scri : scribbles){
			if(bounds != null){
				currScri = scri.getBoundingBoxOfScribble();
				
				bounds = new RectF(Math.min(currScri.left, bounds.left), 
								   Math.min(currScri.top, bounds.top),
								   Math.max(currScri.right, bounds.right),
								   Math.max(currScri.bottom, bounds.bottom));
			} else {
				bounds = new RectF(scri.getBoundingBoxOfScribble());
			}
		}
		
		return bounds;
	}
	
}
