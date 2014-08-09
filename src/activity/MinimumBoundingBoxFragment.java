/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package activity;

import view.MinimumBoundingBoxView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.genericclassificationapp.R;

import domain.Scribble.ScribbleType;

/**
 * Fragment for drawing Minimum Bounding Box.
 * Sets the content view of UserScribbleMainActivity when user selects tab for drawing Minimum Bounding Box
 * 
 * @author Theresa Froeschl
 * @version 1.0
 *
 */
public class MinimumBoundingBoxFragment extends UserScribbleFragment {

	private static final String TAG = MinimumBoundingBoxFragment.class.getSimpleName();
	
	/**
	 * Sets the content view of the activity
	 * and returns the new created view
	 */
	@Override
	protected View setCustomContentView(boolean isLandscape) {
		Log.d(TAG, "setCustomContentView() called");
		
		mActivity.setCurrentScribble(ScribbleType.MINIMUM_BOUNDING_BOX);
		View view;
		
		if(isLandscape){
			// set view for picture in landscape
			view = inflater.inflate(R.layout.min_bounding_box_view_landscape, container, false);
			
		} else {
			// set view for picture in portrait
			view = inflater.inflate(R.layout.min_bounding_box_view_portrait, container, false);
		}
		
		frameView = (FrameLayout) view.findViewById(R.id.min_bounding_box_surface_view);
		
		// create new view for drawing Minimum Bounding Box
		if(mView == null){
			mView = new MinimumBoundingBoxView(mActivity);
		} else {
			mView = new MinimumBoundingBoxView(mActivity, mView);
		}
		
		Toast.makeText(getActivity(), R.string.select_min_bounting_box, Toast.LENGTH_SHORT).show();
		
		return view;
	}
}
