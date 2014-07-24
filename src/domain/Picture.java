/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package domain;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

/**
 * Represents the taken picture
 * Comprises a Bitmap of the picture and a boolean flag which is true when 
 * the picture is in landscape and false when the picture is in portrait
 * 
 * @author Theresa Froeschl
 * @version 1.0
 *
 */
public class Picture {

	private static Picture mPicture;
	private boolean mIsLandscape;
	private Bitmap mBitmap;
	private List<Scribble> scribbles;
	
	public Picture(Bitmap mBitmap, boolean mIsLandscape){
		this.mIsLandscape = mIsLandscape;
		this.mBitmap = mBitmap;
	}
	
	/**
	 * Creates a new static Picture instance.
	 * @param mBitmap Bitmap object for the picture
	 * @param mLandscapeBool true if picture is in landscape, false if it is in portrait
	 */
	public static void createInstance(Bitmap mBitmap, boolean mLandscapeBool){
		mPicture = new Picture(mBitmap, mLandscapeBool);
	}
	
	/**
	 * Return the instance of the Picture
	 * @return Picture instance
	 */
	public static Picture getInstance() {
        return mPicture;
    }

	
	/**
	 * Returns true if the picture is in landscape
	 * and false when the picture is in portrait
	 * 
	 * @return value of boolean flag for landscape
	 */
	public boolean isLandscape() {
		return mIsLandscape;
	}
	
	/**
	 * Returns a Bitmap instance of the picture
	 * @return bitmap of the picture
	 */
	public Bitmap getBitmap() {
		return mBitmap;
	}
	
	/**
	 * TODO
	 */
	public void recyclePicture(){
		mBitmap.recycle();
		mBitmap = null;
		mPicture = null;
	}

	
	public List<Scribble> getScribbles() {
		return scribbles;
	}

	public void setScribbles(List<Scribble> scribbles) {
		this.scribbles = scribbles;
	}
	
	public void addScribbleToList(Scribble scri){
		if(scribbles == null)
			scribbles = new ArrayList<Scribble>();
		scribbles.add(scri);
	}
	
	
	
}
