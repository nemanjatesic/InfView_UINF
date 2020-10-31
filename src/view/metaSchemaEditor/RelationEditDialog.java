package view.metaSchemaEditor;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import model.InfResurs;

public class RelationEditDialog extends JFrame{
	private static RelationEditDialog instance = null;
	private InfResurs infRes;
	
	private JPanel relationEditPanel;
	private JButton newRelationBtn;
	private JButton deleteRelationBtn;
	private JButton editRealationBtn;
	 
	private RelationEditDialog(){
		super();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		initView();
	}
	
	private void initView(){
		// Panel
		newRelationBtn = new JButton("New relation");
		deleteRelationBtn = new JButton("Delete relation");
		editRealationBtn = new JButton("Edit relation");
		newRelationBtn.setPreferredSize(new Dimension(130, 30));
		deleteRelationBtn.setPreferredSize(new Dimension(130, 30));
		editRealationBtn.setPreferredSize(new Dimension(130, 30));
		
		newRelationBtn.addActionListener((e) -> {
			NewRelationDialog dialog = new NewRelationDialog(infRes);
			dialog.setIr(infRes);
			dialog.setVisible(true);
			this.setVisible(false);
		});
		
		deleteRelationBtn.addActionListener((e) -> {
			DeleteRelationDialog dialog = new DeleteRelationDialog(infRes);
			dialog.setIr(infRes);
			dialog.setVisible(true);
			this.setVisible(false);
		});
		
		editRealationBtn.addActionListener((e) -> {
			EditRelationDialog dialog = new EditRelationDialog(infRes);
			dialog.setIr(infRes);
			dialog.setVisible(true);
			this.setVisible(false);
		});
		
		relationEditPanel = new JPanel();
		relationEditPanel.add(newRelationBtn, "wrap");
		relationEditPanel.add(deleteRelationBtn, "wrap");
		relationEditPanel.add(editRealationBtn, "wrap");
		
		// This frame
		this.setTitle("Edit relations");
		this.setSize(260, 160);
		this.setLocationRelativeTo(MetaSchemaEditFrame.getInstance(false));
		this.add(relationEditPanel);
	}
	
	public static RelationEditDialog getInstance(){
		if(instance == null) {
			instance = new RelationEditDialog();
		}
		return instance;
	}
	
	public void setIr(InfResurs infRes) {
		this.infRes = infRes;
	}
	public JButton getNewRelationBtn() {
		return newRelationBtn;
	}
	public JButton getDeleteRelationBtn() {
		return deleteRelationBtn;
	}
	public JButton getEditRealationBtn() {
		return editRealationBtn;
	}
}