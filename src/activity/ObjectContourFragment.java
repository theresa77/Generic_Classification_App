/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package activity;

import view.ObjectContourView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.genericclassificationapp.R;

//import domain.Scribble.ScribbleType;

/**
 * Fragment for drawing Object Contour.
 * Sets the content view of UserScribbleMainActivity when the Object Contour tab is selected.
 *  
 * @author Theresa Froeschl
 * @version 1.0
 *
 */
public class ObjectContourFragment extends UserScribbleFragment {

	private static final String TAG = ObjectContourFragment.class.getSimpleName();

	/**
	 * Sets the content view of the activity
	 * and returns the new created view
	 */
	@Override
	protected View setCustomContentView(boolean isLandscape) {
		Log.d(TAG, "setCustomContentView called");
		
//		mActivity.setCurrentScribble(ScribbleType.OBJECT_CONTOUR);
		View view;

		if (isLandscape) {
			// if the picture is in landscape the landscape-view is set
			view = inflater.inflate(R.layout.object_contour_view_landscape, container, false);

		} else {
			// if the picture is in portrait the portrait-view is set 
			view = inflater.inflate(R.layout.object_contour_view_portrait, container, false);
		}

		frameView = (FrameLayout) view.findViewById(R.id.object_contour_surface_view);
//		frameView = (ScrollView) view.findViewById(R.id.object_contour_surface_view);
		
		// create new view for drawing object contour
		if(mView == null){
			mView = new ObjectContourView(mActivity);
		} else {
			mView = new ObjectContourView(mActivity, mView);
		}
		
		
		Toast.makeText(getActivity(), R.string.select_object_contour, Toast.LENGTH_SHORT).show();
		
		return view;
	}

}
