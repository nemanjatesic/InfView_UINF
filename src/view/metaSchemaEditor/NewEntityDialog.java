package view.metaSchemaEditor;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import actions.metaSchemaEditor.NewEntityAction;
import model.InfResurs;

public class NewEntityDialog extends JFrame{
	private static NewEntityDialog instance = null;
	private InfResurs infRes;
	
	private JPanel panel;
	private JButton okBtn;
	private JLabel newEntLbl = new JLabel("Enter name:");
	private JTextField entityNameTextField = new JTextField();
	
	private NewEntityDialog(InfResurs infRes){
		super();
		this.infRes = infRes;
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		initView();
	}
	
	private void initView(){
		// Panel
		okBtn = new JButton("Create");
		okBtn.setPreferredSize(new Dimension(80, 25));
		entityNameTextField.setToolTipText("Enter entity name...");
		entityNameTextField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		entityNameTextField.setPreferredSize(new Dimension(300, 30));
		
		panel = new JPanel();
		panel.add(newEntLbl); panel.add(entityNameTextField, "wrap");
		panel.add(new JLabel("")); panel.add(okBtn, "wrap");
		
		okBtn.addActionListener(new NewEntityAction(infRes, this));
		
		// This frame
		this.setTitle("New entity");
		this.setSize(400, 120);
		this.setLocationRelativeTo(MetaSchemaEditFrame.getInstance(false));
		this.add(panel);
		
	}
	
		public static NewEntityDialog getInstance(InfResurs infRes){
			if(instance == null) {
				instance = new NewEntityDialog(infRes);
			}
			return instance;
		}
	
		
		public void setIr(InfResurs infRes) {
			this.infRes = infRes;
		}
		
		public JTextField getEntityNameTextField(){
			return entityNameTextField;
		}
}
