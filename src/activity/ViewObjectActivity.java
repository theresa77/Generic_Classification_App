package activity;

import com.genericclassificationapp.R;

import domain.Picture;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ViewObjectActivity extends Activity {
	
	private Picture mPicture;
	private int displayWidth;
	private int displayHeight;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// get Picture instance
		mPicture = Picture.getInstance();
		
		// get Height and Width of display
		WindowManager winMan = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = winMan.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		displayHeight = metrics.heightPixels;
		displayWidth = metrics.widthPixels;
		
		if (!mPicture.isLandscape()) {// Picture on Portrait

			// set orientation of activity to portrait
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			setContentView(R.layout.view_object_activity_portrait);

			ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
			backButton.setMinimumHeight((int) (displayHeight * 0.1));

		} else { // Picture in landscape

			// set orientation of activity to landscape
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			setContentView(R.layout.view_object_activity_landscape);

			ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
			backButton.setMinimumWidth((int) (displayWidth * 0.1));
		}

		// add taken picture to view
		ImageView image = (ImageView) findViewById(R.id.picture);
		image.setImageBitmap(mPicture.getBitmap());
	}
	
	
	public void backToUserScribbleActivity(View v){
		Intent newIntent = new Intent(this, UserScribbleMainActivity.class);
		startActivity(newIntent);
		this.finish();
	}

}
