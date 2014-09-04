/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package domain;

import java.util.ArrayList;
import java.util.List;

import com.genericclassificationapp.R;

import android.graphics.Bitmap;
import android.widget.Toast;

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
	 * Recycle bitmap for taken picture.
	 */
	public void recyclePicture(){
		mBitmap.recycle();
		mBitmap = null;
		mPicture = null;
	}

	/**
	 * Get list of all drawn scribbles.
	 * @return list of scribbles
	 */
	public List<Scribble> getScribbles() {
		return scribbles;
	}

	/**
	 * Set list for all scribbles.
	 * @param scribbles new list of scribbles
	 */
	public void setScribbles(List<Scribble> scribbles) {
		this.scribbles = scribbles;
	}
	
	/**
	 * Add new scribble to list.
	 * @param scri new scribble
	 */
	public void addScribbleToList(Scribble scri){
		if(scribbles == null)
			scribbles = new ArrayList<Scribble>();
		if(scri != null 
			&& (scribbles.isEmpty() 
				|| 	(!scribbles.isEmpty() && !scri.equals(scribbles.get(scribbles.size()-1)))) ){
			scribbles.add(scri);
		}
	}
	
	/**
	 * TODO
	 */
	public void removeLastScribble(){
		if(!scribbles.isEmpty()){
			scribbles.remove(scribbles.size()-1);
		}
	}
	
	public void removeLastForeBackScribble(){
		if(!scribbles.isEmpty()){
			for(int i=scribbles.size()-1; i>=0; i--){
				if(scribbles.get(i) instanceof ForeBackGround){
					scribbles.remove(i);
					break;
				}
			}
		}
	}
	
	
}
