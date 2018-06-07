package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import constants.GEConstants.EEditMenuItems;
import frames.GEDrawingPanel;
import shapes.GEGroup;
import shapes.GEShape;

@SuppressWarnings("serial")
public class GEMenuEdit extends JMenu {
	
	private GEDrawingPanel drawingPanel;
	private EditMenuHandler editMenuHandler;
	private ArrayList<GEShape> copyList;
	
	public GEMenuEdit(String label) {
		super(label);
		
		editMenuHandler = new EditMenuHandler();
		copyList = new ArrayList<GEShape>();
		
		for (EEditMenuItems btn : EEditMenuItems.values()) {
			JMenuItem menuItem = new JMenuItem(btn.toString());	
			menuItem.addActionListener(editMenuHandler);
			menuItem.setActionCommand(btn.toString());
			add(menuItem);
		}
	}

	public void init(GEDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}
	
	public void paste() {
		drawingPanel.paste(copyList);
	}

	public void copy() {
		copyList.clear();
		copyList.addAll(drawingPanel.copy());
	}

	public void cut() {
		copyList.clear();
		copyList.addAll(drawingPanel.cut());
	}

	public void delete() {
		drawingPanel.delete();
	}
	
	public void group() {
		drawingPanel.group(new GEGroup());
	}
	
	public void unGroup() {
		drawingPanel.unGroup();
	}

	private class EditMenuHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			switch (EEditMenuItems.valueOf(e.getActionCommand())) {
			case Undo: drawingPanel.undo();
				break;
			case Redo: drawingPanel.redo();
				break;
			case 삭제: delete();
				break;
			case 잘라내기: cut();
				break;
			case 복사: copy();
				break;	
			case 붙이기: paste();
				break;		
			case Group: group();
				break;
			case Ungroup: unGroup();
				break;
			}
		}
	}
}
