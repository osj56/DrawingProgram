package transformer;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import constants.GEConstants;
import shapes.GEShape;

public class GEGrouper extends GETransformer{

	public GEGrouper(GEShape shape) {
		super(shape);
	}

	@Override
	public void init(Point p) {
		shape.initDraw(p);
	}
	
	@Override
	public Point getPoint() {
		return shape.getPoint();
	}

	@Override
	public void transformer(Graphics2D g2D, Point p) {
		g2D.setXORMode(GEConstants.BACKGROUND_COLOR);
		g2D.setStroke(basicStroke);
		shape.draw(g2D);
		shape.setCoordinate(p);
		shape.draw(g2D);
	}
	
	public void finalize(ArrayList<GEShape> shapeList) {
		for (GEShape tempShape : shapeList) {
			if (shape.getBounds().contains(tempShape.getBounds())) {
				tempShape.setSelected(true);
			}
		}
	}
	
}
