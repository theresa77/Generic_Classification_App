package view;

import java.util.List;

import domain.Picture;
import domain.Scribble;
import activity.ViewObjectActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.View;

public class ViewObjectView extends View {
	
	private ViewObjectActivity mActivity;
	private Bitmap mPictureBitmap;
	private List<Scribble> scribbles;
	private RectF bounds;

	public ViewObjectView(Context context) {
		super(context);
		mActivity = (ViewObjectActivity)context;
		Picture picture = Picture.getInstance();
		scribbles = picture.getScribbles();
		mPictureBitmap = picture.getBitmap();
		bounds = calculateBoundingBoxForScribbles();
		
		//TODO: mit folgendem Befehl Bitmap auf größe des bounding box + 15% verändern
	//	Bitmap.createScaledBitmap(mPicture.getBitmap(), params.width, params.height, false)
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawBitmap(mPictureBitmap, 0, 0, null);
		
		if (scribbles != null && !scribbles.isEmpty()) {
			for (Scribble s : scribbles) {
				if(s != null)
					s.drawScribble(canvas);
			}
		}
	}

	public void setBitmap(Bitmap bitmap) {
		mPictureBitmap = bitmap;
	}
	
	public RectF calculateBoundingBoxForScribbles(){
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
