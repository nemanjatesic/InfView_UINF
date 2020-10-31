package state;

import javax.swing.JPanel;

public class State {
	protected JPanel panel;
	
	public State(JPanel panel) {
		this.panel = panel;
	}
	
	public void doAction(){};
	public void initState(){};
}
