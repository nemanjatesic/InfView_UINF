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
import javax.swing.WindowConstants;

import actions.metaSchemaEditor.DeleteAttributeAction;
import view.Frame1;
import model.Atribut;
import model.Entitet;
import model.InfResurs;
import model.NodeInf;

public class DeleteAttributeDialog extends JFrame {
	private JPanel content = new JPanel();
	private JComboBox<Entitet> entities = new JComboBox<>();
	private JComboBox<Atribut> attributes = new JComboBox<>();
	private InfResurs ir;
	private JButton okBtn = new JButton("Delete");
	
	public DeleteAttributeDialog() {
		setTitle("Delete attribute");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.ir = MetaSchemaEditFrame.getCurrentInstance().getIr();
		setSize(410, 230);
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
		entities.setSelectedItem(null);
		
		GridBagConstraints c = getConstraints(0, 0);
		content.add(new JLabel("Choose entity:"), c);
		
		
		c = getConstraints(1, 0);
		entities.setPreferredSize(new Dimension(200, 35));
		entities.setSelectedItem(null);
		entities.addActionListener((e) -> {
			attributes.removeAllItems();
			attributes.setSelectedItem(null);
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
		
		c = getConstraints(1, 2);
		okBtn.setPreferredSize(new Dimension(100, 30));
		okBtn.addActionListener(new DeleteAttributeAction(this));
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
	
}
