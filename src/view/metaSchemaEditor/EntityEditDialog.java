package view.metaSchemaEditor;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import model.InfResurs;

public class EntityEditDialog extends JFrame{
	
	public static EntityEditDialog instance = null;
	private InfResurs infRes;
	private JButton newEntity;
	private JButton deleteEntity;
	private JButton editEntity;
	
	
	private JPanel centrePanel;
	
	public EntityEditDialog(){
		super();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		initView();
	}
	
	private void initView(){
		centrePanel = new JPanel();
		newEntity = new JButton("New entity");
		editEntity = new JButton("Edit entity");
		deleteEntity = new JButton("Delete entity");
		newEntity.setPreferredSize(new Dimension(130, 30));
		editEntity.setPreferredSize(new Dimension(130, 30));
		deleteEntity.setPreferredSize(new Dimension(130, 30));
		
		deleteEntity.addActionListener((e) -> {
			DeleteEntityDialog dialog = new DeleteEntityDialog(infRes);
			dialog.setVisible(true);
			this.setVisible(false);
		});

		centrePanel.add(newEntity, "wrap");
		centrePanel.add(editEntity, "wrap");
		centrePanel.add(deleteEntity, "wrap");
		
		this.add(centrePanel, BorderLayout.CENTER);
		
		newEntity.addActionListener((e) -> {
			NewEntityDialog dialog = NewEntityDialog.getInstance(MetaSchemaEditFrame.getCurrentInstance().getIr());
			dialog.setIr(infRes);
			dialog.setVisible(true);
			this.setVisible(false);
		});
		
		editEntity.addActionListener((e) -> {
			EditEntityDialog dialog = new EditEntityDialog(infRes);
			dialog.setVisible(true);
			this.setVisible(false);
		});
		
		this.setTitle("Edit relations");
		this.setSize(260, 160);
		this.setLocationRelativeTo(MetaSchemaEditFrame.getInstance(false));
		this.add(centrePanel);
	}

	public static EntityEditDialog getInstance(){
		if(instance == null){
			instance = new EntityEditDialog();
		}
		return instance;
	}

	public InfResurs getIr() {
		return infRes;
	}

	public void setIr(InfResurs infRes) {
		this.infRes = infRes;
	}
}
