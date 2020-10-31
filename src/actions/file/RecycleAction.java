package actions.file;

import java.awt.event.ActionEvent;

import actions.AbstractActionI;
import view.Frame1;
import view.RecycleBinFrame;

public class RecycleAction extends AbstractActionI {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		RecycleBinFrame frame=new RecycleBinFrame(Frame1.getInstance().getPanelRT().getSelectedEntitet());
		frame.setVisible(true);
	}

}
