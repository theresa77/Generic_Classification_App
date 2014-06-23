/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package dialog;

import com.genericclassificationapp.R;
import view.ForegroundBackgroundView.Selection;
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


/**
 * Dialog for selection of foreground or background
 * 
 * @author Theresa Froeschl
 * @version 1.0
 *
 */
public class PickForegroundBackgroundDialog extends DialogFragment {

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		final UserScribbleMainActivity activity = (UserScribbleMainActivity) getActivity();
		final Dialog dialog = new Dialog(getActivity());
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.pick_foreground_background_dialog);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		
		// get image button for selecting foreground and set listener
		ImageButton foreButton = (ImageButton) dialog.findViewById(R.id.pick_foreground_button);
		foreButton.setMinimumHeight((int)(activity.getDisplayHeight()*0.1));
		foreButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	activity.setForeBackgroundSelection(Selection.FOREGROUND);
            	dialog.dismiss();
            	Toast.makeText(getActivity(), R.string.select_foreground, Toast.LENGTH_SHORT).show();
            }
        });
		
		// get image button for selecting background and set listener
		ImageButton backButton = (ImageButton) dialog.findViewById(R.id.pick_background_button);
		backButton.setMinimumHeight((int)(activity.getDisplayHeight()*0.1));
		backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	activity.setForeBackgroundSelection(Selection.BACKGROUND);
            	dialog.dismiss();
            	Toast.makeText(getActivity(), R.string.select_background, Toast.LENGTH_SHORT).show();
            }
        });
		
		return dialog;
    }
}
