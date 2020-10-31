package actions.state;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import actions.AbstractActionI;
import controller.MessageController;
import view.Frame1;
import wrapper.MojPanel;
import model.Entitet;
import model.file.UISEKFile;

public class RelationsAction extends AbstractActionI{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (((MojPanel)Frame1.getInstance().getPanelRT().getTabbedPane().getSelectedComponent()).isCountOrAverage()) {
			MessageController.errorMessage("Please press refresh in order to continue");
			return;
		}
		Frame1.getInstance().getStateManager().setShowRelationsState();;
		Frame1.getInstance().getPanelRT().update();
	}

}
