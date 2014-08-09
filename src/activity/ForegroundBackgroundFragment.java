/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package activity;

import view.ForegroundBackgroundView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.genericclassificationapp.R;

import domain.Scribble.ScribbleType;

/**
 * Fragment for drawing Foreground or Background.
 * Sets the content view of UserScribbleMainActivity when user selects tab for drawing Foreground/Background
 * 
 * @author Theresa Froeschl
 * @version 1.0
 *
 */
public class ForegroundBackgroundFragment extends UserScribbleFragment {

	private static final String TAG = ForegroundBackgroundFragment.class.getSimpleName();
	
	/**
	 * Sets the content view of the activity
	 * and returns the new created view
	 */
	@Override
	protected View setCustomContentView(boolean isLandscape) {
		Log.d(TAG, "setCustomContentView called");
		
		mActivity.setCurrentScribble(ScribbleType.FOREGROUND);
		View view;
		
		if(isLandscape){
			// set view for picture in landscape
			view = inflater.inflate(R.layout.foreground_background_view_landscape, container, false);
			
		} else {
			// set view for picture in portrait
			view = inflater.inflate(R.layout.foreground_background_view_portrait, container, false);
		}
		
		frameView = (FrameLayout) view.findViewById(R.id.foreground_background_surface_view);
//		frameView = (ScrollView) view.findViewById(R.id.foreground_background_surface_view);
		
		// create new view for drawing foreground or background
		if(mView == null){
			mView = new ForegroundBackgroundView(mActivity);
		} else {
			mView = new ForegroundBackgroundView(mActivity, mView);
		}
		
		Toast.makeText(getActivity(), R.string.select_foreground_background, Toast.LENGTH_SHORT).show();
		
		return view;
	}

}
