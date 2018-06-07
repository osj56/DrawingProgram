package shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import constants.GEConstants;
import constants.GEConstants.EAnchorTypes;

public class GEGroup extends GEShape {
	
	public static final float dashes[] = {GEConstants.DEFAULT_DASH_OFFSET};
	private ArrayList<GEShape> shapeList;
	private BasicStroke basicStroke;

	public GEGroup() {
		super(new Rectangle());
		this.shapeList = new ArrayList<GEShape>();
		this.basicStroke = new BasicStroke(GEConstants.DEFAULT_DASHEDLINE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dashes, 0);
	}
	
	@Override
	public void initDraw(Point startP) {
		this.startP = startP;
		
		for (GEShape shape : shapeList) {
			shape.initDraw(startP);
		}
	}
	
	public void initGroup() {
		if (!shapeList.isEmpty()) {
			myShape = shapeList.get(0).getBounds();
			
			for (int i = 1; i < shapeList.size(); i++) {
				GEShape shape = shapeList.get(i);
				myShape = myShape.getBounds().createUnion(shape.getBounds());
			}
			
			this.startP = new Point(myShape.getBounds().x, myShape.getBounds().y);
		}
	}

	public void addShape(GEShape shape) {
		shapeList.add(shape);
		initGroup();
	}
	
	public ArrayList<GEShape> getShapeList() {
		return shapeList;
	}
	
	@Override
	public void setLineColor(Color lineColor) {
		for (GEShape shape : shapeList) {
			shape.setLineColor(lineColor);
		}
	}
	
	@Override
	public void setFillColor(Color fillColor) {
		for (GEShape shape : shapeList) {
			shape.setFillColor(fillColor);
		}
	}
	
	@Override
	public boolean onShape(Point p) {
		if (anchorList != null) {
			selectedAnchor = anchorList.onAnchors(p);
			
			if (selectedAnchor != EAnchorTypes.NONE) {
				return true;
			}
		}
		
		for (GEShape shape : shapeList) {
			if (shape.onShape(p)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public void draw(Graphics2D g2D) {
		for (GEShape shape : shapeList) {
			shape.draw(g2D);
		}
		
		if(isSelected()) {
			g2D.setColor(GEConstants.COLOR_LINE_DEFAULT);
			g2D.setStroke(basicStroke);
			g2D.draw(myShape);
			g2D.setStroke(new BasicStroke());
			getAnchorList().setPosition(getBounds());
			getAnchorList().draw(g2D);
		}
	}
	
	@Override
	public void setCoordinate(Point currentP) {
		for (GEShape shape : shapeList) {
			shape.setCoordinate(currentP);
		}
	}
	
	@Override
	public void resizeCoordinate(Point2D resizeFactor) {
		super.resizeCoordinate(resizeFactor);
		
		for (GEShape shape : shapeList) {
			shape.resizeCoordinate(resizeFactor);
		}
		
		initGroup();
	}
	
	@Override
	public void move(Point resizeAnchor) {		
		super.move(resizeAnchor);
		
		for (GEShape shape : shapeList) {
			shape.move(resizeAnchor);
		}
		
		initGroup();
	}
	
	@Override
	public void moveReverse(Point resizeAnchor) {	
		super.moveReverse(resizeAnchor);
		
		for (GEShape shape : shapeList) {
			shape.moveReverse(resizeAnchor);
		}
		
		initGroup();
	}
	
	@Override
	public void moveCoordinate(Point moveP) {
		super.moveCoordinate(moveP);
		
		for (GEShape shape : shapeList) {
			shape.moveCoordinate(moveP);
		}
		
		initGroup();
	}
	
	@Override
	public GEShape clone() {
		GEGroup newShape = new GEGroup();
		
		for (GEShape shape : shapeList) {
			newShape.addShape(shape.clone());
		}
		
		return newShape;
	}
	
	@Override
	public GEShape deepCopy() {
		GEGroup newShape = new GEGroup();
		
		for (GEShape shape : shapeList) {
			newShape.addShape(shape.deepCopy());
		}
		
		return newShape;
	}
}
