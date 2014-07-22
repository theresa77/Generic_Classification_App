package domain;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public abstract class Scribble {

	protected ScribbleType type;
	protected Paint paint;
	
	//TODO: delete 
	// replace with classes
	public enum ScribbleType {
		MINIMUM_BOUNDING_BOX, OBJECT_CONTOUR, FOREGROUND, BACKGROUND
	}
	
	public Paint getPaint() {
		return paint;
	}
	
	public abstract void drawScribble(Canvas canvas);
	
	// TODO: input parameters???
	public abstract RectF getBoundingBoxOfScribble();	
	
}
