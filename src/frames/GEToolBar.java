package frames;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import constants.GEConstants;
import constants.GEConstants.EToolBarButtons;
import shapes.GEEllipse;
import shapes.GELine;
import shapes.GEPolygon;
import shapes.GERectangle;
import shapes.GESelect;

@SuppressWarnings("serial")
public class GEToolBar extends JToolBar {
	
	private GEDrawingPanel drawingPanel;
	private GEToolBarHandler toolBarHandler;
	private GESizeHandler sizeHandler;
	private ButtonGroup buttonGroup;
	private JTextField widthField;
	private JTextField heightField;
	private JLabel widthLabel;
	private JLabel heightLabel;

	public GEToolBar(String label) {
		super(label);
		
		toolBarHandler = new GEToolBarHandler();
		sizeHandler = new GESizeHandler();
		buttonGroup = new ButtonGroup();
		
		for (EToolBarButtons btn : EToolBarButtons.values()) {
			JRadioButton rbutton = new JRadioButton();
			rbutton.setIcon(new ImageIcon(GEConstants.IMG_URL + btn.toString() + GEConstants.TOOLBAR_BTN));
			rbutton.setSelectedIcon(new ImageIcon(GEConstants.IMG_URL + btn.toString() + GEConstants.TOOLBAR_BTN_SLT));
			rbutton.setActionCommand(btn.name());
			rbutton.addActionListener(toolBarHandler);
			
			add(rbutton);
			buttonGroup.add(rbutton);
		}
		
		
		setLayout(new FlowLayout(FlowLayout.LEADING));
	}

	private void clickDefaultButton() {
		JRadioButton rButton = (JRadioButton) getComponent(EToolBarButtons.Rectangle.ordinal());
		rButton.doClick();
	}

	public void init(GEDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
		clickDefaultButton();
	}

	private class GEToolBarHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JRadioButton btn = (JRadioButton) e.getSource();
			
			if (btn.getActionCommand().equals(EToolBarButtons.Select.name())) {
				drawingPanel.setCurrentShape(new GESelect());
			}
			else if (btn.getActionCommand().equals(EToolBarButtons.Rectangle.name())) {
				drawingPanel.setCurrentShape(new GERectangle());
			}
			else if (btn.getActionCommand().equals(EToolBarButtons.Ellipse.name())) {
				drawingPanel.setCurrentShape(new GEEllipse());
			}
			else if (btn.getActionCommand().equals(EToolBarButtons.Line.name())) {
				drawingPanel.setCurrentShape(new GELine());
			}
			else if (btn.getActionCommand().equals(EToolBarButtons.Polygon.name())) {
				drawingPanel.setCurrentShape(new GEPolygon());
			}
		}
	}
	
	private class GESizeHandler extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				try {
					drawingPanel.resize(
							Integer.parseInt(widthField.getText()),
							Integer.parseInt(heightField.getText()));
				}
				catch (NumberFormatException ex) {}
			}
		}
	}
}
