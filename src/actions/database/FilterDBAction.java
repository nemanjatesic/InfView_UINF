package actions.database;

import java.awt.event.ActionEvent;
import java.sql.SQLException;

import actions.AbstractActionI;
import model.db.UIDBFile;
import state.FilterState;
import view.Frame1;

public class FilterDBAction extends AbstractActionI {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		UIDBFile file = (UIDBFile) Frame1.getInstance().getPanelRT().getSelectedEntitet();
		try {
			String upit=((FilterState)Frame1.getInstance().getStateManager().getCurrentState()).getSQLCode();
			file.filterFind(upit);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
