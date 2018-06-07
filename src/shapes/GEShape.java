package shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.Serializable;

import constants.GEConstants.EAnchorTypes;
import utils.GEAnchorList;

public abstract class GEShape implements Serializable{
	
	protected Shape myShape;
	protected Point startP;
	protected boolean selected;
	protected GEAnchorList anchorList;
	protected EAnchorTypes selectedAnchor;
	protected Color lineColor, fillColor;
	protected AffineTransform affineTransform;

	public GEShape(Shape myShape) {
		this.myShape = myShape;
		this.selected = false;
		this.anchorList = null;
		this.affineTransform = new AffineTransform();
	}
	
	public Shape getShape() {
		return myShape;
	}
	
	public Point getPoint() {
		return startP;
	}
	
	public Color getLineColor() {
		return lineColor;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public GEAnchorList getAnchorList() {
		return anchorList;
	}

	public EAnchorTypes getSelectedAnchor() {
		return selectedAnchor;
	}
	
	public Rectangle getBounds() {
		return myShape.getBounds();
	}
	
	public int getWidth() {
		Rectangle bounds = getBounds();
		return bounds.width;
	}
	
	public int getHeight() {
		Rectangle bounds = getBounds();
		return bounds.height;
	}
	
	public double getIncresedWidthRatio(int width) {
		return (double) width / getWidth();
	}
	
	public double getIncresedHeightRatio(int height) {
		return (double) height / getHeight();
	}
	
	public int getIncresedWidth(double ratio) {
		return (int) (getWidth() * ratio);
	}
	
	public int getIncresedHeight(double ratio) {
		return (int) (getHeight() * ratio);
	}
	
	public int getIncresedWidthVia(int height) {
		return getIncresedWidth(getIncresedHeightRatio(height));
	}
	
	public int getIncresedHeightVia(int width) {
		return getIncresedHeight(getIncresedWidthRatio(width));
	}
	
	public void setShape(Shape shape) {
		this.myShape = shape;
	}
	
	public void setPoint(Point point) {
		this.startP = point;
	}
	
	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}
	
	public void setAnchorList(GEAnchorList anchorList) {
		this.anchorList = anchorList;
	}

	public void setSelectedAnchor(EAnchorTypes selectedAnchor) {
		this.selectedAnchor = selectedAnchor;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		
		if (selected) {
			anchorList = new GEAnchorList();
			anchorList.setPosition(myShape.getBounds());
		}
		else {
			anchorList = null;
		}
	}
	
	public void setGraphicsAttributes(GEShape shape) {
		setPoint(shape.getPoint());
		setLineColor(shape.getLineColor());
		setFillColor(shape.getFillColor());
		setAnchorList(shape.getAnchorList());
		setSelected(shape.isSelected());
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public boolean isVisible() {
		return getWidth() > 0 && getHeight() > 0;
	}
	
	public boolean onShape(Point p) {
		if (anchorList != null) {
			selectedAnchor = anchorList.onAnchors(p);
			
			if (selectedAnchor != EAnchorTypes.NONE) {
				return true;
			}
		}
		
		return myShape.intersects(new Rectangle(p.x, p.y, 2, 2));
	}
	
	public EAnchorTypes onAnchor(Point p) {
		selectedAnchor = anchorList.onAnchors(p);
		return selectedAnchor;
	}
	
	public void draw(Graphics2D g2D) {
		if (fillColor != null) {
			g2D.setColor(fillColor);
			g2D.fill(myShape);
		}
		
		if (lineColor != null) {
			g2D.setColor(lineColor);
			g2D.draw(myShape);
		}
		
		if (selected) {
			anchorList.setPosition(myShape.getBounds());
			anchorList.draw(g2D);
		}
	}
	
	public void resizeCoordinate(Point2D resizeFactor) {
		affineTransform.setToScale(resizeFactor.getX(), resizeFactor.getY());
		myShape = affineTransform.createTransformedShape(myShape);
	}
	
	public void rotateCoordinate(double degree, Point2D rotateAnchor) {
		affineTransform.setToRotation(degree, rotateAnchor.getX(), rotateAnchor.getY());
		myShape = affineTransform.createTransformedShape(myShape);
	}
	
	public void move(Point resizeAnchor) {
		affineTransform.setToTranslation(resizeAnchor.x, resizeAnchor.y);
		myShape = affineTransform.createTransformedShape(myShape);
	}
	
	public void moveReverse(Point resizeAnchor) {
		affineTransform.setToTranslation(-resizeAnchor.x, -resizeAnchor.y);
		myShape = affineTransform.createTransformedShape(myShape);
	}
	
	public void moveCoordinate(Point moveP) {
		affineTransform.setToTranslation(moveP.x, moveP.y);
		myShape = affineTransform.createTransformedShape(myShape);
	}
	
	abstract public void initDraw(Point startP);
	abstract public void setCoordinate(Point currentP);
	abstract public GEShape clone();
	abstract public GEShape deepCopy();
}
