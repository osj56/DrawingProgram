package menus;

import javax.swing.JMenuBar;

import constants.GEConstants;
import frames.GEDrawingPanel;

@SuppressWarnings("serial")
public class GEMenuBar extends JMenuBar {
	
	private GEMenuFile fileMenu;
	private GEMenuEdit editMenu;
	private GEMenuColor colorMenu;

	public GEMenuBar() {
		fileMenu = new GEMenuFile(GEConstants.TITLE_FILEMENU);
		editMenu = new GEMenuEdit(GEConstants.TITLE_EDITMENU);
		colorMenu = new GEMenuColor(GEConstants.TITLE_COLORMENU);
		
		add(fileMenu);
		add(editMenu);
		add(colorMenu);
	}
	
	public void init(GEDrawingPanel drawingPanel) {
		fileMenu.init(drawingPanel);
		editMenu.init(drawingPanel);
		colorMenu.init(drawingPanel);
	}
}
