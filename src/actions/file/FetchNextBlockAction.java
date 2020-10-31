package actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.KeyStroke;

import actions.AbstractActionI;
import controller.MessageController;
import view.Frame1;
import view.PanelRT;
import model.file.UIAbstractFile;
import model.file.UIINDFile;
import model.file.UISEKFile;
import model.file.UISERFile;
import tabels.TableModel1;

public class FetchNextBlockAction extends AbstractActionI {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		PanelRT panel = Frame1.getInstance().getPanelRT();
		UIAbstractFile file = (UIAbstractFile)panel.getSelectedEntitet();
		try {
			if (file instanceof UISEKFile)
				((UISEKFile) file).fetchNextBlock(true);
			if (file instanceof UISERFile)
				((UISERFile) file).fetchNextBlockCopy();
			if (file instanceof UIINDFile)
				((UIINDFile) file).fetchNextBlock(true);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
