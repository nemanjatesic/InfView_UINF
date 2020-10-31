package view.metaSchemaEditor;

import java.awt.Color;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

public class MetaSchemaEditToolBar extends JToolBar {
	private static MetaSchemaEditToolBar instance = null;

	private JButton entityButton;
	private JButton attributeButton;
	private JButton relationButton; 
	
	private MetaSchemaEditToolBar() {
		setBackground(new Color(232, 237, 242));
		setBorder(BorderFactory.createRaisedBevelBorder());
		initButtons();
	}
	
	private void initButtons() {
		entityButton = addButton("Entitet", "src/images/letter-e .png");
		entityButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				EntityEditDialog ed = new EntityEditDialog();
				ed.setVisible(true);
				
			}
		});
		
		addSeparator();
		relationButton = addButton("Relacija", "src/images/letter-r .png");
		relationButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				RelationEditDialog ed = RelationEditDialog.getInstance();
				
				ed.setVisible(true);
				
			}
		});
		
		addSeparator();
		attributeButton = addButton("Atribut", "src/images/letter-a .png");
		attributeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AttributeEditDialog ed = AttributeEditDialog.getInstance();
				ed.setVisible(true);
				
			}
		});
	}

	private JButton addButton(String label, String iconFile) {
		ImageIcon icon = new ImageIcon(iconFile);
		Image image = icon.getImage();
		JButton button = new JButton(new ImageIcon(image.getScaledInstance(35, 35, java.awt.Image.SCALE_SMOOTH)));
		button.setToolTipText(label);
		button.setBackground(new Color(232, 237, 242));
		button.setBorder(new EmptyBorder(5, 5, 5, 5));
		button.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	button.setBackground(new Color(135, 206, 250));
		    }
		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	button.setBackground(new Color(232, 237, 242));
		    }
		});
		
		this.add(button);
		return button;
	}
	
	public static MetaSchemaEditToolBar getInstance() {
		if (instance == null) {
			instance = new MetaSchemaEditToolBar();
		}
		return instance;
	}

	public JButton getEntityButton() {
		return entityButton;
	}

	public JButton getAttributeButton() {
		return attributeButton;
	}

	public JButton getRelationButton() {
		return relationButton;
	}
}
