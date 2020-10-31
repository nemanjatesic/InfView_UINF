package actions.state;

import java.awt.event.ActionEvent;

import actions.AbstractActionI;
import controller.MessageController;
import view.Frame1;
import wrapper.MojPanel;

public class FilterAction extends AbstractActionI {


	private static final long serialVersionUID = -707592560208514227L;

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (((MojPanel)Frame1.getInstance().getPanelRT().getTabbedPane().getSelectedComponent()).isCountOrAverage()) {
			MessageController.errorMessage("Please press refresh in order to continue");
			return;
		}
		Frame1.getInstance().getStateManager().setFilterState();
		Frame1.getInstance().getPanelRT().update();
		
	}

}
