package view;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.View;
import domain.Picture;
import domain.Scribble;

public class ViewObjectView extends View {
	
	private Bitmap mPictureBitmap;
	private List<Scribble> scribbles;
	private RectF bounds;
	
	public ViewObjectView(Context context){
		super(context);
	}

	public ViewObjectView(Context context, Bitmap bitmap) {
		super(context);
		Picture picture = Picture.getInstance();
		scribbles = picture.getScribbles();
		mPictureBitmap = createScaledBitmap(drawScribblesToBitmap(picture.getBitmap()));
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawBitmap(mPictureBitmap, 0, 0, null);
		
//		if (scribbles != null && !scribbles.isEmpty()) {
//			for (Scribble s : scribbles) {
//				if(s != null)
//					s.drawScribble(canvas);
//			}
//		}
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
		bounds = calculateBoundingBoxForScribbles();
		float width = bounds.right - bounds.left;
		bounds.left = (float) (bounds.left - width*0.15);
		bounds.right = (float) (bounds.right + width*0.15);
		if(bounds.left < 0)
			bounds.left = 0;
//		if(bounds.right) --> größer als Bildschirm-Breite/Höhe
		width = bounds.right - bounds.left;
		
		float height = bounds.bottom - bounds.top;
		bounds.top = (float) (bounds.top - height*0.15);
		bounds.bottom = (float) (bounds.bottom + height *0.15);
		if(bounds.top < 0)
			bounds.top = 0;
//		if(bounds.bottom < ...)  ---> größer als Bildschirm-Breite/Höhe
		height = bounds.bottom - bounds.top;
		
		return Bitmap.createBitmap(bitmap, (int)bounds.left, (int)bounds.top, (int)width, (int)height);
	}

	public void setBitmap(Bitmap bitmap) {
		mPictureBitmap = bitmap;
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
