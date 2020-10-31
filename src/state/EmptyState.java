package state;

import javax.swing.JPanel;

import model.Entitet;
import view.Frame1;

public class EmptyState extends State {

	private Entitet entity;
	
	public EmptyState(JPanel panel) {
		super(panel);
	}
	
	public void initState(){
		entity = Frame1.getInstance().getPanelRT().getSelectedEntitet();
		if (entity == null) return;
		
		panel = new JPanel();
	}

}
