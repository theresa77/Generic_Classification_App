package domain;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class ForeBackGround extends Scribble {

	private List<Path> forePathList, backPathList;
//	private int foreColor, backColor;
	private Paint forePaint, backPaint;
	
	public ForeBackGround(List<Path> forePathList, List<Path> backPathList, Paint forePaint, Paint backPaint){
		super();
		this.forePathList = forePathList;
		this.backPathList = backPathList;
		this.forePaint = forePaint;
		this.backPaint = backPaint;
//		this.foreColor = foreColor;
//		this.backColor = backColor;
	}

	public List<Path> getPathFore() {
		return forePathList;
	}

	public List<Path> getPathBack() {
		return backPathList;
	}

	@Override
	public void drawScribble(Canvas canvas) {
//		paint.setColor(foreColor);
//		canvas.drawPath(pathFore, paint);
//		paint.setColor(backColor);
//		canvas.drawPath(pathBack, paint);
		
		for(Path p : forePathList) {
			canvas.drawPath(p, forePaint);
		}

		for (Path p : backPathList) {
			canvas.drawPath(p, backPaint);
		}

//		canvas.drawPath(forePath, forePaint);
//		canvas.drawPath(backPath, backPaint);
	}
	
	public RectF getBoundingBoxOfScribble(){
		RectF boundsFore = new RectF();
		RectF boundsBack = new RectF();
		RectF bounds = new RectF();
		
//		forePath.computeBounds(boundsFore, true);
//		backPath.computeBounds(boundsBack, true);
		
		for(Path p : forePathList) {
			p.computeBounds(boundsFore, true);
			
			if(bounds != null && !bounds.isEmpty()) {
				bounds = new RectF(Math.min(boundsFore.left, bounds.left),
						 		   Math.min(boundsFore.top, bounds.top),
						 		   Math.max(boundsFore.right, bounds.right),
						 		   Math.max(boundsFore.bottom, bounds.bottom));
			} else {
				bounds = new RectF(boundsFore);
			}
		}

		for (Path p : backPathList) {
			p.computeBounds(boundsBack, true);
			
			if(bounds != null && !bounds.isEmpty()) {
				bounds = new RectF(Math.min(boundsBack.left, bounds.left),
						 		   Math.min(boundsBack.top, bounds.top),
						 		   Math.max(boundsBack.right, bounds.right),
						 		   Math.max(boundsBack.bottom, bounds.bottom));
			} else {
				bounds = new RectF(boundsBack);
			}
		}
		
//		bounds.left = Math.min(boundsFore.left, boundsBack.left);
//		bounds.top = Math.min(boundsFore.top, boundsBack.top);
//		bounds.right = Math.min(boundsFore.right, boundsBack.right);
//		bounds.bottom = Math.min(boundsFore.bottom, boundsBack.bottom);

//		RectF bounds = new RectF(Math.min(boundsFore.left, boundsBack.left),
//								 Math.min(boundsFore.top, boundsBack.top),
//								 Math.max(boundsFore.right, boundsBack.right),
//								 Math.max(boundsFore.bottom, boundsBack.bottom));
		
		return bounds;
	}
	
}
