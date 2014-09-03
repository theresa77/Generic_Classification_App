/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package domain;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

/**
 * Class for one Object Contour drawing.
 * 
 * @author Theresa Froeschl
 *
 */
public class ObjectContour implements Scribble {

	private Path path;
	private Paint paint;
	
	public ObjectContour(Path path, Paint paint){
		super();
		this.path = path;
		this.paint = paint;
	}

	@Override
	public void drawScribble(Canvas canvas) {
		canvas.drawPath(path, paint);
	}
	
	@Override
	public RectF getBoundingBoxOfScribble(){
		RectF bounds = new RectF();
		path.computeBounds(bounds, true);
		return bounds;
	}
	
}