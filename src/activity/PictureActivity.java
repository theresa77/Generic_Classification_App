/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package activity;

import com.genericclassificationapp.R;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import dialog.OptionMenuDialog;
import domain.Picture;

/**
* Activity to show the picture taken at CameraActivity
* 
* @author Theresa Froeschl
* @version 1.0
*
*/
public class PictureActivity extends FragmentActivity {

	private static final String TAG = PictureActivity.class.getSimpleName();
	private Picture mPicture;
	private Bitmap mPictureBitmap;
	private int displayWidth;
	private int displayHeight;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// get Picture instance
		mPicture = Picture.getInstance();
		mPictureBitmap = this.mPicture.getBitmap();
        
		// get Height and Width of display
		WindowManager winMan = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
	    Display display = winMan.getDefaultDisplay();
	    DisplayMetrics metrics = new DisplayMetrics();
	    display.getMetrics(metrics);
	    displayHeight = metrics.heightPixels;
	    displayWidth = metrics.widthPixels;
		  
		if (!mPicture.isLandscape()) {//Picture on Portait
			
			//set orientation of activity to portrait
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			setContentView(R.layout.picture_activity_portrait);
			
			ImageButton takeNewPhotoButton = (ImageButton) findViewById(R.id.button_take_new_photo);
			takeNewPhotoButton.setMinimumHeight((int)(displayHeight*0.1));
			
		} else {		//Picture in landscape
			
			//set orientation of activity to landscape
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			setContentView(R.layout.picture_activity_landscape);
			
			ImageButton takeNewPhotoButton = (ImageButton) findViewById(R.id.button_take_new_photo);
			takeNewPhotoButton.setMinimumWidth((int)(displayWidth*0.1));
		}
		
		// add taken picture to view
		ImageView image = (ImageView) findViewById(R.id.picture);
		image.setImageBitmap(mPictureBitmap);
		
	}
	
	/**
	 * Called when clicked on New Photo Button.
	 * Starts CameraActivity.
	 * @param v clicked Button
	 */
	public void takeNewPhoto(View v){
		Log.d(TAG, "Take New Photo Button pressed.");
		Intent cameraIntent = new Intent(PictureActivity.this, CameraActivity.class);
        startActivity(cameraIntent);
        PictureActivity.this.finish();
	}

	/**
	 * Called when clicked on Minimum Bounding Box Button.
	 * Starts UserScribbleMainActivity with the first tab selected
	 * @param v clicked Button
	 */
	public void selectMinimumBoundingBox(View v) {
		Intent newIntent = new Intent(getBaseContext(), UserScribbleMainActivity.class);	
		newIntent.putExtra("tab", 0);
		startActivity(newIntent);
		PictureActivity.this.finish();
	}

	/**
	 * Called when clicked on Foreground Button.
	 * Starts UserScribbleMainActivity with the second tab selected
	 * @param v clicked Button
	 */
	public void selectForegroundBackground(View v) {
		Intent newIntent = new Intent(getBaseContext(), UserScribbleMainActivity.class);
		newIntent.putExtra("tab", 1);
		startActivity(newIntent);
		PictureActivity.this.finish();
	}

	/**
	 * Called when clicked on Object Contour Button.
	 * Starts UserScribbleMainActivity with the third tab selected
	 * @param v clicked Button
	 */
	public void selectObjectContour(View v) {
		Intent newIntent = new Intent(getBaseContext(), UserScribbleMainActivity.class);
		newIntent.putExtra("tab", 2);
		startActivity(newIntent);
		PictureActivity.this.finish();
	}

	/**
	 * Returns the width of the display
	 * @return width of display
	 */
	public int getDisplayWidth() {
		return displayWidth;
	}

	/**
	 * Returns the height of the display
	 * @return height of display
	 */
	public int getDisplayHeight() {
		return displayHeight;
	}
	
	/**
	 * Called when clicked on the menu button.
	 * Opens a Dialog for Option Menu
	 * @param v clicked Button
	 */
	public void openMenu(View v){
		OptionMenuDialog dialog = new OptionMenuDialog();
		dialog.show(getSupportFragmentManager(), "OptionMenuDialog");
	}
	
}
