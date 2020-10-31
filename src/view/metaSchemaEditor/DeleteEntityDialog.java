package view.metaSchemaEditor;
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

import actions.metaSchemaEditor.DeleteEntityAction;
import view.Frame1;
import model.Entitet;
import model.InfResurs;
import model.NodeInf;

public class DeleteEntityDialog extends JFrame{
	private InfResurs infRes;
	
	private JPanel content = new JPanel(new GridBagLayout());;
	
	private JLabel delEntLbl = new JLabel("Choose entity:");
	private JComboBox<Entitet> entitiesCombo = new JComboBox<>();
	
	private JButton okBtn = new JButton("Delete");
	 
	public DeleteEntityDialog(InfResurs infRes){
		super();
		this.infRes = infRes;
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		initView();
		okBtn.addActionListener(new DeleteEntityAction(infRes,this));
	}
	
	private void initView(){
		
		for (NodeInf node : (((NodeInf)Frame1.getInstance().getNodeTree().getLastSelectedPathComponent()).getChildren())) {
			if (node instanceof Entitet) {
				entitiesCombo.addItem((Entitet)node);
			}
		}
		
		// Panel
		GridBagConstraints c = getConstraints(0, 0);
		
		content.add(delEntLbl, c);
		c = getConstraints(1, 0);
		entitiesCombo.setPreferredSize(new Dimension(200, 35));
		entitiesCombo.setSelectedItem(null);
		content.add(entitiesCombo, c);
		
		c = getConstraints(1, 1);
		okBtn.setPreferredSize(new Dimension(100, 30));
		content.add(okBtn, c);
		
		// This frame
		this.setTitle("Delete entity");
		this.setSize(420, 170);
		this.setLocationRelativeTo(MetaSchemaEditFrame.getInstance(false));
		this.add(content);
		this.setVisible(true);
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
		entitiesCombo.setSelectedItem(null);
	}
	
	public void setIr(InfResurs infRes) {
		this.infRes = infRes;
	}

	public JComboBox<Entitet> getEntitiesCombo() {
		return entitiesCombo;
	}

	public JButton getOkBtn() {
		return okBtn;
	}
}
