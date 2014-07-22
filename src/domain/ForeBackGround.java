package domain;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class ForeBackGround extends Scribble {

	private Path pathFore, pathBack;
	// TODO: own paint object, one for fore- the second for background?
	private int foreColor, backColor;
	
	public ForeBackGround(Path fore, Path back, Paint paint, int foreColor, int backColor){
		super();
		this.pathFore = fore;
		this.pathBack = back;
		this.paint = paint;
		this.foreColor = foreColor;
		this.backColor = backColor;
	}

	public Path getPathFore() {
		return pathFore;
	}

	public Path getPathBack() {
		return pathBack;
	}

	@Override
	public void drawScribble(Canvas canvas) {
		paint.setColor(foreColor);
		canvas.drawPath(pathFore, paint);
		paint.setColor(backColor);
		canvas.drawPath(pathBack, paint);
	}
	
	public RectF getBoundingBoxOfScribble(){
		// left, top, right, bottom
		RectF bounds = new RectF();
		RectF boundsFore = new RectF();
		RectF boundsBack = new RectF();
		
		pathFore.computeBounds(boundsFore, true);
		pathBack.computeBounds(boundsBack, true);
		
		bounds.left = Math.min(boundsFore.left, boundsBack.left);
		bounds.top = Math.min(boundsFore.top, boundsBack.top);
		bounds.right = Math.min(boundsFore.right, boundsBack.right);
		bounds.bottom = Math.min(boundsFore.bottom, boundsBack.bottom);
		
		return bounds;
	}
	
}
