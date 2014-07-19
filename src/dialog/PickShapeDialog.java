/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package dialog;

import activity.UserScribbleMainActivity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.genericclassificationapp.R;

import domain.MinBoundingBox.Shape;


/**
 * Dialog for MinimumBoundingBoxView to select the current shape the user wants to draw
 * 
 * @author Theresa Froeschl
 * @version 1.0
 *
 */
public class PickShapeDialog extends DialogFragment {
	
	/**
	 * Called when Dialog gets created.
	 */
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		final UserScribbleMainActivity activity = (UserScribbleMainActivity) getActivity();
		final Dialog dialog = new Dialog(activity);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.pick_shape_dialog);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		
		// get image button for selecting rectangle shape and set click listener
		ImageButton rectangleButton = (ImageButton) dialog.findViewById(R.id.pick_rectangle_button);
		rectangleButton.setMinimumHeight((int)(activity.getDisplayHeight()*0.1));
		rectangleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	activity.setShape(Shape.RECTANGLE);
            	dialog.dismiss();
            	Toast.makeText(getActivity(), R.string.select_rectangle, Toast.LENGTH_SHORT).show();
            }
        });
		
		// get image button for selecting oval shape and set click listener
		ImageButton ovalButton = (ImageButton) dialog.findViewById(R.id.pick_oval_button);
		ovalButton.setMinimumHeight((int)(activity.getDisplayHeight()*0.1));
		ovalButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	activity.setShape(Shape.OVAL);
            	dialog.dismiss();
            	Toast.makeText(getActivity(), R.string.select_oval, Toast.LENGTH_SHORT).show();
            }
        });
		
		return dialog;
	}
	
}
