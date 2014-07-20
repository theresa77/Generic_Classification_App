package view;

import java.util.List;

import domain.Picture;
import domain.Scribble;
import activity.ViewObjectActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

public class ViewObjectView extends View {
	
	private ViewObjectActivity mActivity;
	private Bitmap mPictureBitmap;
	private List<Scribble> scribbles;

	public ViewObjectView(Context context) {
		super(context);
		mActivity = (ViewObjectActivity)context;
		Picture picture = Picture.getInstance();
		scribbles = picture.getScribbles();
		mPictureBitmap = picture.getBitmap();
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

}
