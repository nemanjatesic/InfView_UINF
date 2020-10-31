package view.metaSchemaEditor;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import model.InfResurs;

public class AttributeEditDialog extends JFrame {
	private static AttributeEditDialog instance = null;
	private InfResurs infRes;
	
	private JPanel attributeEditPanel;
	private JButton newAttributeBtn;
	private JButton deleteAttributeBtn;
	private JButton editAttributeBtn;
	 
	private AttributeEditDialog(){
		super();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		initView();
		
	}
	
	private void initView(){
		// Panel
		newAttributeBtn = new JButton("New attribute");
		newAttributeBtn.setPreferredSize(new Dimension(130, 30));
		newAttributeBtn.addActionListener((e) -> {
			NewAttributeDialog dialog = new NewAttributeDialog();
			dialog.setVisible(true);
			this.setVisible(false);
		});
		
		deleteAttributeBtn = new JButton("Delete attribute");
		deleteAttributeBtn.setPreferredSize(new Dimension(130, 30));
		deleteAttributeBtn.addActionListener((e) -> {
			DeleteAttributeDialog dialog = new DeleteAttributeDialog();
			dialog.setVisible(true);
			this.setVisible(false);
		});
		
		editAttributeBtn = new JButton("Edit attribute");
		editAttributeBtn.setPreferredSize(new Dimension(130, 30));
		editAttributeBtn.addActionListener((e) -> {
			EditAttributeDialog dialog = new EditAttributeDialog();
			dialog.setVisible(true);
			this.setVisible(false);
		});
		
		attributeEditPanel = new JPanel();
		attributeEditPanel.add(newAttributeBtn, "wrap");
		attributeEditPanel.add(deleteAttributeBtn, "wrap");
		attributeEditPanel.add(editAttributeBtn, "wrap");
		
		// This frame
		this.setTitle("Edit attributes");
		this.setSize(260, 160);
		this.setLocationRelativeTo(MetaSchemaEditFrame.getInstance(false));
		this.add(attributeEditPanel);
	}
	
	public static AttributeEditDialog getInstance(){
		if(instance == null) {
			instance = new AttributeEditDialog();
		}
		return instance;
	}

	public InfResurs getInfRes() {
		return infRes;
	}

	public JPanel getAttributeEditPanel() {
		return attributeEditPanel;
	}

	public JButton getNewAttributeBtn() {
		return newAttributeBtn;
	}

	public JButton getDeleteAttributeBtn() {
		return deleteAttributeBtn;
	}

	public JButton getEditAttributeBtn() {
		return editAttributeBtn;
	}

	public void setInfRes(InfResurs infRes) {
		this.infRes = infRes;
	}
	
	

}
