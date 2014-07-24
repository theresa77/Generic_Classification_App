package activity;

import java.util.List;

import view.ViewObjectView;

import com.genericclassificationapp.R;

import domain.Picture;
import domain.Scribble;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ViewObjectActivity extends Activity {
	
	private Picture mPicture;
	private ViewObjectView mView;
	private int displayWidth;
	private int displayHeight;
	private Bitmap mPictureBitmap;
	private List<Scribble> scribbles;
	private RectF bounds;

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
		
		LinearLayout.LayoutParams params;
		double width = 0.0; 
	    double height = 0.0;
//	    float marginTopLeft = 0; 
		
		if (!mPicture.isLandscape()) {// Picture on Portrait

			// set orientation of activity to portrait
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			setContentView(R.layout.view_object_activity_portrait);

			ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
			backButton.setMinimumHeight((int) (displayHeight * 0.1));
			
			width = displayWidth;
	    	height = displayWidth * 1.33;
	    	
	    	// calculate and set top and bottom padding to show preview in the middle of the screen
	    	int topBottom = (int) ((displayHeight - height - (displayHeight*0.1))/2);
	    	params = new LinearLayout.LayoutParams((int)width, (int)height);
	    	params.bottomMargin = topBottom;
	    	params.topMargin = topBottom;
//	    	marginTopLeft = (float) (top + displayHeight*0.1);

		} else { // Picture in landscape

			// set orientation of activity to landscape
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			setContentView(R.layout.view_object_activity_landscape);

			ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
			backButton.setMinimumWidth((int) (displayWidth * 0.1));
			
			height = displayHeight;
	    	width = displayHeight * 1.33;
	    	
	    	// calculate and set left and right padding to show preview in the middle of the screen
	    	int leftRight = (int)((displayWidth - width - (displayWidth*0.1))/2);
	    	params = new LinearLayout.LayoutParams((int)width, (int)height);
	    	params.rightMargin = leftRight;
	    	params.leftMargin = leftRight;
//	    	marginTopLeft = (float) (left + displayWidth*0.1);
		}
		
//		mView = new ViewObjectView(this);
//		mView.setBitmap(Bitmap.createScaledBitmap(mPicture.getBitmap(), params.width, params.height, false));
//		
//		FrameLayout pictureFrame = (FrameLayout) findViewById(R.id.picture_frame);
//		pictureFrame.setLayoutParams(params);
//		pictureFrame.addView(mView);
		
		scribbles = mPicture.getScribbles();
		mPictureBitmap = createScaledBitmap(drawScribblesToBitmap(mPicture.getBitmap()));
		
		ImageView image = (ImageView) findViewById(R.id.picture);
		image.setImageBitmap(mPictureBitmap);
	}
	
	
	public void backToUserScribbleActivity(View v){
		Intent newIntent = new Intent(this, UserScribbleMainActivity.class);
		startActivity(newIntent);
		this.finish();
	}
	
	
	private Bitmap drawScribblesToBitmap(Bitmap bitmap){
		Canvas canvas = new Canvas(bitmap);
//		canvas.drawBitmap(bitmap, 0, 0, null);
		
		if (scribbles != null && !scribbles.isEmpty()) {
			for (Scribble s : scribbles) {
				if(s != null)
					s.drawScribble(canvas);
			}
		}
		return bitmap;
	}
	
	private Bitmap createScaledBitmap(Bitmap bitmap){
		bitmap = drawScribblesToBitmap(bitmap);
		bounds = calculateBoundingBoxForScribbles();
		
		float width = bounds.right - bounds.left;
		bounds.left = (float) (bounds.left - width*0.15);
		bounds.right = (float) (bounds.right + width*0.15);
		if(bounds.left < 0)
			bounds.left = 0;
		if(bounds.right > bitmap.getWidth()) //--> größer als Bildschirm-Breite/Höhe
			bounds.right = bitmap.getWidth();
		width = bounds.right - bounds.left;
		
		float height = bounds.bottom - bounds.top;
		bounds.top = (float) (bounds.top - height*0.15);
		bounds.bottom = (float) (bounds.bottom + height *0.15);
		if(bounds.top < 0)
			bounds.top = 0;
		if(bounds.bottom > bitmap.getHeight()) // ---> größer als Bildschirm-Breite/Höhe
			bounds.bottom = bitmap.getHeight();
		height = bounds.bottom - bounds.top;
		
		return Bitmap.createBitmap(bitmap, (int)bounds.left, (int)bounds.top, (int)width, (int)height);
	}

	private RectF calculateBoundingBoxForScribbles(){
		RectF bounds = null;
		RectF currScri = new RectF();
		
		for(Scribble scri : scribbles){
			if(bounds != null){
				currScri = scri.getBoundingBoxOfScribble();
				
				bounds.left = Math.min(currScri.left, bounds.left);
				bounds.top = Math.min(currScri.top, bounds.top);
				bounds.right = Math.max(currScri.right, bounds.right);
				bounds.bottom = Math.max(currScri.bottom, bounds.bottom);
				
//				bounds = new RectF(Math.min(currScri.left, bounds.left), 
//								   Math.min(currScri.top, bounds.top),
//								   Math.max(currScri.right, bounds.right),
//								   Math.max(currScri.bottom, bounds.bottom));
			} else {
				bounds = scri.getBoundingBoxOfScribble();
			}
		}
		
		return bounds;
	}
	

}
