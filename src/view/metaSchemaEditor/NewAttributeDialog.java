package view.metaSchemaEditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import actions.metaSchemaEditor.NewAttributeAction;
import view.Frame1;
import model.Entitet;
import model.InfResurs;
import model.NodeInf;
import model.TipAtributa;

public class NewAttributeDialog extends JFrame {
	private JPanel content = new JPanel();
	private JComboBox<Entitet> entities = new JComboBox<>();
	private InfResurs ir;
	private JButton okBtn = new JButton("Create");

	private JTextField lengthField = new JTextField();
	private JComboBox<String> isPrimaryKeyCB = new JComboBox<>();
	private JComboBox<String> isMandatoryCB = new JComboBox<>();
	private JComboBox<String> typeCB = new JComboBox<>();
	private JTextField nameField = new JTextField();
	private JTextField groupField = new JTextField();

	public NewAttributeDialog() {
		setTitle("Add attribute");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.ir = MetaSchemaEditFrame.getCurrentInstance().getIr();
		setSize(350, 450);
		content.setLayout(new GridBagLayout());
		initView();
		add(content, BorderLayout.CENTER);
		this.setLocationRelativeTo(MetaSchemaEditFrame.getInstance(false));
	}

	private void initView() {
		for (NodeInf node : (((NodeInf)Frame1.getInstance().getNodeTree().getLastSelectedPathComponent()).getChildren())) {
			if (node instanceof Entitet) {
				entities.addItem((Entitet)node);
			}
		}
		
		for (TipAtributa tip : TipAtributa.values()) {
			typeCB.addItem(tip.toString());
		}
		
		//Panel
		GridBagConstraints c = getConstraints(0, 0);
		content.add(new JLabel("Choose entity:"), c);

		c = getConstraints(1, 0);
		entities.setPreferredSize(new Dimension(200, 35));
		entities.setSelectedItem(null);
		content.add(entities, c);

		c = getConstraints(0, 1);
		content.add(new JLabel("Length:"), c);
		c = getConstraints(1, 1);
		lengthField.setPreferredSize(new Dimension(200, 35));
		content.add(lengthField, c);

		c = getConstraints(0, 2);
		content.add(new JLabel("Primary Key:"), c);
		c = getConstraints(1, 2);
		isPrimaryKeyCB.setPreferredSize(new Dimension(200, 35));
		isPrimaryKeyCB.addItem("True");
		isPrimaryKeyCB.addItem("False");
		content.add(isPrimaryKeyCB, c);

		c = getConstraints(0, 3);
		content.add(new JLabel("Mandatory:"), c);
		c = getConstraints(1, 3);
		isMandatoryCB.setPreferredSize(new Dimension(200, 35));
		isMandatoryCB.addItem("True");
		isMandatoryCB.addItem("False");
		content.add(isMandatoryCB, c);

		c = getConstraints(0, 4);
		content.add(new JLabel("Type:"), c);
		c = getConstraints(1, 4);
		typeCB.setPreferredSize(new Dimension(200, 35));
		typeCB.setSelectedItem(null);
		content.add(typeCB, c);

		c = getConstraints(0, 5);
		content.add(new JLabel("Name:"), c);
		c = getConstraints(1, 5);
		nameField.setPreferredSize(new Dimension(200, 35));
		content.add(nameField, c);

		c = getConstraints(1, 6);
		okBtn.setPreferredSize(new Dimension(100, 30));
		okBtn.addActionListener(new NewAttributeAction(this));
		content.add(okBtn, c);

	}

	private GridBagConstraints getConstraints(int x, int y) {
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

	public JTextField getGroupField() {
		return groupField;
	}
	
}
