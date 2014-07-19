package domain;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class ObjectContour extends Scribble {

	private Path path;
	
	public ObjectContour(Path path, Paint paint){
		this.path = path;
		super.paint = paint;
	}

	public Path getPath() {
		return path;
	}

	@Override
	public void drawScribble(Canvas canvas) {
		canvas.drawPath(path, paint);
	}
	
}
