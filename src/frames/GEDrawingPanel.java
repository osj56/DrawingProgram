package frames;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JPanel;

import constants.GEConstants;
import constants.GEConstants.EAnchorTypes;
import constants.GEConstants.EState;
import shapes.GEGroup;
import shapes.GEPolygon;
import shapes.GESelect;
import shapes.GEShape;
import transformer.GEDrawer;
import transformer.GEGrouper;
import transformer.GEMover;
import transformer.GEResizer;
import transformer.GERotater;
import transformer.GETransformer;
import utils.GECursorManager;

@SuppressWarnings("serial")
public class GEDrawingPanel extends JPanel {

	private boolean characteristic;
	private EState currentState;
	private Color fillColor, lineColor;
	private GEShape currentShape, selectedShape, temporaryShape;
	private ArrayList<GEShape> shapeList;
	private MouseDrawingHandler drawingHandler;
	private KeyHandler keyHandler;
	private GECursorManager cursorManager;
	private GETransformer transformer;
	private ArrayList<GEShape> undoList;
	
	public GEDrawingPanel() {
		super();
		this.characteristic = false;
		this.currentState = EState.Idle;
		this.fillColor = GEConstants.COLOR_FILL_DEFAULT;
		this.lineColor = GEConstants.COLOR_LINE_DEFAULT;
		this.shapeList = new ArrayList<GEShape>();
		this.cursorManager = new GECursorManager();
		this.drawingHandler = new MouseDrawingHandler();
		this.keyHandler = new KeyHandler();
		this.undoList = new ArrayList<GEShape>();
		
		setBackground(GEConstants.BACKGROUND_COLOR);
		setForeground(GEConstants.FOREGROUND_COLOR);
		// mouse press & release event
		addMouseListener(drawingHandler);
		// mouse drag event
		addMouseMotionListener(drawingHandler);
		// key press event
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyHandler);
	}
	
	public void setCurrentState(EState state) {
		this.currentState = state;
	}

	public void setFillColor(Color fillColor) {
		if (selectedShape != null) {
			selectedShape.setFillColor(fillColor);
			repaint();
		}
		
		this.fillColor = fillColor;
	}
	
	public void setLineColor(Color lineColor) {
		if (selectedShape != null) {
			selectedShape.setLineColor(lineColor);
			repaint();
		}
		
		this.lineColor = lineColor;
	}

	public void setCurrentShape(GEShape currentShape) {
		this.currentShape = currentShape;
	}
	
	public void setSelectedShape(GEShape selectedShape) {
		this.selectedShape = selectedShape;
	}
	
	public void setTemporaryShape(GEShape temporaryShape) {
		this.temporaryShape = temporaryShape.deepCopy();
	}
	
	public boolean onState(EState state) {
		return currentState == state;
	}
	
	public void resize(int width, int height) {
		if (selectedShape != null) {
			Rectangle bounds = selectedShape.getBounds();
			Point currentFactor = new Point(bounds.x + bounds.width, bounds.y + bounds.height);
			Point resizeFactor = new Point(bounds.x + width, bounds.y + height);
			GEResizer transformer = new GEResizer(selectedShape);
			
			selectedShape.setSelectedAnchor(EAnchorTypes.SE);
			transformer.init(currentFactor);
			transformer.transformer((Graphics2D) getGraphics(), resizeFactor);
			transformer.finalize();
			repaint();
		}
	}
	
	public void paste(ArrayList<GEShape> shapes) {
		for (GEShape shape : shapes) {
			shapeList.add(shape.deepCopy());
		}
		
		repaint();
	}
	
	public ArrayList<GEShape> copy() {
		ArrayList<GEShape> returnList = new ArrayList<GEShape>();
		
		for (GEShape shape: shapeList) {
			if (shape.isSelected()) {
				returnList.add(shape.deepCopy());
			}
		}
		
		return returnList;
	}
	
	public ArrayList<GEShape> cut() {
		ArrayList<GEShape> shapes = new ArrayList<GEShape>();
		
		for (int i = shapeList.size(); i > 0; i--) {
			GEShape shape = shapeList.get(i - 1);
			
			if (shape.isSelected()) {
				shapes.add(0, shape.deepCopy());
				shapeList.remove(shape);
			}
		}
		
		repaint();
		
		return shapes;
	}
	
	public void delete() {
		for (int i = shapeList.size(); i > 0; i--) {
			GEShape shape = shapeList.get(i - 1);
			
			if (shape.isSelected()) {
				shapeList.remove(shape);
			}
		}
		
		repaint();
	}
	
	public void group(GEGroup group) {
		boolean grouped = false;
		
		for (int i = 0, j = shapeList.size(); i < j; i++) {
			GEShape shape = shapeList.get(i);
			if (shape.isSelected()) {
				shape.setSelected(false);
				group.addShape(shape);
				shapeList.remove(shape);
				grouped = true;
				i--; j--;
			}
		}
		
		if (grouped) {
			group.setSelected(true);
			shapeList.add(group);
			repaint();
		}
	}

	public void unGroup() {
		if (selectedShape instanceof GEGroup) {
			ArrayList<GEShape> childList = ((GEGroup) selectedShape).getShapeList();
			for (GEShape shape : childList) {
				shape.setSelected(true);
			}
			shapeList.remove(selectedShape);
			shapeList.addAll(childList);
			repaint();
		}
	}
	
	 public void undo(){
	      int i = shapeList.size()-1;
	      undoList.add(shapeList.get(i));
	      shapeList.remove(i);
	      System.out.println("shapeList : " +shapeList.size());
	      repaint();
	 }
	 
	 public void redo() {
		 int i = undoList.size() - 1, j = 0;
		 shapeList.add(undoList.get(i));
		 undoList.remove(i);
		 repaint();
	 }
	 
	public void setShapeList(ArrayList<GEShape> shapeList) {
		this.shapeList = shapeList;
	}
	
	public Object getShapeList() {
		return this.shapeList;
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		
		for (GEShape shape : shapeList) {
			shape.draw(g2D);
		}
	}

	public void initDraw(Point startP) {
		currentShape = currentShape.clone();
		currentShape.setPoint(startP);
		currentShape.setFillColor(fillColor);
		currentShape.setLineColor(lineColor);
	}

	public void continueDraw(Point currentP) {
		((GEDrawer) transformer).continueDrawing(currentP);
	}
	
	private void finishDraw() {
		shapeList.add(currentShape);
	}

	private GEShape onShape(Point p) {
		for (int i = shapeList.size() - 1; i > -1; i--) {
			GEShape shape = shapeList.get(i);
			if (shape.onShape(p)) {
				return shape;
			}
		}
		
		return null;
	}

	private void clearSelectedShapes() {
		for (GEShape shape : shapeList) {
			shape.setSelected(false);
		}
	}
	
	public void clearShapeList(){
		for(int i=shapeList.size(); i>0; i--) {
			shapeList.clear();
		}
	}
	
	private class MouseDrawingHandler extends MouseAdapter {
		@Override
		public void mouseDragged(MouseEvent e) {
			if (!onState(EState.Idle)) {
				Point currentP = e.getPoint();
				
				if (characteristic) {
					Point startP = transformer.getPoint();
					int width = currentP.x - startP.x;
					
					if (temporaryShape.isVisible()) {
						currentP.y = startP.y + temporaryShape.getIncresedHeightVia(width);
					}
					else {
						currentP.y = startP.y + width;
					}
				}
				
				transformer.transformer((Graphics2D) getGraphics(), currentP);
			}
		}
		
		public void mousePressed(MouseEvent e) {
			if (onState(EState.Idle)) {
				if (currentShape instanceof GESelect) {
					setSelectedShape(onShape(e.getPoint()));
					if (selectedShape != null) {
						if (!characteristic) {
							clearSelectedShapes();
						}
						
						selectedShape.setSelected(true);
						selectedShape.onAnchor(e.getPoint());
						
						if (selectedShape.getSelectedAnchor() == EAnchorTypes.NONE) { 
							transformer = new GEMover(selectedShape);
							((GEMover) transformer).init(e.getPoint());
							setCurrentState(EState.Moving); 
						}
						else if (selectedShape.getSelectedAnchor() == EAnchorTypes.RR) {
							transformer = new GERotater(selectedShape);
							((GERotater) transformer).init(e.getPoint());
							setCurrentState(EState.Rotating);
						}
						else {
							transformer = new GEResizer(selectedShape);
							((GEResizer) transformer).init(e.getPoint());
							setCurrentState(EState.Resizing); 
						}
						
						setTemporaryShape(selectedShape);
					}
					else {
						setCurrentState(EState.Selecting);
						clearSelectedShapes();
						initDraw(e.getPoint());
						transformer = new GEGrouper(currentShape);
						((GEGrouper) transformer).init(e.getPoint());
					}
				}
				else {
					clearSelectedShapes();
					initDraw(e.getPoint());
					transformer = new GEDrawer(currentShape);
					((GEDrawer) transformer).init(e.getPoint());
					setCurrentState(currentShape instanceof GEPolygon ? EState.NPointsDrawing : EState.TwoPointsDrawing);
					setTemporaryShape(currentShape);
				}
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			if (onState(EState.TwoPointsDrawing)) {
				finishDraw();
			}
			else if (onState(EState.NPointsDrawing)) {
				return;
			}
			else if (onState(EState.Resizing)) {
				((GEResizer) transformer).finalize();
			}
			else if (onState(EState.Selecting)) {
				((GEGrouper) transformer).finalize(shapeList);
			}
			
			setCurrentState(EState.Idle);
			repaint();
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (onState(EState.NPointsDrawing)) {
					if (e.getClickCount() == 1) {
						continueDraw(e.getPoint());
					}
					else if (e.getClickCount() == 2) {
						finishDraw();
						setCurrentState(EState.Idle);
						repaint();
					}
				}
			}
		}
		
		@Override
		public void mouseMoved(MouseEvent e) {
			if (onState(EState.NPointsDrawing)) {				
				transformer.transformer((Graphics2D)getGraphics(), e.getPoint());
			}
			else if (onState(EState.Idle)) {
				GEShape shape = onShape(e.getPoint());
				if (shape == null) {
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
				else if (shape.isSelected()) {
					EAnchorTypes anchorType = shape.onAnchor(e.getPoint());
					setCursor(cursorManager.get(anchorType.ordinal()));
				}
			}
		}
	}
	
	private class KeyHandler implements KeyEventDispatcher {
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
				switch (e.getID()) {
				case KeyEvent.KEY_PRESSED:
					characteristic = true;
					break;
					
				case KeyEvent.KEY_RELEASED:
					characteristic = false;
					break;
				}
			}
			
			return false;
		}
	}

	
}
