package frames;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import constants.GEConstants;
import menus.GEMenuBar;
import menus.GEMenuFile;

@SuppressWarnings("serial")
public class GEMainFrame extends JFrame {

	private static GEMainFrame uniqueMainFrame = new GEMainFrame(GEConstants.TITLE_MAINFRAME);
	private GEDrawingPanel drawingPanel;
	private GEMenuBar menuBar;
	private GEToolBar toolBar;

	private GEMainFrame(String title) { 
		super(title);

		drawingPanel = new GEDrawingPanel();
		menuBar = new GEMenuBar();
		toolBar = new GEToolBar(GEConstants.TITLE_TOOLBAR);
		
		add(drawingPanel);
		setJMenuBar(menuBar);
		add(BorderLayout.NORTH, toolBar);
	}

	public static GEMainFrame getInstance() {
		return uniqueMainFrame;
	}

	public void init() {
		toolBar.init(drawingPanel);
		menuBar.init(drawingPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(GEConstants.WIDTH_MAINFRAME, GEConstants.HEIGHT_MAINFRAME);
		setVisible(true);
	}
}
