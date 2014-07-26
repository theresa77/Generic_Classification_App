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
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ViewObjectActivity extends Activity {
	
	private static final String TAG = ViewObjectActivity.class.getSimpleName();
	private Picture mPicture;
	private ViewObjectView mView;
	private int displayWidth;
	private int displayHeight;
	private Bitmap mPictureBitmap;
	private List<Scribble> scribbles;
	private RectF bounds;
	private LinearLayout.LayoutParams params;

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
		
//		LinearLayout.LayoutParams params;
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
		mPictureBitmap = Bitmap.createScaledBitmap(mPicture.getBitmap(), params.width, params.height, false);
		mPictureBitmap = createScaledBitmap(drawScribblesToBitmap(mPictureBitmap));
		
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
		int frameWidth = params.width;
		int frameHeight = params.height;
		Log.d(TAG, "frameWidth: "+ frameWidth);
		Log.d(TAG, "frameHeight: "+ frameHeight);
		Log.d(TAG, "bounds - LEFT (1): " + bounds.left);
		Log.d(TAG, "bounds - RIGHT (1): " + bounds.right);
		Log.d(TAG, "bounds - TOP (1): " + bounds.top);
		Log.d(TAG, "bounds - BOTTOM (1): " + bounds.bottom);
		
		float width = bounds.right - bounds.left;
		bounds.left = (float) (bounds.left - width*0.15);
		bounds.right = (float) (bounds.right + width*0.15);
		Log.d(TAG, "bounds - LEFT (2): " + bounds.left);
		Log.d(TAG, "bounds - RIGHT (2): " + bounds.right);
		if(bounds.left < 0){
			Log.d(TAG, "set bounds.left = 0  (1)");
			bounds.left = 0;
		}
		if(bounds.right > bitmap.getWidth()) {//--> größer als Bildschirm-Breite/Höhe
			Log.d(TAG, "set bounds.right = bitmap.getWidth()  (1)");
			bounds.right = bitmap.getWidth();
		}
		width = bounds.right - bounds.left;
		Log.d(TAG, "width (1): "+ width);
		
		
		//TODO: alle möglichen Fälle wie die Bounding Box auf dem Bild platziert sein kann abdecken
//		if(width < frameWidth){
//			Log.d(TAG, "enter if statement: if(width < frameWidth)");
//			bounds.left = (float) (bounds.left - ((frameWidth - width)*0.5));
//			bounds.right = (float) (bounds.right + ((frameWidth - width)*0.5));
//			Log.d(TAG, "bounds - LEFT (3): " + bounds.left);
//			Log.d(TAG, "bounds - RIGHT (3): " + bounds.right);
//			if(bounds.left < 0){
//				Log.d(TAG, "set bounds.left = 0  (2)");
//				bounds.left = 0;
//			}
//			if(bounds.right > bitmap.getWidth()) {//--> größer als Bildschirm-Breite/Höhe
//				Log.d(TAG, "set bounds.right = bitmap.getWidth()  (2)");
//				bounds.right = bitmap.getWidth();
//			}
//		}
//		width = bounds.right - bounds.left;
//		Log.d(TAG, "width (2): "+ width);
		
		float height = bounds.bottom - bounds.top;
		bounds.top = (float) (bounds.top - height*0.15);
		bounds.bottom = (float) (bounds.bottom + height *0.15);
		Log.d(TAG, "bounds - TOP (2): " + bounds.top);
		Log.d(TAG, "bounds - BOTTOM (2): " + bounds.bottom);
		if(bounds.top < 0){
			Log.d(TAG, "set bounds.top = 0  (1)");
			bounds.top = 0;
		}
			
		if(bounds.bottom > bitmap.getHeight()) {// ---> größer als Bildschirm-Breite/Höhe
			Log.d(TAG, "set bounds.bottom = bitmap.getHeight()  (1)");
			bounds.bottom = bitmap.getHeight();
		}
		height = bounds.bottom - bounds.top;
		Log.d(TAG, "height (1): "+ height);
		
		//TODO: alle möglichen Fälle wie die Bounding Box auf dem Bild platziert sein kann abdecken
//		if(height < frameHeight){
//			Log.d(TAG, "enter if statement: if(height < frameHeight)");
//			bounds.top = (float) (bounds.top - ((frameHeight - height)*0.5));
//			bounds.bottom = (float) (bounds.bottom + ((frameHeight - height)*0.5));
//			Log.d(TAG, "bounds - TOP (3): " + bounds.top);
//			Log.d(TAG, "bounds - BOTTOM (3): " + bounds.bottom);
//			if(bounds.top < 0){
//				Log.d(TAG, "set bounds.top = 0  (2)");
//				bounds.top = 0;
//			}
//			if(bounds.bottom > bitmap.getHeight()) {// ---> größer als Bildschirm-Breite/Höhe
//				Log.d(TAG, "set bounds.bottom = bitmap.getHeight()  (2)");
//				bounds.bottom = bitmap.getHeight();
//			}
//		}
//		height = bounds.bottom - bounds.top;
//		Log.d(TAG, "height (2): "+ height);
		
		//TODO: Seiterverhältnis von Höhe und Breite vergleichen und schauen, 
		//		ob das ganze in Hoch- oder Querformat ist.
		if(frameWidth < frameHeight){ // screen in portrait
			
			if(width < height){ // bounding box ist im "Hochformat" - Höhe übernehmen, Breite anpassen
				height = height;
//				bounds.left = bounds.left - (int)((height * 0.75 - width) / 2); - funkt nicht!
				width = (int)((height * 0.75));
			}
			
		} else { // screen in landscape
			
		}
		
		
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
