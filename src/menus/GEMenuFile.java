package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import constants.GEConstants.EFileMenuItems;
import frames.GEDrawingPanel;
import shapes.GEShape;


public class GEMenuFile extends JMenu {
	private GEDrawingPanel drawingPanel;
	private FileMenuHandler fileMenuHandler;
	private File file;
	
	public GEMenuFile(String label) {
		super(label);
		fileMenuHandler = new FileMenuHandler();
		file = null;
		for (EFileMenuItems btn : EFileMenuItems.values()) {
			JMenuItem menuItem = new JMenuItem(btn.toString());
			menuItem.addActionListener(fileMenuHandler);
			menuItem.setActionCommand(btn.toString());
			this.add(menuItem);
		}
	}
	
	public void init(GEDrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}
	
	
	private void newFile() {
		System.out.println();
		drawingPanel.clearShapeList();
		file = null;
		drawingPanel.repaint();
	}
	
	private void open() {
		JFileChooser fileDialog = new JFileChooser(new File("*.*"));
		fileDialog.showOpenDialog(null);
		file = fileDialog.getSelectedFile();
		ObjectInputStream in = null;
		if (file != null) {
			try {
				in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
				Object obj = in.readObject();
				drawingPanel.setShapeList((ArrayList<GEShape>) obj);
			} catch(IOException e) {
				e.printStackTrace();
			} catch(ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					if (in != null) in.close();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		drawingPanel.repaint();
	}
	
	private void save(boolean name) {
		if (file == null || name == true) {
			JFileChooser fileDialog = new JFileChooser(new File("*.*"));
			fileDialog.showSaveDialog(null);
			file = fileDialog.getSelectedFile();
		}
		ObjectOutputStream out = null;
		if (file != null) {
			try {
				out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
				out.writeObject(drawingPanel.getShapeList());
				JOptionPane.showMessageDialog(drawingPanel, file.getParentFile() + "��ġ��\n" + file.getName() + "�� ����Ǿ����ϴ�", "���� �Ϸ�", 1);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private void close() {
		System.exit(0);
	}
	
	private class FileMenuHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			switch(EFileMenuItems.valueOf(e.getActionCommand())) {
			case ���θ���� : newFile(); 
				break;
			case ���� : open(); 
				break;
			case ���� : save(false); 
				break;
			case �ٸ��̸��������� : save(true); 
				break;
			case ���� : close(); 
				break;
			}
		}
	}
}
