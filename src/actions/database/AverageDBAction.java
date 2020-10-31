package actions.database;

import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import actions.AbstractActionI;
import model.db.UIDBFile;
import state.AverageState;
import state.FilterState;
import view.Frame1;
import wrapper.MojPanel;

public class AverageDBAction extends AbstractActionI {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String selected = ((AverageState)Frame1.getInstance().getStateManager().getCurrentState()).getSelectedAtribut().getName();
		ArrayList<Boolean> tmp = ((AverageState)Frame1.getInstance().getStateManager().getCurrentState()).getSelectedButtonsList();
		Frame1.getInstance().getStateManager().setEmptyState();
		Frame1.getInstance().getPanelRT().update();
		((MojPanel)Frame1.getInstance().getPanelRT().getTabbedPane().getSelectedComponent()).setCountOrAverage(true);
		ArrayList<String> groupByList=new ArrayList<>();
		for(int i=0;i<tmp.size();i++)
		{
			if(tmp.get(i))
				groupByList.add(Frame1.getInstance().getPanelRT().getSelectedEntitet().getAtributi().get(i).getName());
		}
		UIDBFile file = (UIDBFile) Frame1.getInstance().getPanelRT().getSelectedEntitet();
		try
		{
			ResultSet rs = file.average(selected, groupByList);
			groupByList.add("AVERAGE");
			file.resultSetToMatrix(rs,groupByList);
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}