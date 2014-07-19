package domain;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class ForeBackGround extends Scribble {

	private Path pathFore;
	private Path pathBack;
	// TODO: own paint object, one for fore- the second for background?
	
	public ForeBackGround(Path fore, Path back, Paint paint){
		this.pathFore = fore;
		this.pathBack = back;
		this.paint = paint;
	}

	public Path getPathFore() {
		return pathFore;
	}

	public Path getPathBack() {
		return pathBack;
	}

	@Override
	public void drawScribble(Canvas canvas) {
		canvas.drawPath(pathFore, paint);
		canvas.drawPath(pathBack, paint);
	}
	
}
