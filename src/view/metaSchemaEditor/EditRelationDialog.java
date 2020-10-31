package view.metaSchemaEditor;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import actions.metaSchemaEditor.EditRelationAction;
import view.Frame1;
import model.Atribut;
import model.Entitet;
import model.InfResurs;
import model.NodeInf;
import model.Relacija;

public class EditRelationDialog extends JFrame{
	private InfResurs infRes;
	
	private JPanel content = new JPanel(new GridBagLayout());
	
	private JLabel chooseRelLbl = new JLabel("Choose relation:");
	private JComboBox<Relacija> relationsCombo = new JComboBox<>();
	private JLabel nameLbl = new JLabel("Name:");
	private JTextField nameTf = new JTextField();
	private JLabel fromEntLbl = new JLabel("Relation from entity:");
	private JComboBox<Entitet> fromEntCombo = new JComboBox<>();
	private JLabel toEntLbl = new JLabel("Relation to entity:");
	private JComboBox<Entitet> toEntCombo = new JComboBox<>();
	private JLabel fromAttLbl = new JLabel("Relation from attribute:");
	private JComboBox<Atribut> fromAttCombo = new JComboBox<>();
	private JLabel toAttLbl = new JLabel("Relation to attribute:");
	private JComboBox<Atribut> toAttCombo = new JComboBox<>();
	
	private JButton okBtn = new JButton("Edit");
	 
	public EditRelationDialog(InfResurs infRes){
		super();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.infRes = infRes;
		this.setTitle("Edit relation");
		this.setSize(440, 470);
		this.setLocationRelativeTo(MetaSchemaEditFrame.getInstance(false));
		initView();
		okBtn.addActionListener(new EditRelationAction(this));
	}
	
	private void initView() {
		for (NodeInf node : (((NodeInf)Frame1.getInstance().getNodeTree().getLastSelectedPathComponent()).getChildren())) {
			if (node instanceof Entitet) {
				fromEntCombo.addItem((Entitet)node);
				toEntCombo.addItem((Entitet)node);
			}
		}
		
		ArrayList<Relacija> tmp = new ArrayList<>();
		for (NodeInf node : (((NodeInf)Frame1.getInstance().getNodeTree().getLastSelectedPathComponent()).getChildren())) {
			for (Relacija rel : ((Entitet)node).getRelacije()) {
				if (!tmp.contains(rel)) {
					tmp.add(rel);
					relationsCombo.addItem(rel);
				}
			}
		}
		
		// Panel
		GridBagConstraints c = getConstraints(0, 0);
		
		content.add(nameLbl, c);
		c = getConstraints(1, 0);
		nameTf.setPreferredSize(new Dimension(200, 35));
		content.add(nameTf, c);
		
		c = getConstraints(0, 1);
		content.add(chooseRelLbl, c);
		c = getConstraints(1, 1);
		relationsCombo.setPreferredSize(new Dimension(200, 35));
		relationsCombo.setSelectedItem(null);
		content.add(relationsCombo, c);
		
		c = getConstraints(0, 2);
		content.add(fromEntLbl, c);
		c = getConstraints(1, 2);
		fromEntCombo.setPreferredSize(new Dimension(200, 35));
		fromEntCombo.setSelectedItem(null);
		content.add(fromEntCombo, c);
		
		c = getConstraints(0, 3);
		content.add(toEntLbl, c);
		c = getConstraints(1, 3);
		toEntCombo.setPreferredSize(new Dimension(200, 35));
		toEntCombo.setSelectedItem(null);
		content.add(toEntCombo, c);

		c = getConstraints(0, 4);
		content.add(fromAttLbl, c);
		c = getConstraints(1, 4);
		fromAttCombo.setPreferredSize(new Dimension(200, 35));
		fromAttCombo.setVisible(false);
		content.add(fromAttCombo, c);

		c = getConstraints(0, 5);
		content.add(toAttLbl, c);
		c = getConstraints(1, 5);
		toAttCombo.setPreferredSize(new Dimension(200, 35));
		toAttCombo.setVisible(false);
		content.add(toAttCombo, c);

		c = getConstraints(1, 6);
		okBtn.setPreferredSize(new Dimension(100, 30));
		content.add(okBtn, c);
		
		// This frame
		
		this.add(content);
		
		fromEntCombo.addActionListener((e) -> {
			fromAttCombo.removeAllItems();
			for (Atribut atr : ((Entitet)fromEntCombo.getSelectedItem()).getAtributi()) {
				fromAttCombo.addItem(atr);
			}
			fromAttCombo.setVisible(true);
		});
		
		toEntCombo.addActionListener((e) -> {
			toAttCombo.removeAllItems();
			for (Atribut atr : ((Entitet)toEntCombo.getSelectedItem()).getAtributi()) {
				toAttCombo.addItem(atr);
			}
			toAttCombo.setVisible(true);
		});
		
	}
	
	private GridBagConstraints getConstraints(int x, int y) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.insets = new Insets(20, 20, 0, 0);
		c.anchor = GridBagConstraints.WEST;
		return c;
	}
	
	public void setIr(InfResurs infRes) {
		this.infRes = infRes;
	}
	
	public JButton getOkBtn(){
		return okBtn;
	}

	public InfResurs getInfRes() {
		return infRes;
	}

	public JTextField getNameTf() {
		return nameTf;
	}

	public JComboBox<Entitet> getFromEntCombo() {
		return fromEntCombo;
	}

	public JComboBox<Entitet> getToEntCombo() {
		return toEntCombo;
	}

	public JComboBox<Atribut> getFromAttCombo() {
		return fromAttCombo;
	}

	public JComboBox<Atribut> getToAttCombo() {
		return toAttCombo;
	}

	public JComboBox<Relacija> getRelationsCombo() {
		return relationsCombo;
	}
}
