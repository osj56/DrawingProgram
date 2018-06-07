package shapes;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

public class GESelect extends GEShape {
	
	public GESelect() {
		super(new Rectangle());
	}

	@Override
	public void initDraw(Point startP) {
		this.startP = startP;
	}

	@Override
	public void setCoordinate(Point currentP) {
		Rectangle tempRectangle = (Rectangle) myShape;
		tempRectangle.setFrameFromDiagonal(startP.x, startP.y, currentP.x, currentP.y);
	}

	@Override
	public GEShape clone() {
		return new GESelect();
	}
	
	@Override
	public GEShape deepCopy() {
		AffineTransform affineTransform = new AffineTransform();
		Shape newShape = affineTransform.createTransformedShape(myShape);
		GERectangle shape = new GERectangle();
		
		shape.setShape(newShape);
		shape.setGraphicsAttributes(this);
		
		return shape;
	}
}
