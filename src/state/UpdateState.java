package state;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Entitet;
import view.Frame1;

public class UpdateState extends State{

	private Entitet entity;
	private JTextField[] textFields;
	
	public UpdateState(JPanel panel) {
		super(panel);
	}
	
	public void initState() {
		entity = Frame1.getInstance().getPanelRT().getSelectedEntitet();
		if (entity == null) return;
		
		textFields = new JTextField[entity.getAtributi().size()];
		
		panel.setLayout(new GridBagLayout());
		
		JButton buttonAdd = new JButton("Update");
		buttonAdd.addActionListener(Frame1.getInstance().getActionManager().getUpdateDBAction());
		
		GridBagConstraints c = getConstraints(0, 0);
		panel.add(new JLabel("Update : "), c);
		c = getConstraints(1, 0);
		panel.add(new JLabel(""));
		
		int row = 0;

		for (int i = 0; i < entity.getAtributi().size(); i++) {
			if (i%3 == 0) row++;
			c = getConstraints((i*2)%6, row);
			panel.add(new JLabel(entity.getAtributi().get(i).toString() + " : "),c);
			c = getConstraints((i*2 + 1)%6, row);
			JTextField textField = new JTextField();
			textFields[i] = textField;
			textField.setPreferredSize(new Dimension(200, 35));
			panel.add(textField,c);
		}
		
		panel.add(buttonAdd,getConstraints(0, row+1));
	}
	
	private GridBagConstraints getConstraints(int x, int y){
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.insets = new Insets(20, 20, 0, 0);
        c.anchor = GridBagConstraints.WEST;
        return c;
    }
	
	public JTextField[] getTextFields() {
		return textFields;
	}
}