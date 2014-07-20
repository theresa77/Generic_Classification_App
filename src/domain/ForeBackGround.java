package domain;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class ForeBackGround extends Scribble {

	private Path pathFore, pathBack;
	// TODO: own paint object, one for fore- the second for background?
	private int foreColor, backColor;
	
	public ForeBackGround(Path fore, Path back, Paint paint, int foreColor, int backColor){
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
	
}
