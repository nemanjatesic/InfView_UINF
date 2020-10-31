package state;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import view.Frame1;
import model.Entitet;
import model.file.UISERFile;

public class DeleteState extends State{

	private Entitet entity;
	
	public DeleteState(JPanel panel) {
		super(panel);
	}

	public void initState() {
		entity = Frame1.getInstance().getPanelRT().getSelectedEntitet();
		if (entity == null) return;
		
		panel.setLayout(new GridBagLayout());
		
		JButton buttonDelete = new JButton("Delete");
		buttonDelete.addActionListener(Frame1.getInstance().getActionManager().getDeleteStateAction());
		
		GridBagConstraints c = getConstraints(0, 0);
		panel.add(new JLabel("Delete one table row : "), c);
		c = getConstraints(1, 0);
		panel.add(new JLabel(""));
		
		panel.add(buttonDelete,getConstraints(2, 0));
		
		JButton button = new JButton(new ImageIcon("src/images/rb.jpg"));
		button.setEnabled(false);
		button.addActionListener(Frame1.getInstance().getActionManager().getRecycleAction());
		panel.add(button,getConstraints(3, 0));
		
		if(entity instanceof UISERFile) button.setEnabled(true);
	}
	
	private GridBagConstraints getConstraints(int x, int y){
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.insets = new Insets(20, 20, 0, 0);
        c.anchor = GridBagConstraints.WEST;
        return c;
    }
}
