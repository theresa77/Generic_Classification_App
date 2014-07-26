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
		
		Log.d(TAG, "Params-width: "+ params.width);
		Log.d(TAG, "Params-height: "+ params.height);
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
		
		
		//TODO: Seiterverhältnis von Höhe und Breite vergleichen und schauen, 
		//		ob das ganze in Hoch- oder Querformat ist.
		// TODO: and frame-grenzen anpassen - wenn width oder height über diese grenzen gehen
		//		--> offenbar wird das scribble verändert, wenn man dann wieder auf return geht- 
		//			übernimmt irgendwie die das bounds RectF als Scribble
		//
		// bei Bild im Hochformat und Bounding Box im Querformat wird oben und unten ein zu dicker schwarzer streifen angezeigt
		// das gleiche ist auch bei Bild im Querformat, die Bounding Box aber im Hochformat - links und rechts dicke schwarze streifen
		// die Frage ist hier: will ich das so haben, oder soll stattdessen so viel wie möglich vom restlichen Bild gezeigt werden?
			
			if(width < height){ // bounding box ist im "Hochformat" - Höhe übernehmen, Breite anpassen
				Log.d(TAG, "Bounding Box in PORTRAIT");
				width = (int)(height * 0.75);
				bounds.left = bounds.left - Math.abs(width - (bounds.right - bounds.left))/2;
				if((bounds.left + width) > bitmap.getWidth()){
					Log.d(TAG, "IF statement entered: if((bounds.left + width) > bitmap.getWidth())");
					bounds.left = bounds.left - ((bounds.left + width) - bitmap.getWidth());
				}
				
				// Versuch bei Querformat-bild und Hochformat-Scribble-Bounding-Box die ganze mögliche Bildbreite zu nutzen.
				// ist nicht geglückt
//				if((bounds.left + width) < bitmap.getWidth()){
//					Log.d(TAG, "IF statement entered: if((bounds.left + width) < bitmap.getWidth())");
//					bounds.left = bounds.left - (bitmap.getWidth() - width);
//					width = width + (bitmap.getWidth() - width);
//				}
				if(bounds.left < 0){
					Log.d(TAG, "IF statement entered: if(bounds.left < 0)");
					Log.d(TAG, "set bounds.left = 0)");
					width = width + bounds.left;
					bounds.left = 0;
				}
			} else { // bounding box ist im "Querformat" - Breite übernemen, Höhe anpassen
				Log.d(TAG, "Bounding Box in LANDSCAPE");
				height = (int)(width * 0.75);
				bounds.top = bounds.top - Math.abs(height - (bounds.bottom - bounds.top))/2;
				if((bounds.top + height) > bitmap.getHeight()){
					bounds.top = bounds.top - ((bounds.top + height) - bitmap.getHeight());
				}
				if(bounds.top < 0){
					Log.d(TAG, "set bounds.top = 0");
					height = height + bounds.top;
					bounds.top = 0;
				}
			}
			
		
		Log.d(TAG, "Bitmap-HEIGHT: "+bitmap.getHeight());
		Log.d(TAG, "Bitmap-WIDTH: "+bitmap.getWidth());
		Log.d(TAG, "Bounds-LEFT: "+bounds.left);
		Log.d(TAG, "Bounds-TOP: "+bounds.top);
		Log.d(TAG, "WIDTH: "+width);
		Log.d(TAG, "HEIGHT: "+height);
		
		return Bitmap.createBitmap(bitmap, (int)bounds.left, (int)bounds.top, (int)width, (int)height);
	}

	private RectF calculateBoundingBoxForScribbles(){
		RectF bounds = null;
		RectF currScri = new RectF();
		
		for(Scribble scri : scribbles){
			if(bounds != null){
				currScri = scri.getBoundingBoxOfScribble();
				
				bounds = new RectF(Math.min(currScri.left, bounds.left), 
								   Math.min(currScri.top, bounds.top),
								   Math.max(currScri.right, bounds.right),
								   Math.max(currScri.bottom, bounds.bottom));
			} else {
				bounds = new RectF(scri.getBoundingBoxOfScribble());
			}
		}
		
		return bounds;
	}
	

}
