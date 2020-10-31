package actions.state;

import java.awt.event.ActionEvent;

import javax.swing.JPanel;

import actions.AbstractActionI;
import controller.MessageController;
import view.Frame1;
import view.SearchWindow;
import wrapper.MojPanel;

public class SearchAction extends AbstractActionI {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (((MojPanel)Frame1.getInstance().getPanelRT().getTabbedPane().getSelectedComponent()).isCountOrAverage()) {
			MessageController.errorMessage("Please press refresh in order to continue");
			return;
		}
		Frame1.getInstance().getStateManager().setSearchState();
		Frame1.getInstance().getPanelRT().update();
	}

}
