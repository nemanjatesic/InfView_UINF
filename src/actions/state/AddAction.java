package actions.state;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import actions.AbstractActionI;
import controller.MessageController;
import view.Frame1;
import wrapper.MojPanel;

public class AddAction extends AbstractActionI {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (((MojPanel)Frame1.getInstance().getPanelRT().getTabbedPane().getSelectedComponent()).isCountOrAverage()) {
			MessageController.errorMessage("Please press refresh in order to continue");
			return;
		}
		Frame1.getInstance().getStateManager().setAddState();
		Frame1.getInstance().getPanelRT().update();
	}

}
