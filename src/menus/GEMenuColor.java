package menus;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import constants.GEConstants;
import constants.GEConstants.EColorMenuItems;
import frames.GEDrawingPanel;

@SuppressWarnings("serial")
public class GEMenuColor extends JMenu {

	private GEDrawingPanel drawingPanel;

	public GEMenuColor(String label) {
		super(label);

		for (EColorMenuItems btn : EColorMenuItems.values()) {
			JMenuItem menuItem = new JMenuItem(btn.toString());		
			menuItem.setActionCommand(btn.toString());
			menuItem.addActionListener(new ColorMenuHandler());
			add(menuItem);
		}
	}
	
	public void init(GEDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}

	private class ColorMenuHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			switch (EColorMenuItems.valueOf(e.getActionCommand())) {
			case SetLineColor:
				setLineColor();
				break;
				
			case SetFillColor:
				setFillColor();
				break;
				
			case ClearLineColor:
				clearLineColor();
				break;
				
			case ClearFillColor:
				clearFillColor();
				break;
			}
		}
	}
	
	private void setLineColor() {
		Color lineColor = JColorChooser.showDialog(null, GEConstants.LINE_COLOR_TITLE, null);
		drawingPanel.setLineColor(lineColor);
	}

	private void clearFillColor() {
		drawingPanel.setFillColor(GEConstants.COLOR_FILL_DEFAULT);
	}

	private void clearLineColor() {
		drawingPanel.setLineColor(GEConstants.COLOR_LINE_DEFAULT);
	}

	private void setFillColor() {
		Color fillColor = JColorChooser.showDialog(null, GEConstants.FILL_COLOR_TITLE, null);
		drawingPanel.setFillColor(fillColor);
	}
}
