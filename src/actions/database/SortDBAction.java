package actions.database;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;

import actions.AbstractActionI;
import model.db.UIDBFile;
import state.SortState;
import view.Frame1;

public class SortDBAction extends AbstractActionI {

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		UIDBFile file = (UIDBFile) Frame1.getInstance().getPanelRT().getSelectedEntitet();
		int arr[] = ((SortState) Frame1.getInstance().getStateManager().getCurrentState()).getKriterijum();
		try
		{
			file.sortMDI(arr);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}