/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package activity;

import view.ForegroundBackgroundView;
import view.ForegroundBackgroundView.Selection;
import view.MinimumBoundingBoxView;
import view.MinimumBoundingBoxView.Shape;
import view.ObjectContourView;
import view.UserScribbleView;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TabHost.OnTabChangeListener;

import com.genericclassificationapp.R;

import dialog.ColorPickerDialog;
import dialog.OptionMenuDialog;
import dialog.PickForegroundBackgroundDialog;
import dialog.PickShapeDialog;
import dialog.PickStrokeWidthDialog;
import domain.Picture;

/**
 * Activity for drawing user scribbles.
 * Contains fragment-tabs for the different view for user input.
 * 
 * @author Theresa Froeschl
 * @version 1.0
 *
 */
public class UserScribbleMainActivity extends FragmentActivity  {

	private static final String TAG = UserScribbleMainActivity.class.getSimpleName();
	private FragmentTabHost mTabHost;
	private UserScribbleView mView;
	private Picture mPicture;
	private Paint mPaint;
	private int displayWidth;
	private int displayHeight;
	private DialogFragment currentDialog;
	private float marginTopLeft;
	private float xStart;
	private float yStart;
	private RelativeLayout tab1;
	private RelativeLayout tab2;
	private RelativeLayout tab3;
	private Scribble currentScribble;
	
	public enum Scribble {
		MINIMUM_BOUNDING_BOX, OBJECT_CONTOUR, FOREGROUND, BACKGROUND
	}
	
	/**
	 * Called when the activity is created.
	 * Sets local variables.
	 */
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get Picture instance
		mPicture = Picture.getInstance();
        
		// get height and width of display
        WindowManager winMan = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = winMan.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		displayHeight = metrics.heightPixels;
		displayWidth = metrics.widthPixels;
		
		// Layout params for icons on tabs
		RelativeLayout.LayoutParams paramsImg = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsImg.addRule(RelativeLayout.CENTER_IN_PARENT);
		RelativeLayout.LayoutParams paramsTab;
		
		// set content view corresponding to picture format
		if (mPicture.isLandscape()) {
			setContentView(R.layout.user_scribble_tab_layout_landscape);
			paramsTab = new RelativeLayout.LayoutParams(
					(int) (displayWidth * 0.1), ((int) (displayHeight / 3)));

		} else {
			setContentView(R.layout.user_scribble_tab_layout_portrait);
			paramsTab = new RelativeLayout.LayoutParams(
					(int) (displayWidth / 3), (int) (displayHeight * 0.1));
		}

		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		
		// create 3 tabs for the 3 different user scribbles
		// first tab is for Minimum Bounding Box View
		tab1 = new RelativeLayout(this);
		tab1.setLayoutParams(paramsTab);
		
		// ImageView for showing icons on tabs
		ImageView img1 = new ImageView(this);
		img1.setBackgroundResource(R.drawable.mbr_icon);
		img1.setLayoutParams(paramsImg);
		tab1.addView(img1);

		// second tab is for Foreground/Background View
		tab2 = new RelativeLayout(this);
		tab2.setLayoutParams(paramsTab);
		ImageView img2 = new ImageView(this);
		img2.setBackgroundResource(R.drawable.f_icon);
		img2.setLayoutParams(paramsImg);
		tab2.addView(img2);		

		// third tab is for Object Contour View
		tab3 = new RelativeLayout(this);
		tab3.setLayoutParams(paramsTab);
		ImageView img3 = new ImageView(this);
		img3.setBackgroundResource(R.drawable.oc_icon);
		img3.setLayoutParams(paramsImg);
		tab3.addView(img3);
		

		// add tabs to tab host
		mTabHost.addTab(mTabHost.newTabSpec("mbr").setIndicator(tab1),
				MinimumBoundingBoxFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("fb").setIndicator(tab2),
				ForegroundBackgroundFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("oc").setIndicator(tab3),
				ObjectContourFragment.class, null);

		// get current tab from extras of the intent
		mTabHost.setCurrentTab(getIntent().getIntExtra("tab", 0));
        setTabsColor();
        
        // set tab change listener for changing the color of the selected tab
        mTabHost.setOnTabChangedListener(new OnTabChangeListener(){
        	@Override
        	public void onTabChanged(String tabId) {
        		setTabsColor();
        	}});
        
