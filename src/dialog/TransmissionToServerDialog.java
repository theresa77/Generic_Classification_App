/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.genericclassificationapp.R;

/**
 * Dialog to show progress for transmission for picture and scribbles to a server.
 * 
 * @author Theresa Froeschl
 * @version 1.0
 *
 */
public class TransmissionToServerDialog extends DialogFragment {
	
	private static final String TAG = TransmissionToServerDialog.class.getSimpleName();
	private ProgressBar progressBar;
	private Dialog dialog;
	private TextView responseText;
	
	/**
	 * Called when the dialog gets created.
	 * @param savedInstanceState
	 * @return the created dialog object
	 */
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		dialog = new Dialog(getActivity());
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.transmission_to_server_dialog);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		
		// get progress bar and textView for response text from server
		progressBar = (ProgressBar) dialog.findViewById(R.id.progressbar);
		responseText = (TextView) dialog.findViewById(R.id.server_response_text);
		
		// set listener for ok button
		ImageButton okButton = (ImageButton) dialog.findViewById(R.id.button_transmission_ok);
		okButton.setOnClickListener(
	            new View.OnClickListener() {
	                @Override
	                public void onClick(View v) {
	                	dialog.dismiss();
	                }
	            }
	        );
		
		return dialog;
	}
	
	/**
	 * Called to update the progress bar.
	 * @param progress new value for the progress of the progress bar
	 */
	public void updateProgressBar(int progress){
		progressBar.setProgress(progress);
	}
	
	/**
	 * Set the response text from the server under the progress bar.
	 * @param response text response from server
	 */
	public void setResponseText(String response){
		Log.d(TAG, "setResponseText() called");
		responseText.setText(response);
	}

}
