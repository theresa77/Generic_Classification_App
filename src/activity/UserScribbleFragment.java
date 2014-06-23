/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package activity;

import view.UserScribbleView;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.genericclassificationapp.R;

import domain.Picture;

/**
 * Parent Fragment for UserScribbleActivity 
 * This class contains all methods which are used in all UserScribbleFragments
 * 
 * @author Theresa Froeschl
 * @version 1.0
 *
 */
public abstract class UserScribbleFragment extends Fragment {
	
	protected UserScribbleMainActivity mActivity;
	private static final String TAG = UserScribbleFragment.class.getSimpleName();
	protected UserScribbleView mView;
	protected Picture mPicture;
	protected FrameLayout frameView;
	protected LayoutInflater inflater;
	protected ViewGroup container;
	
	/**
	 * Called when View gets created.
	 * Sets local variables.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mActivity = (UserScribbleMainActivity) getActivity();
		this.inflater = inflater;
		this.container = container;

		mPicture = Picture.getInstance();
        Bitmap mPictureBitmap = mPicture.getBitmap();
        
        // set content view
        View view = setCustomContentView(mPicture.isLandscape());

        // get height and width of the display
		int displayHeight = mActivity.getDisplayHeight();
	    int displayWidth = mActivity.getDisplayWidth();
	    mView.setDisplayHeight(displayHeight);
	    mView.setDisplayWidth(displayWidth);
	   
	    double width = 0.0;
    	double height = 0.0;
    	float marginTopLeft = 0; 
    	
    	LinearLayout.LayoutParams pictureParams;
    	LinearLayout.LayoutParams frameParams = (LinearLayout.LayoutParams) frameView.getLayoutParams();
    	ImageButton menuButton = (ImageButton) view.findViewById(R.id.menu_button);
		
		if (!mPicture.isLandscape()) {
			// set orientation of the activity to portrait
			mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			
			height = (int) displayWidth * 1.33;
	    	width = displayWidth;

	    	// calculate top margin for picture frame
			int top = (int)((displayHeight - height - (displayHeight*0.2))/2);
			
			// add top margin value to frame parameters to show picture in the middle of the screen
			frameParams.topMargin = top;
			marginTopLeft = (float) (top + displayHeight*0.1);
			
			menuButton.setMinimumHeight((int)(displayHeight*0.1));
			
    	} else {
    		// set orientation of the activity to landscape
    		mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    		
    		width = (int) displayHeight * 1.33;
	    	height = displayHeight;
			
	    	// calculate left margin for picture 
			int left = (int)((displayWidth - width - (displayWidth*0.2))/2);
			
			// add left margin value to frame parameters to show picture in the middle of the screen
			frameParams.leftMargin = left;
			marginTopLeft = (float) (left + displayWidth*0.1);
			
			menuButton.setMinimumWidth((int)(displayWidth*0.1));
    	}
		
		// create new layout parameter object with calculated width and height for the bitmap
	    pictureParams = new LinearLayout.LayoutParams((int)width, (int)height);
	    
	    // creates scaled bitmap and adds it to view
		mPictureBitmap = Bitmap.createScaledBitmap(mPictureBitmap, pictureParams.width, pictureParams.height, false);
		mView.setBitmap(mPictureBitmap);
		
		// set new frame view parameters and add new user scribble view to it
		frameView.setLayoutParams(frameParams);
		frameView.addView(mView);
		
		mActivity.setMarginTopLeft(marginTopLeft);
		mActivity.setCurrentView(mView);
		
		Log.d(TAG, "View created");
		return view;
	}
	
	/**
	 * Method for setting the content view of the activity,
	 * @param isLandscape true when picture is in landscape, false when it is in portrait
	 * @return the current view for the activity
	 */
	protected abstract View setCustomContentView(boolean isLandscape);
	
}


