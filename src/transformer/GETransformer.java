package transformer;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;

import constants.GEConstants;
import shapes.GEShape;

public abstract class GETransformer {

	public static final float dashes[] = {GEConstants.DEFAULT_DASH_OFFSET};
	protected GEShape shape;
	protected BasicStroke basicStroke;
	
	public GETransformer(GEShape shape) {
		this.shape = shape;
		this.basicStroke = new BasicStroke(GEConstants.DEFAULT_DASHEDLINE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1, dashes, 0);
	}
	
	public GEShape getShape() {
		return shape;
	}
	
	abstract public void init(Point p);
	abstract public Point getPoint();
	abstract public void transformer(Graphics2D g2D, Point p);
}
