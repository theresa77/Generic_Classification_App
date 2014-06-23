/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package dialog;

import view.ColorPickerView;
import activity.UserScribbleMainActivity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.genericclassificationapp.R;

 
/**
 * DialogFragment to show color picker from which the user can choose a color for drawing
 * 
 * @author Theresa Froeschl
 * @version 1.0
 *
 */
public class ColorPickerDialog extends DialogFragment {

	private ColorPickerView mColorPickerView;
	
	/**
	 * Called when Dialog gets created
	 */
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Dialog dialog = new Dialog(getActivity());
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		if(((UserScribbleMainActivity)getActivity()).getDisplayHeight()<=700){
			dialog.setContentView(R.layout.pick_color_dialog_low);
		} else {
			dialog.setContentView(R.layout.pick_color_dialog);
		}
		
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
			
		FrameLayout view = (FrameLayout) dialog.findViewById(R.id.color_picker_view); 
		mColorPickerView = new ColorPickerView((UserScribbleMainActivity)getActivity());
		LinearLayout.LayoutParams frameParams = (LinearLayout.LayoutParams)  view.getLayoutParams();
		frameParams.leftMargin = 20;
		view.setLayoutParams(frameParams);
		view.addView(mColorPickerView);
		
		//dialog.getWindow().setLayout(296, LayoutParams.WRAP_CONTENT);
		
		ImageButton mPickButton = (ImageButton) dialog.findViewById(R.id.pick_color_button); 
		mPickButton.setOnClickListener(
	            new View.OnClickListener() {
	                @Override
	                public void onClick(View v) {
	                	((UserScribbleMainActivity)getActivity()).
	                		setUserScribbleColor(mColorPickerView.getCurrentColor());
	                	Toast.makeText(getActivity(), R.string.select_color, Toast.LENGTH_SHORT).show();
	                }
	            }
	        );
		
		return dialog;
	}
}