/**
 * Copyright (C) 2014 Theresa Froeschl
 */
package domain;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

/**
 * Class for one Foreground-Background drawing.
 * 
 * @author Theresa Froeschl
 *
 */
public class ForeBackGround implements Scribble {
	
	// code for Tuple-class
	//TODO: if not needed --> delete
//	package domain;
//
//	public class Tuple<X,Y>{
//		
//		public X x;
//		public Y y;
//		
//		public Tuple(X x, Y y){
//			this.x = x;
//			this.y = y;
//		}
//	}

//	private List<Tuple<Path,Paint>> foreTupleList, backTupleList;
//	private List<Path> forePathList, backPathList;
//	private Paint forePaint, backPaint;
	private Path path;
	private Paint paint;
	
//	public ForeBackGround(List<Path> forePathList, List<Path> backPathList, Paint forePaint, Paint backPaint){
//		super();
//		this.forePathList = forePathList;
//		this.backPathList = backPathList;
//		this.forePaint = forePaint;
//		this.backPaint = backPaint;
//	}
//	
//	public ForeBackGround(List<Tuple<Path,Paint>> foreTupleList, List<Tuple<Path,Paint>> backTupleList){
//		super();
//		this.foreTupleList = foreTupleList;
//		this.backTupleList = backTupleList;
//	}
	
	public ForeBackGround(Path path, Paint paint){
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

//	/**
//	 * Get list of all drawn paths which were marked by the user as Foreground.
//	 * @return
//	 */
//	public List<Path> getPathsForeground() {
//		return forePathList;
//	}
//
//	/**
//	 * Get list of all drawn paths which were marked by the user as Background.
//	 * @return
//	 */
//	public List<Path> getPathsBackground() {
//		return backPathList;
//	}
//
//	/**
//	 * Draw all paths of Foreground and Background to a canvas.
//	 */
//	@Override
//	public void drawScribble(Canvas canvas) {
//
//		for(Path p : forePathList) {
//			canvas.drawPath(p, forePaint);
//		}
//
//		for (Path p : backPathList) {
//			canvas.drawPath(p, backPaint);
//		}
//		
//		
////		// code for Tuples
////		for(Tuple<Path,Paint> t : foreTupleList) {
////			canvas.drawPath(t.x, t.y);
////		}
////
////		for (Tuple<Path,Paint> t : backTupleList) {
////			canvas.drawPath(t.x, t.y);
////		}
//
//	}
//	
//	@Override
//	public RectF getBoundingBoxOfScribble(){
//		RectF boundsFore = new RectF();
//		RectF boundsBack = new RectF();
//		RectF bounds = new RectF();
//		
////		forePath.computeBounds(boundsFore, true);
////		backPath.computeBounds(boundsBack, true);
//		
//		for(Path p : forePathList) {
//			p.computeBounds(boundsFore, true);
//			
//			if(bounds != null && !bounds.isEmpty()) {
//				bounds = new RectF(Math.min(boundsFore.left, bounds.left),
//						 		   Math.min(boundsFore.top, bounds.top),
//						 		   Math.max(boundsFore.right, bounds.right),
//						 		   Math.max(boundsFore.bottom, bounds.bottom));
//			} else {
//				bounds = new RectF(boundsFore);
//			}
//		}
//
//		for (Path p : backPathList) {
//			p.computeBounds(boundsBack, true);
//			
//			if(bounds != null && !bounds.isEmpty()) {
//				bounds = new RectF(Math.min(boundsBack.left, bounds.left),
//						 		   Math.min(boundsBack.top, bounds.top),
//						 		   Math.max(boundsBack.right, bounds.right),
//						 		   Math.max(boundsBack.bottom, bounds.bottom));
//			} else {
//				bounds = new RectF(boundsBack);
//			}
//		}
//		
////		bounds.left = Math.min(boundsFore.left, boundsBack.left);
////		bounds.top = Math.min(boundsFore.top, boundsBack.top);
////		bounds.right = Math.min(boundsFore.right, boundsBack.right);
////		bounds.bottom = Math.min(boundsFore.bottom, boundsBack.bottom);
//
////		RectF bounds = new RectF(Math.min(boundsFore.left, boundsBack.left),
////								 Math.min(boundsFore.top, boundsBack.top),
////								 Math.max(boundsFore.right, boundsBack.right),
////								 Math.max(boundsFore.bottom, boundsBack.bottom));
//		
//		return bounds;
//	}
	
}
