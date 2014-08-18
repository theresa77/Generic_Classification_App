/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package domain;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class ObjectContour extends Scribble {

	private Path path;
	private Paint paint;
	
	public ObjectContour(Path path, Paint paint){
		super();
		this.path = path;
		this.paint = paint;
	}

	public Path getPath() {
		return path;
	}

	@Override
	public void drawScribble(Canvas canvas) {
		canvas.drawPath(path, paint);
	}
	
	public RectF getBoundingBoxOfScribble(){
		// left, top, right, bottom
		RectF bounds = new RectF();
		path.computeBounds(bounds, true);
		return bounds;
	}
	
}
