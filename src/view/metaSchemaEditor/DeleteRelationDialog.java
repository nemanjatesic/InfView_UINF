package view.metaSchemaEditor;

import java.awt.Dimension;
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
import javax.swing.WindowConstants;

import actions.metaSchemaEditor.DeleteRelationAction;
import view.Frame1;
import model.Entitet;
import model.InfResurs;
import model.NodeInf;
import model.Relacija;

public class DeleteRelationDialog extends JFrame{
	private InfResurs infRes;
	
	private JPanel content = new JPanel(new GridBagLayout());
	
	private JLabel delRelLbl = new JLabel("Choose relation:");
	private JComboBox<Relacija> relationsCombo = new JComboBox<>();
	
	private JButton okBtn = new JButton("Delete");
	 
	public DeleteRelationDialog(InfResurs infRes){
		super();
		this.infRes = infRes;
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		initView();
		okBtn.addActionListener(new DeleteRelationAction(this));
	}
	
	private void initView(){
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
		
		content.add(delRelLbl, c);
		c = getConstraints(1, 0);
		relationsCombo.setPreferredSize(new Dimension(200, 35));
		relationsCombo.setSelectedItem(null);
		content.add(relationsCombo, c);
		
		c = getConstraints(1, 1);
		okBtn.setPreferredSize(new Dimension(100, 30));
		content.add(okBtn, c);
		
		
		// This frame
		this.setTitle("Delete relation");
		this.setSize(420, 170);
		this.setLocationRelativeTo(MetaSchemaEditFrame.getInstance(false));
		this.add(content);
	}
	
	private GridBagConstraints getConstraints(int x, int y) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.insets = new Insets(20, 20, 0, 0);
		c.anchor = GridBagConstraints.WEST;
		return c;
	}
	
	public void initDefault(){
		relationsCombo.setSelectedItem(null);
	}
	
	public void setIr(InfResurs infRes) {
		this.infRes = infRes;
	}

	public JComboBox<Relacija> getRelationsCombo() {
		return relationsCombo;
	}

	public JButton getOkBtn() {
		return okBtn;
	}
}
