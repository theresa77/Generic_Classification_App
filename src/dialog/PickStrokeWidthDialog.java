/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package dialog;

import com.genericclassificationapp.R;
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
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;


/**
 * Dialog for setting stoke width of user scribbles.
 * 
 * @author Theresa Froeschl
 * @version 1.0
 *
 */
public class PickStrokeWidthDialog extends DialogFragment {

	/**
	 * Called when Dialog gets created.
	 */
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		final UserScribbleMainActivity mActivity = (UserScribbleMainActivity)getActivity();
		final Dialog dialog = new Dialog(mActivity);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.pick_stroke_width_dialog);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		
		View view = (View) dialog.findViewById(R.id.line_picker_view); 
		LinearLayout.LayoutParams frameParams = (LinearLayout.LayoutParams)  view.getLayoutParams();
		frameParams.height = (int) mActivity.getPaint().getStrokeWidth();
		view.setLayoutParams(frameParams);
		view.setBackgroundColor(mActivity.getPaint().getColor());
		
		SeekBar lineController = (SeekBar) dialog.findViewById(R.id.line_controller_bar);
		lineController.setProgress((int) mActivity.getPaint().getStrokeWidth());
		
		// set listener for seek bar
		// updates preview for stroke width in the dialog
        lineController.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progressChanged = 0;
 
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                progressChanged = progress;
                setViewHeight(progressChanged);
            }
            
            public void onStartTrackingTouch(SeekBar seekBar) {
            	setViewHeight(progressChanged);
            }
            
            public void onStopTrackingTouch(SeekBar seekBar) {
            	setViewHeight(progressChanged);
            }
            
            private void setViewHeight(int height){
            	View view = (View) dialog.findViewById(R.id.line_picker_view);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
                params.height = height;
                view.setLayoutParams(params);
            }
        });
		
        // get button for picking the selected stroke width and set a click listener 
        ImageButton mPickButton = (ImageButton) dialog.findViewById(R.id.pick_line_button);
        mPickButton.setOnClickListener(
	            new View.OnClickListener() {
	                @Override
	                public void onClick(View v) {
	                	((UserScribbleMainActivity)getActivity()).
	                		setStrokeWidth((dialog.findViewById(R.id.line_picker_view)).getLayoutParams().height);
	                	Toast.makeText(getActivity(), R.string.select_stroke_width, Toast.LENGTH_SHORT).show();
	                }
	            }
	        );

		return dialog;
	}
}
