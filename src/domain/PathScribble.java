package domain;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

/**
 * Abstract class for a scribble that only consists of a simple path object. 
 * 
 * @author Theresa Froeschl
 * @version 1.0
 *
 */
public abstract class PathScribble implements Scribble {

	private Path path;
	private Paint paint;
	private float[] points;
	
	public PathScribble(Path path, Paint paint){
		this.path = path;
		this.paint = paint;
	}
	
//	public PathScribble(float[] points, Paint paint){
//		this.paint = paint;
//		this.points = points;
//	}

	@Override
	public void drawScribble(Canvas canvas) {
		if(points != null && points.length>0)
			canvas.drawLines(points, paint);
		else
			canvas.drawPath(path, paint);
	}
	
	@Override
	public RectF getBoundingBoxOfScribble(){
		RectF bounds = new RectF();
		path.computeBounds(bounds, true);
		return bounds;
	}
}
