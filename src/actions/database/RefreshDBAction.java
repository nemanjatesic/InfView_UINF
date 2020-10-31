package actions.database;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;

import actions.AbstractActionI;
import model.db.UIDBFile;
import model.file.UIAbstractFile;
import tabels.TableModel1;
import view.Frame1;
import view.PanelRT;
import wrapper.MojPanel;

public class RefreshDBAction extends AbstractActionI {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		UIDBFile file = (UIDBFile) Frame1.getInstance().getPanelRT().getSelectedEntitet();
		try {
			((MojPanel) Frame1.getInstance().getPanelRT().getTabbedPane().getSelectedComponent())
					.setCountOrAverage(false);
			((MojPanel) Frame1.getInstance().getPanelRT().getTabbedPane().getSelectedComponent()).getTabela()
					.setModel(new TableModel1(Frame1.getInstance().getPanelRT().getSelectedEntitet()));
			file.fetchNextBlock();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
