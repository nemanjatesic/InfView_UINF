package actions.state;

import java.awt.event.ActionEvent;

import actions.AbstractActionI;
import controller.MessageController;
import view.Frame1;
import wrapper.MojPanel;

public class EditAction extends AbstractActionI{

	@Override
	public void actionPerformed(ActionEvent e) {
		if (((MojPanel)Frame1.getInstance().getPanelRT().getTabbedPane().getSelectedComponent()).isCountOrAverage()) {
			MessageController.errorMessage("Please press refresh in order to continue");
			return;
		}
		Frame1.getInstance().getStateManager().setEditState();
		Frame1.getInstance().getPanelRT().update();
	}

}