package actions.file;

import java.awt.event.ActionEvent;

import actions.AbstractActionI;
import controller.MessageController;
import model.file.UIAbstractFile;
import view.Frame1;
import view.PanelRT;

public class ChangeFAction extends AbstractActionI
{

	@Override
	public void actionPerformed(ActionEvent e)
	{
		PanelRT panel=Frame1.getInstance().getPanelRT();
		String txt=panel.getTxtBlockSize().getText();
		if(txt!=null)
		{
			try
			{
				int blockSize=Integer.parseInt(txt);
				((UIAbstractFile)panel.getSelectedEntitet()).setBLOCK_SIZE(blockSize);
				panel.updateTop();
			}
			catch (NumberFormatException e2)
			{
				MessageController.errorMessage("Please, insert valid number");
				return;
			}
		}
	//	panel.getHashMap().get(panel.getSelectedEntitet())
	}
	
}
