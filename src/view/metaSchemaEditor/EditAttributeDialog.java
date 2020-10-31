package view.metaSchemaEditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import actions.metaSchemaEditor.EditAttributeAction;
import view.Frame1;
import model.Atribut;
import model.Entitet;
import model.InfResurs;
import model.NodeInf;
import model.TipAtributa;

public class EditAttributeDialog extends JFrame {
	private JPanel content = new JPanel();
	private JComboBox<Entitet> entities = new JComboBox<>();
	private JComboBox<Atribut> attributes = new JComboBox<>();
	private InfResurs ir;
	private JButton okBtn = new JButton("Edit");
	
	private JTextField lengthField = new JTextField();
	private JComboBox<String> isPrimaryKeyCB = new JComboBox<>();
	private JComboBox<String> isMandatoryCB = new JComboBox<>();
	private JComboBox<String> typeCB = new JComboBox<>();
	private JTextField nameField = new JTextField();

	
	public EditAttributeDialog() {
		setTitle("Edit attribute");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.ir = MetaSchemaEditFrame.getCurrentInstance().getIr();
		setSize(450, 650);
		content.setLayout(new GridBagLayout());
		setContent();
		add(content, BorderLayout.CENTER);
		this.setLocationRelativeTo(MetaSchemaEditFrame.getInstance(false));
	}
	
	private void setContent() {
		for (NodeInf node : (((NodeInf)Frame1.getInstance().getNodeTree().getLastSelectedPathComponent()).getChildren())) {
			if (node instanceof Entitet) {
				entities.addItem((Entitet)node);
			}
		}
		
		for (TipAtributa tip : TipAtributa.values()) {
			typeCB.addItem(tip.toString());
		}
		
		GridBagConstraints c = getConstraints(0, 0);
		content.add(new JLabel("Choose entity:"), c);
		
		c = getConstraints(1, 0);
		entities.setPreferredSize(new Dimension(200, 35));
		entities.setSelectedItem(null);
		entities.addActionListener((e) -> {
			attributes.removeAllItems();
			Entitet selectedEntity = (Entitet)entities.getSelectedItem();
			for (Atribut attr : selectedEntity.getAtributi()) {
				attributes.addItem(attr);
			}
			attributes.setVisible(true);
		});
		content.add(entities, c);
		
		c = getConstraints(0, 1);
		content.add(new JLabel("Choose attribute:"), c);
		
		c = getConstraints(1, 1);
		attributes.setPreferredSize(new Dimension(200, 35));
		attributes.setVisible(false);
		content.add(attributes, c);
		
		c = getConstraints(0, 2);
		content.add(new JLabel("Length:"), c);
		c = getConstraints(1, 2);
		lengthField.setPreferredSize(new Dimension(200, 35));
		content.add(lengthField, c);
		
		c = getConstraints(0, 3);
		content.add(new JLabel("Primary Key:"), c);
		c = getConstraints(1, 3);
		isPrimaryKeyCB.setPreferredSize(new Dimension(200, 35));
		isPrimaryKeyCB.addItem("True");
		isPrimaryKeyCB.addItem("False");
		content.add(isPrimaryKeyCB, c);
		
		c = getConstraints(0, 4);
		content.add(new JLabel("Mandatory:"), c);
		c = getConstraints(1, 4);
		isMandatoryCB.setPreferredSize(new Dimension(200, 35));
		isMandatoryCB.addItem("True");
		isMandatoryCB.addItem("False");
		content.add(isMandatoryCB, c);
		
		c = getConstraints(0, 5);
		content.add(new JLabel("Type:"), c);
		c = getConstraints(1, 5);
		typeCB.setPreferredSize(new Dimension(200, 35));
		content.add(typeCB, c);
		
		c = getConstraints(0, 6);
		content.add(new JLabel("Name:"), c);
		c = getConstraints(1, 6);
		nameField.setPreferredSize(new Dimension(200, 35));
		content.add(nameField, c);
		
		
		c = getConstraints(1, 7);
		okBtn.setPreferredSize(new Dimension(100, 30));
		okBtn.addActionListener(new EditAttributeAction(this));
		content.add(okBtn, c);
		
	}
	
	private GridBagConstraints getConstraints(int x, int y){
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.insets = new Insets(20, 20, 0, 0);
        c.anchor = GridBagConstraints.WEST;
        return c;
    }

	public JPanel getContent() {
		return content;
	}

	public JComboBox<Entitet> getEntities() {
		return entities;
	}

	public JComboBox<Atribut> getAttributes() {
		return attributes;
	}

	public InfResurs getIr() {
		return ir;
	}

	public JButton getOkBtn() {
		return okBtn;
	}

	public JTextField getLengthField() {
		return lengthField;
	}

	public JComboBox<String> getIsPrimaryKeyCB() {
		return isPrimaryKeyCB;
	}

	public JComboBox<String> getIsMandatoryCB() {
		return isMandatoryCB;
	}

	public JComboBox<String> getTypeCB() {
		return typeCB;
	}

	public JTextField getNameField() {
		return nameField;
	}

}
