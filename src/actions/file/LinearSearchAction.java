package actions.file;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JTextField;

import actions.AbstractActionI;
import controller.MessageController;
import view.Frame1;
import wrapper.MojPanel;
import model.file.UIAbstractFile;
import model.file.UIINDFile;
import model.file.UISEKFile;
import model.file.UISERFile;
import state.SearchState;

public class LinearSearchAction extends AbstractActionI
{

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		if (Frame1.getInstance().getPanelRT().getSelectedEntitet() == null)
			return;
		UIAbstractFile file = (UIAbstractFile)Frame1.getInstance().getPanelRT().getSelectedEntitet();
		if (file instanceof UISEKFile)
		{
			JTextField[] textFields = ((SearchState) Frame1.getInstance().getStateManager().getCurrentState())
					.getTextFields();
			ArrayList<String> searchRec = new ArrayList<>();
			for(int i=0;i<textFields.length;i++)
				searchRec.add(textFields[i].getText());

			boolean flagResetFilePointer = ((SearchState) Frame1.getInstance().getStateManager()
					.getCurrentState()).getButtonPocetakYes().isSelected();
			boolean flagStopAfterFirst = ((SearchState) Frame1.getInstance().getStateManager()
					.getCurrentState()).getButtonJedanYes().isSelected();
			boolean flagMakeNewDirectory = ((SearchState) Frame1.getInstance().getStateManager()
					.getCurrentState()).getButtonNovaYes().isSelected();

			((UISEKFile) file).linearSearchViewUpdate(Frame1.getInstance().getPanelRT().getSelectedEntitet(),
					searchRec, flagResetFilePointer, flagStopAfterFirst, flagMakeNewDirectory);
			/*((UISERFile) file).findRecord(Frame1.getInstanceOfFrame1().getPanelRT().getSelectedEntitet(),
					searchRec);*/
			// System.out.println(test);
		}
		if (file instanceof UISERFile)
		{
			JTextField[] textFields = ((SearchState) Frame1.getInstance().getStateManager().getCurrentState()).getTextFields();
			ArrayList<String> searchRec = new ArrayList<>();
			for(int i=0;i<textFields.length;i++)
				searchRec.add(textFields[i].getText());
			int position = ((UISERFile) file).findRecord(Frame1.getInstance().getPanelRT().getSelectedEntitet(),
					searchRec);
			if(position==-1)
				MessageController.errorMessage("Nije nista pronadjeno");
			else
				((MojPanel)Frame1.getInstance().getPanelRT().getTabbedPane().getSelectedComponent()).getTabela().setRowSelectionInterval(position, position);
		}
	}

}
