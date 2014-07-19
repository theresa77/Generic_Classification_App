package domain;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class MinBoundingBox extends Scribble {

	private RectF rectf;
	private Shape shape;
	
	public enum Shape {
		RECTANGLE, OVAL
	}
	
	public MinBoundingBox(RectF rectf, Shape shape, Paint paint){
		this.rectf = rectf;
		this.shape = shape;
		super.paint = paint;
	}

	public RectF getRectf() {
		return rectf;
	}

	public Shape getShape() {
		return shape;
	}

	@Override
	public void drawScribble(Canvas canvas) {
		canvas.drawRect(rectf, paint);
	}
	
	
}
