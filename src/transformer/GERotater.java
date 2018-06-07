package transformer;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import shapes.GEGroup;
import shapes.GEShape;

public class GERotater extends GETransformer {
	
	private ArrayList<GEShape> shapeList;
	private Point2D.Double rOrigin;
	private double degree;

	public GERotater(GEShape shape) {
		super(shape);
		
		if (shape instanceof GEGroup) {
			shapeList = new ArrayList<GEShape>();
			for (GEShape childshape : ((GEGroup) shape).getShapeList()) {
				shapeList.add(childshape);
			}
		}
	}
	
	@Override
	public void init(Point p) {
		rOrigin = new Point2D.Double(shape.getBounds().getCenterX(), shape.getBounds().getCenterY());
		degree = Math.atan2(rOrigin.y - p.getY(), rOrigin.x - p.getX());
	}

	@Override
	public Point getPoint() {
		return shape.getPoint();
	}
	
	@Override
	public void transformer(Graphics2D g2d, Point p) {
		g2d.setXORMode(g2d.getBackground());
		g2d.setStroke(basicStroke);
		double newDegree = degree - Math.atan2(rOrigin.y - p.getY(), p.getX() - rOrigin.x);
		
		if (shape instanceof GEGroup) {
			for (GEShape temp : shapeList) {
				temp.draw(g2d);
				temp.rotateCoordinate(newDegree, rOrigin);
				temp.draw(g2d);
			}
		}
		else {
			shape.draw(g2d);
			shape.rotateCoordinate(newDegree, rOrigin);
			shape.draw(g2d);
		}
		
		degree = Math.atan2(rOrigin.y - p.getY(), p.getX() - rOrigin.x);
	}
}