        // create new paint object for scribbles
        mPaint = new Paint();
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(Color.BLUE);
		mPaint.setStrokeWidth(10);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
	}
	
	/**
	 * Called when user touches the screen
	 * Invoke drawing of scribbles.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int currentTab = mTabHost.getCurrentTab();
		int action = MotionEventCompat.getActionMasked(event);
		float x = 0;
		float y = 0;

		// get position of touch on the picture
		if (displayWidth > displayHeight) { //display in landscape
			x = event.getX() - marginTopLeft;
			y = event.getY();
		} else {							//display in portrait
			x = event.getX();
			y = event.getY() - marginTopLeft;
		}
		
		// for drawing the scribbles a touch is only relevant when it is on the picture
		if (x >= 0 && x <= mView.getBitmap().getWidth() && y >= 0
				&& y <= mView.getBitmap().getHeight()) {
			
			switch (action) {

			// user start touch
			// drawing of scribbles starts
			case (MotionEvent.ACTION_DOWN):
				 Log.d(TAG,"Action was DOWN");
				if (currentTab == 0) {
					xStart = x;
					yStart = y;
					
				} else if (currentTab == 1) {
					((ForegroundBackgroundView)mView).startMove(x, y);
				} else {
					((ObjectContourView)mView).startMove(x, y);
				}

			// user moves finger on screen
			case (MotionEvent.ACTION_MOVE):
				 Log.d(TAG, "Action was MOVE");
				if (currentTab == 0) {
					if (xStart != 0 || yStart != 0) {
						((MinimumBoundingBoxView)mView).setShape(Math.min(xStart, x), Math.min(yStart, y),
																 Math.max(xStart, x), Math.max(yStart, y));
					} else {
						xStart = x;
						yStart = y;
					}
				} else if (currentTab == 1) {
					((ForegroundBackgroundView)mView).move(x, y);
				} else {
					((ObjectContourView)mView).move(x, y);	
				}
				return true;
				
			//user stop touching the screen
			case (MotionEvent.ACTION_UP):
				// Log.d(TAG,"Action was UP");
				if (currentTab == 0) {
					((MinimumBoundingBoxView)mView).setShape(Math.min(xStart, x), Math.min(yStart, y),
															 Math.max(xStart, x), Math.max(yStart, y));
					xStart = 0;
					yStart = 0;		
				} else if (currentTab == 1) {
					((ForegroundBackgroundView)mView).stopMove(x, y);			
				} else {
					((ObjectContourView)mView).stopMove(x, y);
				}
				return true;

			case (MotionEvent.ACTION_CANCEL):
				// Log.d(TAG,"Action was CANCEL");
				return true;

			case (MotionEvent.ACTION_OUTSIDE):
				// Log.d(TAG,"Movement occurred outside bounds of current screen element");
				return true;

			default:
				return super.onTouchEvent(event);
			}
			
		} else { // if the touch is outside of the picture
				 //reset drawing
			if (currentTab == 0) {
				if (action == MotionEvent.ACTION_UP) {
					xStart = 0;
					yStart = 0;
				}
			} else if (currentTab == 1) {				
				if(action == MotionEvent.ACTION_DOWN){
					((ForegroundBackgroundView)mView).resetPath();
				}				
			} else {				
				if(action == MotionEvent.ACTION_DOWN){
					((ObjectContourView)mView).resetPath();
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Called when clicked on change color button.
	 * Opens Dialog to pick a color.
	 * @param v clicked button
	 */
	public void editColor(View v){
		currentDialog = new ColorPickerDialog();
    	currentDialog.show(getSupportFragmentManager(), "editUserScribble");
		Log.d(TAG, "show Dialog for editing color for user scribbles");
	}
	
	/**
	 * Called when clicked on change stroke button.
	 * Opens Dialog to set stroke width.
	 * @param v clicked button
	 */
	public void editLine(View v){
		currentDialog = new PickStrokeWidthDialog();
    	currentDialog.show(getSupportFragmentManager(), "EditLineDialog");
		Log.d(TAG, "show Dialog for editing color for user scribbles");
	}
	
	/**
	 * Called when clicked on change shape button at MinimumBoundingBoxView.
	 * Opens a Dialog to choose between rectangle and oval shape.
	 * @param v clicked button
	 */
	public void editForm(View v){
		currentDialog = new PickShapeDialog();
    	currentDialog.show(getSupportFragmentManager(), "EditFormDialog");
		Log.d(TAG, "show Dialog for editing form for user scribbles");
	}
	
	/**
	 * Called when clicked on menu button.
	 * Opens a Dialog with the menu.
	 * @param v clicked button
	 */
	public void openMenu(View v){
		currentDialog = new OptionMenuDialog();
		currentDialog.show(getSupportFragmentManager(), "OptionMenuDialog");
	}
	
	/**
	 * Called when clicked on ForegroundBackground button in ForegroundBackgroundView
	 * Opens a Dialog to choose between selecting the foreground or the background of the picture.
	 * @param v clicked button
	 */
	public void pickForegroundBackground(View v){
		currentDialog = new PickForegroundBackgroundDialog();
		currentDialog.show(getSupportFragmentManager(), "SelectForgroundBackgroundDialog");
		Log.d(TAG, "show Dialog for selecting foreground or background");
	}
	
	/**
	 * Sets color for drawing scribbles.
	 * @param color selected color
	 */
	public void setUserScribbleColor(int color) {
		mPaint.setColor(color);
		mView.setUserScribbleColor(color);
		if(currentDialog!=null)
			currentDialog.dismiss();
	}
	
	/**
	 * Gets current color for drawing scribbles
	 * @return color for scribbles
	 */
	public int getUserScribbleColor() {
		return mPaint.getColor();
	}

	/**
	 * Sets the stroke width for drawing scribbles
	 * @param width for scribbles
	 */
	public void setStrokeWidth(int width){
		mPaint.setStrokeWidth(width);
		mView.setStrokeWidth(width);
		if(currentDialog!=null)
			currentDialog.dismiss();
	}
	
	/**
	 * Gets current stroke width for scribbles
	 * @return stroke width for scribbles
	 */
	public float getStrokeWidth(){
		return mPaint.getStrokeWidth();
	}
	
	/**
	 * Gets Paint object for user scribbles,
	 * @return paint object for scribbles
	 */
	public Paint getPaint(){
		return mPaint;
	}

	/**
	 * Gets width of the display
	 * @return width of display
	 */
	public int getDisplayWidth(){
		return displayWidth;
	}
	
	/**
	 * Gets height of the display
	 * @return height of display
	 */
	public int getDisplayHeight(){
		return displayHeight;
	}
	
	/**
	 * Gets margin value for the picture to the top of the display when it is in portrait
	 * and to the left of the display when it is on landscape
	 * @param marginTopLeft margin to top/left to the display
	 */
	public void setMarginTopLeft(float marginTopLeft){
		this.marginTopLeft = marginTopLeft;
	}

	/**
	 * Sets the current view for the selected tab
	 * @param mView current view for the activity
	 */
	public void setCurrentView(UserScribbleView mView) {
		this.mView = mView;
	}

	/**
	 * Sets the value for the selected shape for MinimumBoundingBoxView
	 * @param shape selected shape
	 */
	public void setShape(Shape shape) {
		((MinimumBoundingBoxView)mView).setCurrentShape(shape);
	}
	
	/**
	 * Sets the value for the selection of fore- or background for ForgroundBackgroundView
	 * @param selection value for selection
	 */
	public void setForeBackgroundSelection(Selection selection){
		((ForegroundBackgroundView)mView).setCurrentSelection(selection);
		ImageButton imgButton = (ImageButton) findViewById(R.id.select_fore_or_background_button);
		
		// adapt icon for button to new selection value
		if(selection == Selection.FOREGROUND) {
			imgButton.setImageResource(R.drawable.f_icon);
			currentScribble = Scribble.FOREGROUND;
		} else {
			imgButton.setImageResource(R.drawable.b_icon);
			currentScribble = Scribble.BACKGROUND;
		}
	}
	
	/**
	 * Resets the drawing.
	 * Deletes user scribbles.
	 */
	public void resetDrawing(){
		mView.resetDrawing();
	}

	/**
	 * Called to set the background color for the tabs
	 * The currently selected tab gets a lighter gray as the other two.
	 */
	private void setTabsColor(){
		int currentTab = mTabHost.getCurrentTab();
        if(currentTab == 0){
        	tab1.setBackgroundColor(this.getResources().getColor(R.color.darkGray));
        	tab2.setBackgroundColor(this.getResources().getColor(R.color.buttonGray));
        	tab3.setBackgroundColor(this.getResources().getColor(R.color.buttonGray));
        } else if(currentTab == 1){
        	tab1.setBackgroundColor(this.getResources().getColor(R.color.buttonGray));
        	tab2.setBackgroundColor(this.getResources().getColor(R.color.darkGray));
        	tab3.setBackgroundColor(this.getResources().getColor(R.color.buttonGray));
        } else {
        	tab1.setBackgroundColor(this.getResources().getColor(R.color.buttonGray));
        	tab2.setBackgroundColor(this.getResources().getColor(R.color.buttonGray));
        	tab3.setBackgroundColor(this.getResources().getColor(R.color.darkGray));
        }
	}
	
	/**
	 * Get current view from the selected tab.
	 * @return view for the activity
	 */
	public UserScribbleView getView(){
		return mView;
	}

	/**
	 * Get current selection for drawing scribbles.
	 * @return current scribble selection
	 */
	public Scribble getCurrentScribble() {
		return currentScribble;
	}

	/**
	 * Set value for scribble selection.
	 * @param currentScribble new scribble selection
	 */
	public void setCurrentScribble(Scribble currentScribble) {
		this.currentScribble = currentScribble;
	}
	
}
